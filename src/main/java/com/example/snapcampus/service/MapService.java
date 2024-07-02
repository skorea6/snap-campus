package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.map.MapPlaceAddRequest;
import com.example.snapcampus.dto.request.map.MapPlaceGetRequest;
import com.example.snapcampus.dto.response.map.MapDtoResponse;
import com.example.snapcampus.entity.Map;
import com.example.snapcampus.repository.MapRepository;
import com.example.snapcampus.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional
@Service
public class MapService {
    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;

    public List<MapDtoResponse> getAllPlace(){
        List<Map> maps = mapRepository.findAll();
        return maps.stream().map(Map::toDto).toList();
    }

    public String addPlace(String memberUserId, MapPlaceAddRequest mapPlaceAddRequest){
        // TODO: 이름 중복 검사 + x, y 좌표 중복 검사 (시간되면)
        Map entity = mapPlaceAddRequest.toEntity();
        entity.setMember(memberRepository.findByUserId(memberUserId));
        mapRepository.save(entity);

        return "장소가 추가되었습니다.";
    }

    public MapDtoResponse getPlace(Long id){
        Map map = mapRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("이미 등록된 아이디입니다.")
        );

        return map.toDto();
    }
}
