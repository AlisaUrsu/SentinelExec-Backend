package com.example.SentinelBE.repository;

import com.example.SentinelBE.model.Executable;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExecutableRepository extends JpaRepository<Executable, Long>, JpaSpecificationExecutor<Executable> {
    public Page<Executable> findAll(Specification<Executable> specification, Pageable pageable);
    public Optional<Executable> findById(long id);
    @Transactional
    @Query(
            value = "SELECT * FROM executables e WHERE e.raw_features ->> 'sha256' = :sha256",
            nativeQuery = true
    )
    Optional<Executable> findBySha256InRawFeatures(@Param("sha256") String sha256);
}
