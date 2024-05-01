package com.example.snapcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
        @Index(columnList = "coordinate_x", unique = true),
        @Index(columnList = "coordinate_y", unique = true),
        @Index(columnList = "createdBy"),
})
@Entity
public class Map extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double coordinate_x;

    @Column(nullable = false)
    private Double coordinate_y;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String placeType;

    @Column(nullable = false)
    private String placeDescription;
}

