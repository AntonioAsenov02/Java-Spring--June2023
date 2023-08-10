package com.example.jsonexercise.model.dto;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.Size;

public class UserSeedDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private int age;

    public UserSeedDto() {

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 3)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
