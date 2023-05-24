package fr.gigaillards.health;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("_health")
public class Health {
    @GET
    public void healthCheck() {
        return;
    }
}
