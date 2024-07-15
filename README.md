# 대학교 위치기반 SNS, 스냅캠퍼스
## Demo URL
- 서비스 Demo URL: [snapcampus.abz.kr](https://snapcampus.abz.kr)
    - 서버에 Docker, Nginx(loadbalancing)와 같은 기능을 함께 사용중인 관계로, 서버가 가끔 다운될 수 있습니다.

## 서비스 소개
- 캠퍼스에서 찍은 사진을 기반으로한 위치 기반형 SNS
- 학교 장소, 이벤트별로 사진을 분류하여 시각적으로 사진을 정리

## 구현 환경
- Java
- Mustache
- Spring 3.2.2
- Spring Data JPA
- Spring Security
- MySQL
- Redis
- JSON (API)
- AWS: SES, S3, Cloudfront

## Github CI/CD : Jenkins Diagram