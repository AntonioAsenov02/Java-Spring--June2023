package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "planes")
public class Plane extends Vehicle {
    private static final String PLANE_TYPE = "PLANE";

    private int passengerCapacity;

    public Plane() {
        super(PLANE_TYPE);
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }
}
