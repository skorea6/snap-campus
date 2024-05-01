package com.example.snapcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
        @Index(columnList = "name", unique = true),
        @Index(columnList = "createdBy"),
})
@Entity
public class Event extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime stopDate;

    @Column
    private String organizer;
}

