package dk.emilmadsen.namerrapi.model;

import lombok.Data;

@Data
public class NameCount {

    private Integer year;
    private Integer maleCount = 0;
    private Integer femaleCount = 0;

    public NameCount(Integer year) {
        this.year = year;
    }

}
