package com.midziklabs.advertisement.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import com.midziklabs.advertisement.model.LocationModel;
import com.midziklabs.advertisement.request.AdvertisementRequest;
import com.midziklabs.advertisement.service.AdvertisementService;
import com.midziklabs.advertisement.service.CategoryService;
import com.midziklabs.advertisement.service.FileStorageService;
import com.midziklabs.advertisement.service.LocationService;

import lombok.extern.slf4j.Slf4j;

@WebMvcTest(AdvertisementController.class)
@Slf4j
public class AdvertisementControllerTest {
    List<AdvertisementModel> advertisement_list = new ArrayList<AdvertisementModel>();
    List<LocationModel> location_list = new ArrayList<LocationModel>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AdvertisementService advertisementService;
    @MockitoBean
    private LocationService locationService;
    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private FileStorageService fileStorageService;

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
        LocationModel test_location = new LocationModel();
        test_location.setName("Test Location");
        test_location.setAddress("Test Address");
        test_location.setCounty("Test County");
        test_location.setPrice(2.0);
        location_list.add(test_location);
    }

    /**
     * Test POST request
     * @throws Exception
     */
    @Test
    public void testAddAdvertisement() throws Exception {
        CategoryModel category_model = new CategoryModel(1L, "Category 1");
        Mockito.when(advertisementService.addAdvertisement(Mockito.any(AdvertisementRequest.class))).thenReturn(advertisement_list.get(0));
        Mockito.when(locationService.getLocationById(Mockito.anyLong())).thenReturn(Optional.of(location_list.get(0)));
        Mockito.when(categoryService.getCategoryById(Mockito.anyLong())).thenReturn(Optional.of(category_model));
        MockMultipartFile visuals = new MockMultipartFile("visuals", "sample.jpg", "image/jpeg", "test image content".getBytes());
        List<Long> location_ids = new ArrayList<>();
        location_ids.add(1L);
        
        log.info("Location ids: "+objectMapper.writeValueAsString(location_ids));
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/advertisement/")
                .file(visuals)
                .param("title", "Test Title")
                .param("description", "Test Description")
                .param("category_id", "1")
                .param("location_ids", objectMapper.writeValueAsString(location_ids))
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
        Mockito.when(advertisementService.getAllAdvertisements()).thenReturn(advertisement_list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/advertisement/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}


