package softuni.exam.models.dto;

import softuni.exam.models.entity.enums.DayOfWeek;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "forecast")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForecastSeedDto {

    @XmlElement
    @NotNull
    private String dayOfWeek;
    @XmlElement
    @DecimalMin("-20")
    @DecimalMax("60")
    @NotNull
    private BigDecimal maxTemperature;
    @XmlElement
    @DecimalMin("-50")
    @DecimalMax("40")
    @NotNull
    private BigDecimal minTemperature;
    @XmlElement
    @NotNull
    private String sunrise;
    @XmlElement
    @NotNull
    private String sunset;
    @XmlElement
    private CityIdDto city;


    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    public BigDecimal getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(BigDecimal maxTemperature) {
        this.maxTemperature = maxTemperature;
    }


    public BigDecimal getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(BigDecimal minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public CityIdDto getCity() {
        return city;
    }

    public void setCity(CityIdDto city) {
        this.city = city;
    }
}
