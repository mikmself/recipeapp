package com.example.recipeapp.service;

import com.example.recipeapp.model.Meal;
import com.example.recipeapp.model.MealResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class MealService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://www.themealdb.com/api/json/v1/1";
    public List<Meal> searchMealsByName(String name) {
        String url = BASE_URL + "/search.php?s=" + name;
        MealResponse response = restTemplate.getForObject(url, MealResponse.class);
        return response != null ? response.getMeals() : null;
    }
    public Meal getMealDetailsById(String id) {
        String url = BASE_URL + "/lookup.php?i=" + id;
        MealResponse response = restTemplate.getForObject(url, MealResponse.class);
        return response != null && !response.getMeals().isEmpty() ? response.getMeals().get(0) : null;
    }
    public List<Meal> filterMealsByIngredient(String ingredient) {
        String url = BASE_URL + "/filter.php?i=" + ingredient;
        MealResponse response = restTemplate.getForObject(url, MealResponse.class);
        return response != null ? response.getMeals() : null;
    }
}
