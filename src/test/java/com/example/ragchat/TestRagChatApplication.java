package com.example.ragchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestRagChatApplication {
	public static void main(String[] args) {
		SpringApplication
				.from(RagChatApplication::main)
				.with(ContainersConfiguration.class)
				.run(args);
	}
}
