package softuni.exam.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mechanic")
@XmlAccessorType(XmlAccessType.FIELD)
public class MechanicFirstNameDto {

    @XmlElement(name = "firstName")
    private String firstName;

    @Size(min = 2)
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
