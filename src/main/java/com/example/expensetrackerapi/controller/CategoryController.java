package com.example.expensetrackerapi.controller;

import javax.servlet.http.HttpServletRequest;

import com.example.expensetrackerapi.constants.GlobalConstants;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConstants.API_ENDPOINT + "/categories")
public class CategoryController {
    
    @GetMapping(value ="")
    public String getAllCategories(HttpServletRequest req){
        Integer userId = (Integer) req.getAttribute("userId");
        return "Authenticated User id  : "+ userId;
    }
}
