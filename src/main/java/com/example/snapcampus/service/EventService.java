package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.event.EventAddRequest;
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

    public String addEvent(String memberUserId, EventAddRequest eventAddRequest){
        Event entity = eventAddRequest.toEntity();

        Map map = mapRepository.findById(eventAddRequest.getMapId()).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 map입니다.")
        );

        entity.setMember(memberRepository.findByUserId(memberUserId));
        entity.setMap(map);

        eventRepository.save(entity);
        return "이벤트가 추가되었습니다.";
    }
}
