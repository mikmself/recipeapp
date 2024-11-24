package com.example.recipeapp.model;

import lombok.Data;

import java.util.List;

@Data
public class MealResponse {
    private List<Meal> meals;
}
