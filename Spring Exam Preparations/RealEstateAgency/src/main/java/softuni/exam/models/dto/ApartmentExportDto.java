package softuni.exam.models.dto;

public class ApartmentExportDto {

    private Double area;
    private TownExportDto town;

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public TownExportDto getTown() {
        return town;
    }

    public void setTown(TownExportDto town) {
        this.town = town;
    }
}
