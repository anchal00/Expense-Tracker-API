package com.example.expensetrackerapi.service;

import java.util.List;

import com.example.expensetrackerapi.entity.Category;
import com.example.expensetrackerapi.exception.EtBadRequestException;
import com.example.expensetrackerapi.exception.EtResourceNotFoundException;

public interface CategoryService {
    
    public List<Category> fetchAllCategories(Integer userId);


    public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;

    Category addCategory(Integer userId, String title, String description) throws EtBadRequestException;

    void updateCatgory(Integer userId, Integer categoryId , Category category) throws EtBadRequestException;

    void removeCategoryWithAllTransactions(Integer userId , Integer categoryId) throws EtResourceNotFoundException;

}
