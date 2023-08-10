package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import softuni.exam.models.entity.Country;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CitySeedDto {

    @Expose
    @Size(min = 2, max = 60)
    @NotNull
    private String cityName;
    @Expose
    @Size(min = 2)
    private String description;
    @Expose
    @Positive
    @Min(500)
    @NotNull
    private Integer population;
    @Expose
    private CountryIdDto country;



    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public CountryIdDto getCountry() {
        return country;
    }

    public void setCountry(CountryIdDto country) {
        this.country = country;
    }
}
