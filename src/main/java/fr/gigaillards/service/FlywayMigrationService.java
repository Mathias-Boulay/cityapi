package fr.gigaillards.service;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;

@ApplicationScoped
public class FlywayMigrationService {

    @ConfigProperty(name = "quarkus.datasource.reactive.url")
    String dbUrl;
    @ConfigProperty(name = "quarkus.datasource.username")
    String dbUsername;
    @ConfigProperty(name = "quarkus.datasource.password")
    String dbPassword;

    public void runFlywayMigration(@Observes StartupEvent event) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:" + dbUrl, dbUsername, dbPassword)
                .load();

        System.out.println(flyway.info());
        flyway.migrate();
    }
}
