package com.example.recipeapp.controller;

import com.example.recipeapp.model.SavedRecipe;
import com.example.recipeapp.repository.SavedRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-recipes")
public class SavedRecipeController {
    @Autowired
    private SavedRecipeRepository savedRecipeRepository;
    @GetMapping("/{userId}")
    public ResponseEntity<List<SavedRecipe>> getSavedRecipesByUserId(@PathVariable Long userId) {
        List<SavedRecipe> savedRecipes = savedRecipeRepository.findByUserId(userId);
        return ResponseEntity.ok(savedRecipes);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSavedRecipe(@PathVariable Long id) {
        savedRecipeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
