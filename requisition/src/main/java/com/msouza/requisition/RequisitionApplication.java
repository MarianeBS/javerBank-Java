package com.msouza.requisition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RequisitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequisitionApplication.class, args);
	}

}
