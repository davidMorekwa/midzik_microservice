package com.midziklabs.advertisement.integration;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midziklabs.advertisement.controller.LocationController;
import com.midziklabs.advertisement.model.LocationModel;
import com.midziklabs.advertisement.request.LocationRequest;
import com.midziklabs.advertisement.service.LocationService;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LocationService locationService;

    List<LocationModel> location_list = new ArrayList<>();

    @BeforeEach
    public void setup(){
        LocationModel model = new LocationModel(1L, "Sample Name", "Sammple Address", "Sample County",2.0);
        location_list.add(model);
    }

    @Test
    public void post_location_assert_created() throws Exception{
        LocationRequest request = new LocationRequest("Test Location", "Test address", "Test county", 2.0);
        LocationModel model = new LocationModel(1L, request.getName(), request.getAddress(), request.getCounty(), request.getPrice());
        Mockito.when(locationService.saveLocation(Mockito.any(LocationRequest.class))).thenReturn(model);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/location/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/location/id/1"));
        Mockito.verify(locationService, times(1)).saveLocation(request);
    }
    @Test
    public void get_all_locations_assert_ok() throws Exception{
        Mockito.when(locationService.getAllLocations()).thenReturn(location_list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/location/").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(locationService, times(1)).getAllLocations();
    }
    @Test
    public void get_location_by_id_assert_ok() throws Exception{
        Mockito.when(locationService.getLocationById(Mockito.anyLong())).thenReturn(Optional.of(location_list.get(0)));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/location/id/{id}", 1)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(locationService, times(1)).getLocationById(1L);
    }
    @Test
    public void get_location_by_invalid_id_assert_bad_request() throws Exception{
        Mockito.when(locationService.getLocationById(Mockito.anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/location/id/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(locationService, times(1)).getLocationById(1L);
    }
    @Test
    public void update_location_assert_ok() throws Exception{
        LocationRequest request = new LocationRequest("Test Location", "Test address", "Test county", 2.0);
        Mockito.when(locationService.updateLocation(Mockito.any(LocationRequest.class), Mockito.anyLong())).thenReturn(location_list.get(0));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/location/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sample Name"));
    }

}
