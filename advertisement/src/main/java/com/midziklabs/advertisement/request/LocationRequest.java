package com.midziklabs.advertisement.request;

import com.midziklabs.advertisement.model.LocationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {
    private String name;
    private String address;
    private String county;
    private Double price;

    public LocationModel toLocationModel(){
        LocationModel new_model = new LocationModel();
        new_model.setName(name);
        new_model.setAddress(address);
        new_model.setCounty(county);
        new_model.setPrice(price);
        return new_model;
    }
}
