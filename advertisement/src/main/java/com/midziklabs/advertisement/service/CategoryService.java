package com.midziklabs.advertisement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.midziklabs.advertisement.model.CategoryModel;
import com.midziklabs.advertisement.repository.CategoryRepository;
import com.midziklabs.advertisement.request.CategoryRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryModel save(CategoryRequest category_request) {
        CategoryModel model = category_request.toCategoryModel();
        return categoryRepository.save(model);
    }
    public Optional<CategoryModel> getCategoryByName(String category_name){
        return categoryRepository.findByName(category_name);
    }
    public Optional<CategoryModel> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }
    public List<CategoryModel> getAllCategories(){
        return categoryRepository.findAll();
    }
    public CategoryModel update(CategoryRequest request, Long id){
        Optional<CategoryModel> curr_model = categoryRepository.findById(id);
        if(curr_model.isPresent()){
            curr_model.get().setName(request.getName());
            return categoryRepository.save(curr_model.get());
        } else {
            return null;
        }
    }
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }

}
