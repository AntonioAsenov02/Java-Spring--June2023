package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.DayOfWeek;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;


@Entity
@Table(name = "forecasts")
public class Forecast extends BaseEntity {

    private DayOfWeek dayOfWeek;
    private BigDecimal maxTemperature;
    private BigDecimal minTemperature;
    private LocalTime sunrise;
    private LocalTime sunset;
    private City city;

    public Forecast() {

    }

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Column(name = "max_temperature", nullable = false)
    public BigDecimal getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(BigDecimal maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Column(name = "min_temperature", nullable = false)
    public BigDecimal getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(BigDecimal minTemperature) {
        this.minTemperature = minTemperature;
    }

    @Column(nullable = false)
    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }

    @Column(nullable = false)
    public LocalTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }

    @ManyToOne
    public City getCity() {
        return city;
    }

    public void setCity(City cityId) {
        this.city = cityId;
    }
}
