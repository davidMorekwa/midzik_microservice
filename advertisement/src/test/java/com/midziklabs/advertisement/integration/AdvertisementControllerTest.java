package com.midziklabs.advertisement.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midziklabs.advertisement.controller.AdvertisementController;
import com.midziklabs.advertisement.model.AdvertisementModel;
import com.midziklabs.advertisement.model.CategoryModel;
import com.midziklabs.advertisement.request.AdvertisementRequest;
import com.midziklabs.advertisement.service.AdvertisementService;

@WebMvcTest(AdvertisementController.class)
public class AdvertisementControllerTest {
    List<AdvertisementModel> advertisement_list = new ArrayList<AdvertisementModel>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AdvertisementService advertisementService;

    private AdvertisementRequest advertisementRequest;
    private AdvertisementModel ad1;

    @BeforeEach
    public void setup(){
        ad1 = new AdvertisementModel();
        ad1.setId(1L);
        ad1.setCategory(new CategoryModel("Category 1"));
        ad1.setDescription("Sample description");
        ad1.setIs_approved(false);
        ad1.setReviewer_id(1);
        ad1.setUser_id(2);
        ad1.setTitle("Sample title");
        advertisement_list.add(ad1);
        advertisementRequest = new AdvertisementRequest();
        advertisementRequest.setTitle("Test Title");
        advertisementRequest.setDescription("Test Description");
        advertisementRequest.setCategory_id(1L);
    }

    /**
     * Test POST request
     * @throws Exception
     */
    @Test
    public void testAddAdvertisement() throws Exception {
        Mockito.when(advertisementService.addAdvertisement(Mockito.any(AdvertisementRequest.class)))
                .thenReturn(ad1);
        MockMultipartFile visuals = new MockMultipartFile("visuals", "sample.jpg", "image/jpeg", "test image content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/advertisement/")
                .file(visuals)
                .param("title", "Test Title")
                .param("description", "Test Description")
                .param("category_id", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/advertisement/1"));
    }

    /**
     * Test get All advertisements endpoint
     */
    @Test
    public void assert_get_all_advertisments() throws Exception{
        Mockito.when(advertisementService.getAdvertisements()).thenReturn(advertisement_list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/advertisement/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}


