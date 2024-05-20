package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.map.MapPlaceAddRequest;
import com.example.snapcampus.dto.response.map.MapResponse;
import com.example.snapcampus.entity.Map;
import com.example.snapcampus.repository.MapRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class MapService {
    private final MapRepository mapRepository;

    public List<MapResponse> getAllPlace(){
        List<Map> maps = mapRepository.findAll();
        return maps.stream().map(Map::toDto).toList();
    }

    public String addPlace(MapPlaceAddRequest mapPlaceAddRequest){
        // TODO: 이름 중복 검사 + x, y 좌표 중복 검사 (시간되면)
        Map entity = mapPlaceAddRequest.toEntity();
        mapRepository.save(entity);

        return "장소가 추가되었습니다.";
    }
}
