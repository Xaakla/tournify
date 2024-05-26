package com.app.tournify.dtos;

import jakarta.validation.constraints.NotBlank;

public class TeamDto {
    @NotBlank
    private String name;

    @NotBlank
    private String acronym;

    @NotBlank
    private String imageId;

    @NotBlank
    private String color;

    public TeamDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
