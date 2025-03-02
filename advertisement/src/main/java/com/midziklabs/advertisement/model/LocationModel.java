package com.midziklabs.advertisement.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String county;
    private Double price;
    // @ManyToMany(mappedBy = "location")
    // private Set<AdvertisementModel> advertisements = new HashSet<>();
}
