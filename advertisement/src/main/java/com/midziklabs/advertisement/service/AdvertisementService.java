package com.midziklabs.advertisement.service;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.AccessFlag.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midziklabs.advertisement.broker.model.UserModelReplica;
import com.midziklabs.advertisement.broker.producer.RejectionMessageProducer;
import com.midziklabs.advertisement.broker.repository.UserModelReplicaRepository;
import com.midziklabs.advertisement.exceptions.StorageFileNotFoundException;
import com.midziklabs.advertisement.feignclient.model.AuthUser;
import com.midziklabs.advertisement.feignclient.repository.AuthenticationClient;
import com.midziklabs.advertisement.model.AdvertisementModel;
import com.midziklabs.advertisement.model.CategoryModel;
import com.midziklabs.advertisement.model.LocationModel;
import com.midziklabs.advertisement.repository.AdvertisementRepository;
import com.midziklabs.advertisement.requestDto.AdvertisementRejectionRequest;
import com.midziklabs.advertisement.requestDto.AdvertisementRequest;
import com.midziklabs.advertisement.responseDto.AdvertisementByUserResponse;
import com.midziklabs.advertisement.responseDto.AdvertisementWithUserResponse;
import com.midziklabs.advertisement.utils.CurrentTenantHolder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final UserModelReplicaRepository userModelReplicaRepository;
    private final FileStorageService fileStorageService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final AuthenticationClient authenticationClient;
    private final ObjectMapper objectMapper;
    private final RejectionMessageProducer rejectionMessageProducer;

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

    public List<AdvertisementWithUserResponse> getAllAdvertisements(){
        List<AdvertisementWithUserResponse> result = new ArrayList<>();
        List<AdvertisementModel> ad_list = advertisementRepository.findAll();
        for (AdvertisementModel model : ad_list) {
            log.info("Advertisement Model: "+model.getLocation().size());
            Optional<UserModelReplica> user = userModelReplicaRepository.findById(Long.valueOf(model.getUserId()));
            if(user.isPresent()){
                AdvertisementWithUserResponse ad_response = new AdvertisementWithUserResponse();
                ad_response.setId(model.getId());
                ad_response.setEmail(user.get().getEmail());
                ad_response.setTitle(model.getTitle());
                ad_response.setStatus(model.getStatus());
                ad_response.setCategory(model.getCategory());
                ad_response.setLocation(model.getLocation());
                ad_response.setAd_visual(model.getFile_path());
                ad_response.setReviewer_id(String.valueOf(model.getReviewer_id()));
                log.info("Ad Response: {}", ad_response.toString());
                result.add(ad_response);
            }
        }
        return result;
    }

    public List<AdvertisementModel> getAdvertisementsByLocation(Long location_id){
        return advertisementRepository.findByLocation(location_id);
    }

    public List<AdvertisementModel> getAdvertisementByUser(String token) throws Exception{
        try {
            ResponseEntity<?> response = authenticationClient.getAuthenticatedUser(token, CurrentTenantHolder.getCurrentTenant());
            log.info("Response:");
            if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
                throw new Exception();
            }
            Object response_body = response.getBody();
            AuthUser auth_user = objectMapper.convertValue(response_body, AuthUser.class);
            log.info(auth_user.toString());
            log.info("Authenticated user: " + auth_user.toString());
            List<AdvertisementModel> ad_list = advertisementRepository.findByUserId(auth_user.getId());
            // List<AdvertisementByUserResponse> ad_response_list = new ArrayList<>();
            // for (AdvertisementModel model : ad_list) {
            //     Resource filResource = fileStorageService.loadAsResource(model.getFile_path());
            //     AdvertisementByUserResponse ad_response = objectMapper.convertValue(model, AdvertisementByUserResponse.class);
            //     ad_response.setAd_visual(filResource);
            //     log.info("ADVERTISMENT RESPONSE OBJ");
            //     log.info(ad_response.toString());
            //     ad_response_list.add(ad_response);
            // }
            return ad_list;
        } catch (Exception e) {
            log.error("getAdvertisementByUser Error!!", e);
            throw e;
        }
    }

    public Resource getAdvertisementVisual(String filename) throws StorageFileNotFoundException{
        return fileStorageService.loadAsResource(filename);
    }
    
    public AdvertisementModel approveAdvertisement(Long id){
        Optional<AdvertisementModel> advertisement = advertisementRepository.findById(id);
        if(advertisement.isPresent()){
            advertisement.get().setStatus("Approved");
            return advertisementRepository.save(advertisement.get());
        }
        return null;
    }

    public void rejectAdvertisement(AdvertisementRejectionRequest request) throws JsonProcessingException{
        rejectionMessageProducer.sendRejectionMessage(request);
    }
}
