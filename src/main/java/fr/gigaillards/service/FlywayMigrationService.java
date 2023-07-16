package fr.gigaillards.service;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.flyway.runtime.FlywayContainer;
import io.quarkus.flyway.runtime.FlywayContainerProducer;
import io.quarkus.flyway.runtime.QuarkusPathLocationScanner;
import io.quarkus.runtime.Quarkus;

import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FlywayMigrationService {
    @Inject
    Logger logger;

    @ConfigProperty(name = "quarkus.datasource.reactive.url")
    String datasourceUrl;

    @ConfigProperty(name = "quarkus.datasource.username")
    String datasourceUsername;

    @ConfigProperty(name = "quarkus.datasource.password")
    String datasourcePassword;

    @ConfigProperty(name = "todo.migration.files")
    List<String> files;

    public void init() {
        logger.info("Initialising flyway...");
        logger.info("Checking required environment variables...");

        String emptyVars = getEmptyVars(
                new String[]{"CITY_API_DB_URL", "CITY_API_DB_USER", "CITY_API_DB_PWD"},
                datasourceUrl, datasourceUsername, datasourcePassword
        );

        if (emptyVars != null) {
            logger.error("Empty environment variables: " + emptyVars);
            Quarkus.asyncExit();
            return;
        }
        logger.info("Got environment variables successfully !");

        QuarkusPathLocationScanner.setApplicationMigrationFiles(files.stream()
                .map(file -> "db/migration/" + file)
                .collect(Collectors.toList()));
        DataSource datasource = Flyway.configure()
                .dataSource(getDatasourceUrl(), datasourceUsername, datasourcePassword)
                .getDataSource();
        try (InstanceHandle<FlywayContainerProducer> instance = Arc.container().instance(FlywayContainerProducer.class)) {
            FlywayContainerProducer flywayProducer = instance.get();
            FlywayContainer flywayContainer = flywayProducer.createFlyway(datasource, "<default>", true, true);
            Flyway flyway = flywayContainer.getFlyway();
            flyway.migrate();
        }
    }

    private String getDatasourceUrl() {
        if (datasourceUrl.startsWith("vertx-reactive:")) {
            return "jdbc:" + datasourceUrl.substring("vertx-reactive:".length());
        } else {
            return "jdbc:" + datasourceUrl;
        }
    }

    @Nullable
    private String getEmptyVars(String[] varNames, String... vars) {
        String emptyVars = null;
        for (int i = 0; i < vars.length; i++) {
            String str = vars[i];
            if (str != null && !str.isBlank()) {
                continue;
            }

            if (emptyVars == null) {
                emptyVars = varNames[i];
            } else {
                emptyVars += ", " + varNames[i];
            }
        }
        return emptyVars;
    }
}
