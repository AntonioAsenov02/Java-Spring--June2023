package exam.model.dtos;

import com.sun.istack.NotNull;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "shop")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopSeedDto {

    @XmlElement
    @Size(min = 4)
    @NotNull
    private String address;
    @XmlElement(name = "employee-count")
    @Min(1)
    @Max(50)
    @NotNull
    private Integer employeeCount;
    @XmlElement
    @DecimalMin("20000")
    @NotNull
    private BigDecimal income;
    @XmlElement
    @Size(min = 4)
    @NotNull
    private String name;
    @XmlElement(name = "shop-area")
    @Min(150)
    @NotNull
    private Integer shopArea;
    @XmlElement
    private TownNameDto town;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShopArea() {
        return shopArea;
    }

    public void setShopArea(Integer shopArea) {
        this.shopArea = shopArea;
    }

    public TownNameDto getTown() {
        return town;
    }

    public void setTown(TownNameDto town) {
        this.town = town;
    }
}
