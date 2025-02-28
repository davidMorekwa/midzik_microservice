package com.midziklabs.advertisement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.midziklabs.advertisement.model.AdvertisementModel;
import com.midziklabs.advertisement.repository.AdvertisementRepository;
import com.midziklabs.advertisement.request.AdvertisementRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final FileStorageService fileStorageService;

    public AdvertisementModel addAdvertisement(AdvertisementRequest request){
        AdvertisementModel model = advertisementRequestToModel(request);
        return advertisementRepository.save(model);
    }

    public List<AdvertisementModel> getAdvertisements(){
        return advertisementRepository.findAll();
    }

    private AdvertisementModel advertisementRequestToModel(AdvertisementRequest request){
        String file_path = fileStorageService.storeFile(request.getVisuals());
        AdvertisementModel model = new AdvertisementModel();
        model.setTitle(request.getTitle());
        model.setDescription(request.getDescription());
        model.setCategory_id(request.getCategory_id());
        model.setIs_approved(false);
        model.setReviewer_id(1);
        model.setUser_id(2);
        model.setFile_path(file_path);
        return model;
    }

}
