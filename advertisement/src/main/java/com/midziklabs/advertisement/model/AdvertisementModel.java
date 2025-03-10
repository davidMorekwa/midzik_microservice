package com.midziklabs.advertisement.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_advertisements")
@Getter
@Setter
@NoArgsConstructor
public class AdvertisementModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer user_id;
    private Integer reviewer_id;
    private Boolean is_approved;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private CategoryModel category;
    @ManyToMany
    @JoinTable(
        name = "advertisement_location",
        joinColumns = @JoinColumn(name = "advertisement_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<LocationModel> location;
    private String file_path;
    // private Integer loops;
}

