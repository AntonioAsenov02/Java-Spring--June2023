package softuni.exam.models.dto;

import java.math.BigDecimal;

public class OfferExportDto {

    private Long id;
    private BigDecimal price;
    private AgentExportDto agent;
    private ApartmentExportDto apartment;

    public OfferExportDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AgentExportDto getAgent() {
        return agent;
    }

    public void setAgent(AgentExportDto agent) {
        this.agent = agent;
    }

    public ApartmentExportDto getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentExportDto apartment) {
        this.apartment = apartment;
    }
}
