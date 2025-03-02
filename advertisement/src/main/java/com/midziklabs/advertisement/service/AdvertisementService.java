package com.midziklabs.advertisement.service;

import java.lang.reflect.Array;
import java.lang.reflect.AccessFlag.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midziklabs.advertisement.model.AdvertisementModel;
import com.midziklabs.advertisement.model.CategoryModel;
import com.midziklabs.advertisement.model.LocationModel;
import com.midziklabs.advertisement.repository.AdvertisementRepository;
import com.midziklabs.advertisement.request.AdvertisementRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final FileStorageService fileStorageService;
    private final CategoryService categoryService;
    private final LocationService locationService;

    @Transactional
    public AdvertisementModel addAdvertisement(AdvertisementRequest request) throws JsonMappingException, JsonProcessingException{
        log.info("Request in advertisement service class");
        log.info("Request received: "+request.getLocation_ids().getClass().getName());
        String file_path = fileStorageService.storeFile(request.getVisuals());
        log.info("File path: "+file_path);
        Optional<CategoryModel> category = categoryService.getCategoryById(request.getCategory_id());

        Set<LocationModel> location_set_list = new HashSet<>();
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> location_ids = Arrays.asList(mapper.readValue(request.getLocation_ids(), Integer[].class));
        log.info("LOcation id list: "+location_ids.size());
        for (Integer id : location_ids) {
            log.info("ID in location_ids list: "+id.toString());
            Optional<LocationModel> model = locationService.getLocationById(Long.valueOf(id));
            if(model.isPresent()){
                log.info("Location found");
                location_set_list.add(model.get());
            }
        }
        log.info("Location Hash set list size: "+location_set_list.size());
        AdvertisementModel new_advertisement = request.toAdvertisementModel(file_path, category, location_set_list);
        log.info("Advertisement Model: "+new_advertisement.getLocation().size());
        return advertisementRepository.save(new_advertisement);
    }

    public List<AdvertisementModel> getAllAdvertisements(){
        return advertisementRepository.findAll();
    }

    public List<AdvertisementModel> getAdvertisementsByLocation(Long location_id){
        return advertisementRepository.findByLocation(location_id);
    }

}
