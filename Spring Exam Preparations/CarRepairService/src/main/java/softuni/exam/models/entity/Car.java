package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.CarType;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    private CarType carType;
    private String carMake;
    private String carModel;
    private Integer year;
    private String plateNumber;
    private Integer kilometers;
    private Double engine;

    public Car() {

    }

    @Enumerated(EnumType.STRING)
    @Column(name = "car_type", nullable = false)
    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    @Column(name = "car_make", nullable = false)
    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    @Column(name = "car_model", nullable = false)
    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    @Column(nullable = false)
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Column(name = "plate_number", nullable = false, unique = true)
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Column(nullable = false)
    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    @Column(nullable = false)
    public Double getEngine() {
        return engine;
    }

    public void setEngine(Double engine) {
        this.engine = engine;
    }
}
