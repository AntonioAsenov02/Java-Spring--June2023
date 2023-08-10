package softuni.exam.models.dto;

public class StarExportDto {

    private String name;
    private Double lightYears;
    private String description;
    private String constellationName;


    public StarExportDto(String name, Double lightYears, String description, String constellationName) {
        this.name = name;
        this.lightYears = lightYears;
        this.description = description;
        this.constellationName = constellationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLightYears() {
        return lightYears;
    }

    public void setLightYears(Double lightYears) {
        this.lightYears = lightYears;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstellationName() {
        return constellationName;
    }

    public void setConstellationName(String constellationName) {
        this.constellationName = constellationName;
    }
}
