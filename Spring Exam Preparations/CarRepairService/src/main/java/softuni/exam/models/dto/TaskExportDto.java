package softuni.exam.models.dto;

import java.math.BigDecimal;

public class TaskExportDto {

    private Long id;
    private BigDecimal price;
    private CarExportDto car;
    private MechanicExportDto mechanic;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CarExportDto getCar() {
        return car;
    }

    public void setCar(CarExportDto car) {
        this.car = car;
    }

    public MechanicExportDto getMechanic() {
        return mechanic;
    }

    public void setMechanic(MechanicExportDto mechanic) {
        this.mechanic = mechanic;
    }
}
