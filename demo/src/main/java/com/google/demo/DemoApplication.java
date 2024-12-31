package com.google.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		// 控制台提示信息
		System.out.println("Application is running! Open http://localhost:8080 in your browser.");
	}
}
