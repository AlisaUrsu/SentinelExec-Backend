package com.example.SentinelBE.repository;

import com.example.SentinelBE.model.Scan;
import com.example.SentinelBE.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ScanRepository extends JpaRepository<Scan, Long>, JpaSpecificationExecutor<Scan> {
    public Page<Scan> findAll(Specification<Scan> specification, Pageable pageable);
    public Optional<Scan> findById(long id);
    public List<Scan> findByUser(User user);
}
