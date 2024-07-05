package com.example.snapcampus.entity;

import com.example.snapcampus.dto.response.map.MapDetailDtoResponse;
import com.example.snapcampus.dto.response.map.MapDtoResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_map_member_id"))
    @ToString.Exclude
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "map", cascade = {CascadeType.ALL}, targetEntity = Event.class)
    @ToString.Exclude
    private List<Event> events = new ArrayList<>();

    public Map(Double coordinate_x, Double coordinate_y, String placeName, String placeType, String placeDescription) {
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeDescription = placeDescription;
    }

    public Map() {

    }

    public MapDetailDtoResponse toDetailDto(){
        return new MapDetailDtoResponse(id, coordinate_x, coordinate_y, placeName, placeType, placeDescription, member.toDto(), events.stream().map(Event::toDetailDto).toList());
    }

    public MapDtoResponse toDto(){
        return new MapDtoResponse(id, coordinate_x, coordinate_y, placeName, placeType, placeDescription, member.toDto());
    }
}

