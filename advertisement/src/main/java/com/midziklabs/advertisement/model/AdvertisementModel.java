package com.midziklabs.advertisement.model;

import java.util.Set;

import jakarta.persistence.Column;
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
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "reviewer_id")
    private Integer reviewer_id;
    @Column(name = "status")
    private String status;
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
    @Column(name = "file_path")
    private String file_path;
    @Column(name = "loops")
    private Integer loops;
}

