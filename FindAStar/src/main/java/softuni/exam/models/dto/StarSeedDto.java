package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import softuni.exam.models.entity.enums.StarType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class StarSeedDto {

    @Expose
    private String description;
    @Expose
    private Double lightYears;
    @Expose
    private String name;
    @Expose
    private StarType starType;
    @Expose
    private Long constellation;


    @Size(min = 6)
    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Positive
    @NotNull
    public Double getLightYears() {
        return lightYears;
    }

    public void setLightYears(Double lightYears) {
        this.lightYears = lightYears;
    }

    @Size(min = 2, max = 30)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public StarType getStarType() {
        return starType;
    }
    public void setStarType(StarType starType) {
        this.starType = starType;
    }


    public Long getConstellation() {
        return constellation;
    }

    public void setConstellation(Long constellation) {
        this.constellation = constellation;
    }
}
