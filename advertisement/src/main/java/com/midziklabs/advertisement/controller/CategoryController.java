package com.midziklabs.advertisement.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.midziklabs.advertisement.model.CategoryModel;
import com.midziklabs.advertisement.request.CategoryRequest;
import com.midziklabs.advertisement.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    public final CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<?> getAllCategories(){
        List<CategoryModel> category_list = categoryService.getAllCategories();
        return ResponseEntity.ok().body(category_list);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Integer id){
        Optional<CategoryModel> category = categoryService.getCategoryById(Long.valueOf(id));
        if(category.isPresent()){
            return ResponseEntity.ok().body(category.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable("name") String category_name){
        Optional<CategoryModel> category = categoryService.getCategoryByName(category_name);
        if (category.isPresent()) {
            return ResponseEntity.ok().body(category.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryRequest category){
        log.info("Request received: "+category.toString());
        CategoryModel new_category = categoryService.save(category);
        URI location = URI.create(String.format("/api/v1/category/id/%s", new_category.getId()));
        return ResponseEntity.created(location).body(new_category);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer id){
        categoryService.deleteCategory(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editCategory(@RequestBody CategoryRequest request, @PathVariable("id") Integer id){
        CategoryModel new_model = categoryService.update(request, Long.valueOf(id));
        return ResponseEntity.ok().body(new_model);
    }

}
