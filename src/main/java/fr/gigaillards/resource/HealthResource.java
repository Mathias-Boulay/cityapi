package fr.gigaillards.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("_health")
public class HealthResource {
    @GET
    public void healthCheck() {
        return;
    }
}
