package fr.gigaillards.resource;

import fr.gigaillards.entity.City;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("city")
public class CityResource {

    @GET
    public Uni<List<City>> findAll() {
        return City.findAll().list();
    }

    @POST
    public Uni<Response> createOne(@Valid City cityDto) {
        return cityDto.persistAndFlush()
                .onItem().ifNotNull().transform(city -> Response.ok(city).status(201).build())
                .onItem().ifNull().continueWith(Response.ok().status(400)::build);
    }
}
