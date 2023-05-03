package fr.gigaillards.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class City extends PanacheEntity {
    @NotBlank(message = "Department code cannot be blank")
    @Column(name = "department_code")
    public String departmentCode;

    @NotBlank(message = "Zip Code cannot be blank")
    @Column(name = "zip_code")
    public String zipCode;

    @NotBlank(message = "Name cannot be blank")
    public String name;

    public Float lat;

    public Float lon;
}
