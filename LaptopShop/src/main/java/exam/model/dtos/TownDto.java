package exam.model.dtos;

import com.google.gson.annotations.Expose;

public class TownDto {

    @Expose
    private String name;

    public TownDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
