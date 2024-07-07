package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.map.MapPlaceAddDtoRequest;
import com.example.snapcampus.dto.request.map.MapPlaceDeleteDtoRequest;
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
    private final AwsS3Service awsS3Service;


    public List<MapDtoResponse> getAllPlace(){
        List<Map> maps = mapRepository.findAll();
        return maps.stream().map(Map::toDto).toList();
    }

    public MapAddDtoResponse addPlace(String memberUserId, MapPlaceAddDtoRequest mapPlaceAddDtoRequest){
        // TODO: 이름 중복 검사 + x, y 좌표 중복 검사 (시간되면)
        Map entity = mapPlaceAddDtoRequest.toEntity();
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

    public String deletePlace(MapPlaceDeleteDtoRequest mapPlaceDeleteDtoRequest) {
        Map map = mapRepository.findById(mapPlaceDeleteDtoRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 id입니다.")
        );

        // 현재는 Admin만 삭제하게끔 허용하기 때문에 작성자와 로그인 유저가 일치하는지 확인하는 과정은 필요 없음
        map.getEvents().forEach(
                e -> e.getPosts().forEach(
                        p -> p.getImages().forEach(awsS3Service::deleteFile)
                )
        ); // 기존 이미지 파일 삭제 (s3)

        mapRepository.deleteById(map.getId());
        return "장소가 삭제되었습니다.";
    }
}
