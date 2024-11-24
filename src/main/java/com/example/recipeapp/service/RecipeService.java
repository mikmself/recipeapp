package com.example.recipeapp.service;

import com.example.recipeapp.model.SavedRecipe;
import com.example.recipeapp.model.SearchHistory;
import com.example.recipeapp.model.User;
import com.example.recipeapp.repository.SavedRecipeRepository;
import com.example.recipeapp.repository.SearchHistoryRepository;
import com.example.recipeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    @Autowired
    private SavedRecipeRepository savedRecipeRepository;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    public String saveRecipe(SavedRecipe savedRecipe, Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            savedRecipe.setUser(user);
            savedRecipeRepository.save(savedRecipe);
            return "Recipe saved successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save recipe: " + e.getMessage());
        }
    }
    public void saveSearchHistory(String searchQuery, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setSearchQuery(searchQuery);
        searchHistory.setUser(user);
        searchHistoryRepository.save(searchHistory);
    }
}
