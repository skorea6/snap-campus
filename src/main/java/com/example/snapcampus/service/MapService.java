package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.map.MapPlaceAddRequest;
import com.example.snapcampus.dto.response.map.MapAddDtoResponse;
import com.example.snapcampus.dto.response.map.MapDetailDtoResponse;
import com.example.snapcampus.dto.response.map.MapDtoResponse;
import com.example.snapcampus.entity.Map;
import com.example.snapcampus.repository.MapRepository;
import com.example.snapcampus.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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

    public MapAddDtoResponse addPlace(String memberUserId, MapPlaceAddRequest mapPlaceAddRequest){
        // TODO: 이름 중복 검사 + x, y 좌표 중복 검사 (시간되면)
        Map entity = mapPlaceAddRequest.toEntity();
        entity.setMember(memberRepository.findByUserId(memberUserId));
        Map savedMap = mapRepository.save(entity);

        return new MapAddDtoResponse(savedMap.getId());
    }

    public MapDetailDtoResponse getPlace(Long id){
        Map map = mapRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 장소입니다.")
        );

        return map.toDetailDto();
    }
}
