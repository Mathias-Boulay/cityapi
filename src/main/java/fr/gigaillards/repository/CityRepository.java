package fr.gigaillards.repository;

import fr.gigaillards.entity.City;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class CityRepository {
    @Inject
    Mutiny.SessionFactory sf;

    public Uni<Response> createCity(City city) {
        return sf.withTransaction(s -> s.persist(city))
                .replaceWith(Response.ok(city).status(Response.Status.CREATED)::build);
    }

    public Uni<List<City>> findAll() {
        return sf.withTransaction(s -> s
                .createNamedQuery("City.findAll", City.class)
                .getResultList()
        );
    }
}
