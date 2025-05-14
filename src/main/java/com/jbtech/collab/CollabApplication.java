package com.jbtech.collab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CollabApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CollabApplication.class, args);

		// Check if SpringDoc beans are present
		String[] beanNames = context.getBeanNamesForType(org.springdoc.core.configuration.SpringDocConfiguration.class);
		if (beanNames.length > 0) {
			System.out.println("SpringDocConfiguration bean is present!");
		} else {
			System.out.println("SpringDocConfiguration bean is NOT present.  This is a problem!");
		}
	}
}
