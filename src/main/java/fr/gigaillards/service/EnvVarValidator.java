package fr.gigaillards.service;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class EnvVarValidator {

    public void onStart(@Observes StartupEvent evt) {
        String CITY_API_DB_URL = System.getenv("CITY_API_DB_URL");
        String CITY_API_DB_USER = System.getenv("CITY_API_DB_USER");
        String CITY_API_DB_PWD = System.getenv("CITY_API_DB_PWD");

        shutdownIfNullOrEmpty(CITY_API_DB_URL);
        shutdownIfNullOrEmpty(CITY_API_DB_USER);
        shutdownIfNullOrEmpty(CITY_API_DB_PWD);
    }

    private void shutdownIfNullOrEmpty(String str){
        if(str == null || str.isBlank())
            Quarkus.asyncExit();

    }
}
