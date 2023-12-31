package com.example.xmlexercise.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserViewRootDto {

    @XmlElement(name = "user")
    private List<UserWithAtLeastOneSoldProductDto> users;


    public List<UserWithAtLeastOneSoldProductDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithAtLeastOneSoldProductDto> users) {
        this.users = users;
    }
}
