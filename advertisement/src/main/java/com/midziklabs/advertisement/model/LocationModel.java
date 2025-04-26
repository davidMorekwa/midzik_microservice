package com.midziklabs.advertisement.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
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
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "county")
    private String county;
    @Column(name = "price")
    private Double price;
    // @ManyToMany(mappedBy = "location")
    // private Set<AdvertisementModel> advertisements = new HashSet<>();
}
