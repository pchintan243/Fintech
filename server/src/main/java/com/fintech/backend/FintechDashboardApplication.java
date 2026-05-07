package com.fintech.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Fintech Dashboard - Spring Boot Application Entry Point
 *
 * Architecture: Controller → Service Interface → Service Implementation
 * Security:     JWT-based stateless authentication (Spring Security 6)
 * Persistence:  Spring Data JPA (database-first, PostgreSQL)
 * Auditing:     BaseAuditEntity — auto-tracks createdAt, lastModified,
 *               createdByIdf, lastModifiedByIdf on every entity
 */
@Slf4j
@SpringBootApplication
@EnableAsync
public class FintechDashboardApplication {

    private final Environment environment;

    public FintechDashboardApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FintechDashboardApplication.class);
        app.run(args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String[] activeProfiles = environment.getActiveProfiles();
        String profiles = activeProfiles.length > 0
                ? String.join(", ", activeProfiles)
                : "default";

        log.info("""
                \n
                ╔══════════════════════════════════════════════════════╗
                ║           Fintech Dashboard API — STARTED            ║
                ╠══════════════════════════════════════════════════════╣
                ║  Local   : http://localhost:{}{}
                ║  Network : http://{}:{}{}
                ║  Profile : {}
                ╚══════════════════════════════════════════════════════╝
                """,
                port, contextPath,
                host, port, contextPath,
                profiles
        );
    }
}
