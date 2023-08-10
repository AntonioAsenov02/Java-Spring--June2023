package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trucks")
public class Truck extends Vehicle {
    private static final String TRUCK_TYPE = "TRUCK";

    @Column(name = "load_capacity")
    private Double loadCapacity;

    public Truck() {
        super(TRUCK_TYPE);
    }

    public Double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(Double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }
}
