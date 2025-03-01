package com.midziklabs.advertisement.request;

import com.midziklabs.advertisement.model.CategoryModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String name;

    public CategoryModel toCategoryModel() {
        return new CategoryModel(name);
    }

    @Override
    public String toString(){
        return "Category Request: "+this.name;
    }
}
