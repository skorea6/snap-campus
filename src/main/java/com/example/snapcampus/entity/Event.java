package com.example.snapcampus.entity;

import com.example.snapcampus.dto.response.event.EventDtoResponse;
import com.example.snapcampus.util.DateUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
    private LocalDate startDate;

    @Column
    private LocalDate stopDate;

    @Column
    private String organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_event_member_id"))
    @Setter
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", foreignKey = @ForeignKey(name = "fk_event_map_id"))
    @Setter
    private Map map;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = {CascadeType.ALL}, targetEntity = Post.class)
    private List<Post> posts = new ArrayList<>();

    public Event(String name, String description, LocalDate startDate, LocalDate stopDate, String organizer) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.organizer = organizer;
    }

    public Event() {

    }

    public EventDtoResponse toDto(){
        AtomicLong counter = new AtomicLong(1);

        return new EventDtoResponse(id, name, description, DateUtil.formatDate(startDate), DateUtil.formatDate(stopDate), organizer, member.toDto(), posts.stream().map(post -> {
            long currentIndex = counter.getAndIncrement();
            currentIndex = (currentIndex - 1) % 4 + 1;
            return post.toDto(currentIndex);
        }).toList());
    }
}

