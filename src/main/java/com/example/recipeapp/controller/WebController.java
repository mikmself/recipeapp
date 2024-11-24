package com.example.recipeapp.controller;

import com.example.recipeapp.model.Meal;
import com.example.recipeapp.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {
    @Autowired
    private MealService mealService;
    @GetMapping("/update-profile")
    public String updateProfilePage() {
        return "update-profile";
    }
    @GetMapping("/")
    public String home() {
        return "recipes";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    @GetMapping("/recipes")
    public String recipesPage() {
        return "recipes";
    }
    @GetMapping("/recipes/{id}")
    public String recipeDetails(@PathVariable String id, Model model) {
        Meal meal = mealService.getMealDetailsById(id);
        model.addAttribute("meal", meal);
        return "recipe-details";
    }
    @GetMapping("/saved-recipes")
    public String savedRecipesPage() {
        return "saved-recipes";
    }
}

