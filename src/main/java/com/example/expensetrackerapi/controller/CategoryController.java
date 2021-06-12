package com.example.expensetrackerapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.entity.Category;
import com.example.expensetrackerapi.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConstants.API_ENDPOINT + "/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "")
    public ResponseEntity<List<Category>> getAllCategories(HttpServletRequest req) {

        Integer userId = (Integer) req.getAttribute("userId");

        List<Category> allCats = categoryService.fetchAllCategories(userId);
        return new ResponseEntity<>(allCats, HttpStatus.OK);

    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> updateCategory(HttpServletRequest req, @PathVariable int categoryId,
            @RequestBody Category category) {

        Integer userId = (Integer) req.getAttribute("userId");
        
        categoryService.updateCatgory(userId, categoryId, category);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);

        return new ResponseEntity<>(map, HttpStatus.OK);
        
    }

    @GetMapping(value = "/{categoryId}")
    public ResponseEntity<Category> getCategory(HttpServletRequest req, @PathVariable int categoryId) {

        Integer userId = (Integer) req.getAttribute("userId");

        Category categoryFound = categoryService.fetchCategoryById(userId, categoryId);

        return new ResponseEntity<Category>(categoryFound, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<Category> addCategory(HttpServletRequest req, HttpServletResponse rep,
            @RequestBody Map<String, Object> categoryMap) {

        int userId = (Integer) req.getAttribute("userId");
        String title = (String) categoryMap.get("title");
        String description = (String) categoryMap.get("description");

        Category category = categoryService.addCategory(userId, title, description);
        return new ResponseEntity<Category>(category, HttpStatus.CREATED);

    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(HttpServletRequest req, HttpServletResponse rep,
            @PathVariable("categoryId") Integer categoryId) {

        int userId = (Integer) req.getAttribute("userId");
       
        categoryService.removeCategoryWithAllTransactions(userId, categoryId);
        return new ResponseEntity<>(Map.of("Success", true), HttpStatus.OK);

    }
}
