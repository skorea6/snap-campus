package com.example.snapcampus.repository;

import com.example.snapcampus.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
