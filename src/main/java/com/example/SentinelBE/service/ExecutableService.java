package com.example.SentinelBE.service;

import com.example.SentinelBE.model.Executable;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ExecutableService {
    @Transactional
    Executable addExecutable(Executable executable);
    @Transactional
    Executable updateExecutable(Executable executable);
    @Transactional
    void deleteExecutable(long executableId);
    @Transactional
    Executable getExecutable(long executableId);

    @Transactional
    Page<Executable> findAllByCriteria(String executableName, Pageable pageable);

}
