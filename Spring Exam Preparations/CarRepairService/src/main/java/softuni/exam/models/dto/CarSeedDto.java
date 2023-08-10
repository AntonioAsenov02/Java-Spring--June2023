package softuni.exam.models.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarSeedDto {

    @XmlElement
    @Size(min = 2, max = 30)
    @NotNull
    private String carMake;

    @XmlElement
    @Size(min = 2, max = 30)
    @NotNull
    private String carModel;

    @XmlElement
    @Positive
    @NotNull
    private Integer year;

    @XmlElement
    @Size(min = 2, max = 30)
    @NotNull
    private String plateNumber;

    @XmlElement
    @Positive
    @NotNull
    private Integer kilometers;

    @XmlElement
    @DecimalMin(value = "1.00")
    @NotNull
    private Double engine;

    @XmlElement
    @NotNull
    private String carType;



    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public Double getEngine() {
        return engine;
    }

    public void setEngine(Double engine) {
        this.engine = engine;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
