package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OffersSeedRootDto {

    @XmlElement
    private List<OffersSeedDto> offers;

    public OffersSeedRootDto() {

    }

    public List<OffersSeedDto> getOffers() {
        return offers;
    }

    public void setOffers(List<OffersSeedDto> offers) {
        this.offers = offers;
    }
}
