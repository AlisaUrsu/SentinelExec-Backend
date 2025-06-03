package com.example.SentinelBE.service;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.repository.ExecutableRepository;
import com.example.SentinelBE.utils.validation.GenericValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExecutableServiceImpl implements ExecutableService{
    private final UserService userService;
    private final ExecutableRepository executableRepository;
    private final GenericValidator<Executable> excutableValidator;

    @Override
    @Transactional
    public Executable addExecutable(Executable executable) {
        try {
            if (executable == null) {
                throw new IllegalArgumentException("Executable cannot be null");
            }
            String sha256 = (String) executable.getRawFeatures().get("sha256");
            Optional<Executable> existingExec = executableRepository.findBySha256InRawFeatures(sha256);

            if (existingExec.isPresent()) {
                Executable exec = existingExec.get();
                exec.setUpdatedAt(LocalDateTime.now());
                return executableRepository.save(exec);
            }

            if (executable.getFirstDetection() == null) {
                executable.setFirstDetection(LocalDateTime.now());
            }

            // validate
            excutableValidator.validate(executable);
            return executableRepository.save(executable);
        } catch (Exception e) {
            throw new RuntimeException("Error adding executable: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Executable updateExecutable(Executable executable) {
        var updatedExecutable = executableRepository.findById(executable.getId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Executable with id %d not found", executable.getId()))
        );

        try{
            excutableValidator.validate(updatedExecutable);
            return executableRepository.save(updatedExecutable);
        } catch (Exception e) {
            throw new RuntimeException("Error updating executable: " + e.getMessage());
        }
    }

    @Override
    public void deleteExecutable(long executableId) {
        try {
            executableRepository.deleteById(executableId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting executable: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Executable getExecutable(long executableId) {
        Optional<Executable> executable = executableRepository.findById(executableId);
        if(executable.isEmpty()) {
            throw new EntityNotFoundException(String.format("Executable with id %d not found", executableId));
        }
        Hibernate.initialize(executable.get().getReporters());
        Hibernate.initialize(executable.get().getScans());
        Executable retrievedExecutable = executable.get();
        Hibernate.initialize(retrievedExecutable.getReporters());
        Hibernate.initialize(retrievedExecutable.getScans());
        return retrievedExecutable;
    }

    @Override
    @Transactional
    public Page<Executable> findAllByCriteria(String executableName, String label, Pageable pageable) {
        // use specification and criteria builder
        Specification<Executable> specification = (root, query, criteriaBuilder) -> {
            // predicates for partial search
            List<Predicate> predicates = new ArrayList<>();
            //if (executableId != null) {
            //    predicates.add(criteriaBuilder.equal(root.get("id"), executableId));
            //}
            if (executableName != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + executableName + "%"));
            }

            if (label != null) {
                predicates.add(criteriaBuilder.equal(root.get("label"), label));
            }

            //predicates.add(criteriaBuilder.isNotEmpty(root.get("reporters")));
            Predicate isReported = criteriaBuilder.isNotNull(root.get("firstReport"));
            predicates.add(isReported);


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Executable> found = executableRepository.findAll(specification, pageable);
        found.forEach(executable -> {
            Hibernate.initialize(executable.getScans());
            Hibernate.initialize(executable.getReporters());

        });
        return found;
    }

    @Override
    @Transactional
    public Executable getExecutableByHash(String hash) {
        Executable executable = executableRepository.findBySha256InRawFeatures(hash).orElseThrow(
                () -> new EntityNotFoundException("Executable not found"));Hibernate.initialize(executable.getReporters());

        return executable;
    }
}
