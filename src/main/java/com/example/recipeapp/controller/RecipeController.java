package com.example.recipeapp.controller;

import com.example.recipeapp.model.Meal;
import com.example.recipeapp.model.SavedRecipe;
import com.example.recipeapp.model.SearchHistory;
import com.example.recipeapp.repository.SearchHistoryRepository;
import com.example.recipeapp.security.JwtUtil;
import com.example.recipeapp.service.MealService;
import com.example.recipeapp.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private MealService mealService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/{id}")
    public Meal getMealDetails(@PathVariable String id) {
        return mealService.getMealDetailsById(id);
    }
    @GetMapping("/filter")
    public List<Meal> filterMealsByIngredient(@RequestParam String ingredient) {
        return mealService.filterMealsByIngredient(ingredient);
    }
    @PostMapping("/save")
    public String saveRecipe(@RequestBody SavedRecipe savedRecipe, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtUtil.extractUserId(token); 
            return recipeService.saveRecipe(savedRecipe, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to save recipe: " + e.getMessage();
        }
    }
    @GetMapping("/search")
    public List<Meal> searchMeals(@RequestParam String name, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtUtil.extractUserId(token);
            recipeService.saveSearchHistory(name, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mealService.searchMealsByName(name);
    }
    @GetMapping("/search/history")
    public List<SearchHistory> getSearchHistory(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtUtil.extractUserId(token);
            return searchHistoryRepository.findByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch search history");
        }
    }
    @DeleteMapping("/search/history/{id}")
    public String deleteSearchHistory(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long userId = jwtUtil.extractUserId(token);
            searchHistoryRepository.findById(id).ifPresent(history -> {
                if (history.getUser().getId().equals(userId)) {
                    searchHistoryRepository.delete(history);
                } else {
                    throw new RuntimeException("Unauthorized to delete this search history");
                }
            });

            return "Search history deleted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to delete search history: " + e.getMessage();
        }
    }
}
