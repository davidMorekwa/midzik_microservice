package com.midziklabs.advertisement.integration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midziklabs.advertisement.controller.CategoryController;
import com.midziklabs.advertisement.model.CategoryModel;
import com.midziklabs.advertisement.request.CategoryRequest;
import com.midziklabs.advertisement.service.CategoryService;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    List<CategoryModel> category_list = new ArrayList<>();
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    private void setup(){
        CategoryModel category = new CategoryModel(1L, "Category 1");
        category_list.add(category);
    }

    /**
     * Test get All categories
     * @throws Exception
     */
    @Test
    public void assert_get_all_categories() throws Exception{
        Mockito.when(categoryService.getAllCategories()).thenReturn(category_list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    /**
     * @throws Exception
     */
    @Test
    public void get_category_by_id_assert_ok() throws Exception{
        Mockito.when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category_list.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/id/{id}", 1)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(categoryService, times(1)).getCategoryById(1L);
    }
    /**
     * @throws Exception
     */
    @Test
    public void get_category_by_id_assert_no_content() throws Exception{
        Mockito.when(categoryService.getCategoryById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/id/{id}", 2)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    /**
     * @throws Exception
     */
    @Test
    public void get_category_by_name_assert_ok() throws Exception{
        Mockito.when(categoryService.getCategoryByName("Category 1")).thenAnswer(new Answer<Optional<CategoryModel>>() {
            public Optional<CategoryModel> answer(InvocationOnMock invocationOnMock){
                return category_list.stream().filter(category -> category.getName().equals("Category 1")).findFirst();
            }
        });
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/name/{name}", "Category 1")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
    /**
     * @throws Exception
     */
    @Test
    public void get_category_by_name_assert_no_content() throws Exception{
        Mockito.when(categoryService.getCategoryByName("Category 1")).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/name/{name}", "Category 1")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    /**
     * @throws Exception
     */
    @Test
    public void post_category_assert_created() throws Exception{
        CategoryRequest test_request = new CategoryRequest("Category 2");
        CategoryModel test_model = new CategoryModel(2L, "Category 2");
        Mockito.when(categoryService.save(Mockito.any(CategoryRequest.class))).thenReturn(test_model);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/category/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(test_request))
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/category/id/2"));
        Mockito.verify(categoryService, times(1)).save(Mockito.any(CategoryRequest.class));
    }
    /**
     * @throws Exception
     */
    @Test
    public void update_category_assert_ok() throws Exception{
        CategoryModel updated_model = new CategoryModel(3L, "Updated Model");
        Mockito.when(categoryService.update(Mockito.any(CategoryRequest.class), Mockito.anyLong())).thenReturn(updated_model);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/category/{id}", 2)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updated_model))
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
