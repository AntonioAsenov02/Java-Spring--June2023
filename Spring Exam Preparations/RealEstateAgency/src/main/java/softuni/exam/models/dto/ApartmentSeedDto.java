package softuni.exam.models.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "apartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentSeedDto {

    @XmlElement
    @NotNull
    private String apartmentType;
    @XmlElement
    @DecimalMin("40")
    @NotNull
    private Double area;
    @XmlElement
    private String town;

    public ApartmentSeedDto() {

    }

    public String getApartmentType() {
        return apartmentType;
    }
    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
