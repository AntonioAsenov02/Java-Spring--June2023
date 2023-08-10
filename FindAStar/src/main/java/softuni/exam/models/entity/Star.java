package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.StarType;

import javax.persistence.*;

@Entity
@Table(name = "stars")
public class Star extends BaseEntity {

    private String description;
    private Double lightYears;
    private String name;
    private StarType starType;
    private Constellation constellation;

    public Star() {

    }

    @Column(columnDefinition = "TEXT", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "light_years", nullable = false)
    public Double getLightYears() {
        return lightYears;
    }

    public void setLightYears(Double lightYears) {
        this.lightYears = lightYears;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "star_type", nullable = false)
    public StarType getStarType() {
        return starType;
    }

    public void setStarType(StarType starType) {
        this.starType = starType;
    }

    @ManyToOne
    public Constellation getConstellation() {
        return constellation;
    }

    public void setConstellation(Constellation constellation) {
        this.constellation = constellation;
    }
}
