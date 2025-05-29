package com.example.SentinelBE.service;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.model.Scan;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScanService {
    @Transactional
    Scan addScan(Scan scan);
    //@Transactional
    //Scan updateScan(Scan scan);
    //@Transactional
    //void deleteScan(long scanId);
    @Transactional
    Scan getScan(long scanId);

    @Transactional
    Page<Scan> findAllByCriteria(String label, String filename, Boolean isReported, String user, Pageable pageable);

}
