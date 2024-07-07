package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.event.EventAddDtoRequest;
import com.example.snapcampus.dto.request.event.EventDeleteDtoRequest;
import com.example.snapcampus.entity.Event;
import com.example.snapcampus.entity.Map;
import com.example.snapcampus.repository.EventRepository;
import com.example.snapcampus.repository.MapRepository;
import com.example.snapcampus.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final MapRepository mapRepository;
    private final AwsS3Service awsS3Service;

    public String addEvent(String memberUserId, EventAddDtoRequest eventAddDtoRequest){
        Event entity = eventAddDtoRequest.toEntity();

        Map map = mapRepository.findById(eventAddDtoRequest.getMapId()).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 map입니다.")
        );

        entity.setMember(memberRepository.findByUserId(memberUserId));
        entity.setMap(map);

        eventRepository.save(entity);
        return "이벤트가 추가되었습니다.";
    }

    public String deleteEvent(EventDeleteDtoRequest eventDeleteDtoRequest){
        Event event = eventRepository.findById(eventDeleteDtoRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("id를 찾을 수 없습니다.")
        );

        // 현재는 Admin만 삭제하게끔 허용하기 때문에 작성자와 로그인 유저가 일치하는지 확인하는 과정은 필요 없음
        event.getPosts().forEach(
                p -> p.getImages().forEach(awsS3Service::deleteFile)
        ); // 기존 이미지 파일 삭제 (s3)

        eventRepository.deleteById(event.getId());
        return "이벤트가 삭제되었습니다.";
    }
}
