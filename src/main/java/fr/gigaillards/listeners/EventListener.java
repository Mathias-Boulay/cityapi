package fr.gigaillards.listeners;

import fr.gigaillards.service.FlywayMigrationService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventListener {

    @Inject
    FlywayMigrationService flywayMigrationService;

    public void onStart(@Observes StartupEvent evt) {
        flywayMigrationService.init();
    }

}
