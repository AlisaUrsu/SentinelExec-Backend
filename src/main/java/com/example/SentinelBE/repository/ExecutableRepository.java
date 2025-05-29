package com.example.SentinelBE.repository;

import com.example.SentinelBE.model.Executable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ExecutableRepository extends JpaRepository<Executable, Long>, JpaSpecificationExecutor<Executable> {
    public Page<Executable> findAll(Specification<Executable> specification, Pageable pageable);
    public Optional<Executable> findById(long id);
}
