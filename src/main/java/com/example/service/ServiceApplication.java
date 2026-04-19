package com.example.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner debugBeans(ApplicationContext ctx) {
        return args -> {
            System.out.println("====================================");
            System.out.println("Beans implementing AuthService:");
            for (String name : ctx.getBeanNamesForType(com.example.service.service.AuthService.class)) {
                System.out.println(" - " + name + " -> " + ctx.getBean(name).getClass().getName());
            }
            System.out.println("====================================");

            // Print controller mappings (optional)
            try {
                var rmm = ctx.getBean(org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.class);
                System.out.println("Registered request mappings:");
                rmm.getHandlerMethods().forEach((k, v) -> System.out.println(k));
            } catch (Exception e) {
                System.out.println("Cannot print mappings: " + e.getMessage());
            }
        };
    }
}
