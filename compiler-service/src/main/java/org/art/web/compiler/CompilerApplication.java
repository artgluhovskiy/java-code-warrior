package org.art.web.compiler;

import org.art.web.compiler.service.api.CompilationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CompilerApplication {

    private final CompilationService compilationService;

    @Autowired
    public CompilerApplication(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CompilerApplication.class, args);
    }

    @Bean
    public CommandLineRunner showInfo() {
        return args -> {
            System.out.println(compilationService);
        };
    }
}
