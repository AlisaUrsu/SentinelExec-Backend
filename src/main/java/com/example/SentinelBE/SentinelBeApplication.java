package com.example.SentinelBE;

import com.example.SentinelBE.repository.ExecutableRepository;
import com.example.SentinelBE.security.config.RsaKeyProperties;
import com.example.SentinelBE.service.ExecutableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SentinelBeApplication {
	@Autowired
	private ExecutableRepository executableRepository;

	@Autowired
	private ExecutableService executableService;

	public static void main(String[] args) {
		SpringApplication.run(SentinelBeApplication.class, args);
	}

}
