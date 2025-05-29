package com.example.SentinelBE.service;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.model.Scan;
import com.example.SentinelBE.repository.ScanRepository;
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
public class ScanServiceImpl implements ScanService{
    private final ScanRepository scanRepository;
    private final GenericValidator<Scan> validator;
    @Override
    public Scan addScan(Scan scan) {
        try {
            if (scan == null) {
                throw new IllegalArgumentException("Scan cannot be null");
            }
            if (scan.getScannedAt() == null) {
                scan.setScannedAt(LocalDateTime.now());
            }

            // validate
            validator.validate(scan);
            return scanRepository.save(scan);
        } catch (Exception e) {
            throw new RuntimeException("Error adding scan: " + e.getMessage());
        }
    }

    @Override
    public Page<Scan> findAllByCriteria(String label, String filename, Boolean isReported, String user, Pageable pageable) {
        Specification<Scan> specification = (root, query, criteriaBuilder) -> {
            // predicates for partial search
            List<Predicate> predicates = new ArrayList<>();

            if (label != null) {
                predicates.add(criteriaBuilder.like(root.get("label"), "%" + label + "%"));
            }
            if (filename != null) {
                Join<Object, Object> executableJoin = root.join("executable");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(executableJoin.get("name")), "%" + filename.toLowerCase() + "%"));
            }
            if (isReported != null) {
                predicates.add(criteriaBuilder.equal(root.get("reported"), isReported));
            }
            if (user != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("username"), user));
            }

            if(predicates.isEmpty() || query == null) return null;
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        Page<Scan> found = scanRepository.findAll(specification, pageable);
        found.forEach(executable -> {
            Hibernate.initialize(executable.getExecutable());

        });
        return found;
    }

    @Override
    @Transactional
    public Scan getScan(long scanId) {
        Optional<Scan> scan = scanRepository.findById(scanId);
        if(scan.isEmpty()) {
            throw new EntityNotFoundException(String.format("Scan with id %d not found", scanId));
        }
        Hibernate.initialize(scan.get().getExecutable());
        Hibernate.initialize(scan.get().getExecutable().getScans());
        Hibernate.initialize(scan.get().getExecutable().getReporters());
        Scan retrievedScan = scan.get();
        Hibernate.initialize(retrievedScan.getExecutable());
        Hibernate.initialize(retrievedScan.getExecutable().getReporters());
        Hibernate.initialize(retrievedScan.getExecutable().getScans());
        return retrievedScan;
    }
}
