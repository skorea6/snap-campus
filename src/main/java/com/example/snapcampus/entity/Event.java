package com.example.snapcampus.entity;

import com.example.snapcampus.dto.response.event.EventAggregateDtoResponse;
import com.example.snapcampus.dto.response.event.EventDetailDtoResponse;
import com.example.snapcampus.util.DateUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    @ToString.Exclude
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", foreignKey = @ForeignKey(name = "fk_event_map_id"))
    @Setter
    @ToString.Exclude
    private Map map;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = {CascadeType.ALL}, targetEntity = Post.class)
    @ToString.Exclude
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

    public EventDetailDtoResponse toDetailDto(){
        AtomicLong counter = new AtomicLong(1);

        return new EventDetailDtoResponse(
                id, name, description, DateUtil.formatDate(startDate), DateUtil.formatDate(stopDate),
                organizer, member.toDto(),
                posts.stream().map(post -> post.toDetailDto(counter)).toList()
        );
    }

    public EventAggregateDtoResponse toAggregateDto(){
        return new EventAggregateDtoResponse(id, name, description, DateUtil.formatDate(startDate), DateUtil.formatDate(stopDate), organizer, member.toDto(), map.toDto());
    }
}

