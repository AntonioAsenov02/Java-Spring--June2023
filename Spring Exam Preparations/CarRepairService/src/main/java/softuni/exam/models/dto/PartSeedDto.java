package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.*;

public class PartSeedDto {

    @Expose
    private String partName;
    @Expose
    private Double price;
    @Expose
    private Integer quantity;

    public PartSeedDto() {

    }

    @Size(min = 2, max = 19)
    @NotNull
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @DecimalMin(value = "10")
    @DecimalMax(value = "2000")
    @NotNull
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Positive
    @NotNull
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
