package com.example.expensetrackerapi.repository;

import java.util.List;

import com.example.expensetrackerapi.entity.Category;
import com.example.expensetrackerapi.exception.EtBadRequestException;
import com.example.expensetrackerapi.exception.EtResourceNotFoundException;

public interface CategoryRepository {
    
    List<Category> findAll(Integer userId) throws EtResourceNotFoundException;
    
    Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;

    Integer create(Integer userId, String title, String desc) throws EtBadRequestException;
    
    void update(Integer userId , Integer categoryId , Category category) throws EtBadRequestException;

    void removeById(Integer userId , Integer categoryId); 
}
