package fr.gigaillards.service;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.flyway.runtime.FlywayContainer;
import io.quarkus.flyway.runtime.FlywayContainerProducer;
import io.quarkus.flyway.runtime.QuarkusPathLocationScanner;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FlywayMigrationService
{
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

    public void init()
    {
        logger.info("Initialising flyway...");
        logger.info("Checking required environment variables...");

        String missingVars = getMissingVars(
                new String[]{"CITY_API_DB_URL", "CITY_API_DB_USER", "CITY_API_DB_PWD"},
                datasourceUrl, datasourceUsername, datasourcePassword
        );
        if(missingVars != null){
            logger.error("Missing environment variables: "+missingVars);
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
        try (InstanceHandle<FlywayContainerProducer> instance = Arc.container().instance(FlywayContainerProducer.class))
        {
            FlywayContainerProducer flywayProducer = instance.get();
            FlywayContainer flywayContainer = flywayProducer.createFlyway(datasource, "<default>", true, true);
            Flyway flyway = flywayContainer.getFlyway();
            flyway.migrate();
        }
    }

    private String getDatasourceUrl()
    {
        if (datasourceUrl.startsWith("vertx-reactive:"))
            return "jdbc:" + datasourceUrl.substring("vertx-reactive:".length());
        else
            return "jdbc:" + datasourceUrl;
    }

    @Nullable
    private String getMissingVars(String[] varNames, String... vars){
        String missingVars = null;
        for(int i = 0; i < vars.length; i++){
            String str = vars[i];
            if(str != null && !str.isBlank())
                continue;

            if(missingVars == null)
                missingVars = varNames[i];
            else
                missingVars += ", "+varNames[i];
        }
        return missingVars;
    }
}
