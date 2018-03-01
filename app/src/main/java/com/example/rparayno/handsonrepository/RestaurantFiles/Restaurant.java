package com.example.rparayno.handsonrepository.RestaurantFiles;

/**
 * Created by rparayno on 02/02/2018.
 */

public class Restaurant {


    private String name, description;
    private String weight;

    public Restaurant() {
    }

    public Restaurant(String name, String description, String weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}