package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

public class CountryIdDto {

    @Expose
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
