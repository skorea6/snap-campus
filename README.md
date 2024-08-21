# 대학교 위치기반 SNS, 스냅캠퍼스
## Demo URL
- 서비스 Demo URL: [snapcampus.abz.kr](https://snapcampus.abz.kr)
    - 서버에 Docker, Nginx(loadbalancing)와 같은 기능을 함께 사용중인 관계로, 서버가 가끔 다운될 수 있습니다.

## 서비스 소개
- 캠퍼스에서 찍은 사진을 기반으로한 위치 기반형 SNS
- 학교 장소, 이벤트별로 사진을 분류하여 시각적으로 사진을 정리

![image](https://github.com/user-attachments/assets/459f7059-93ea-41bc-9e82-b2253ec99827)
![스크린샷 2024-07-15 오후 7 04 29](https://github.com/user-attachments/assets/0a9665d1-a7a0-4e0a-a71a-24dee4386784)
![스크린샷 2024-07-15 오후 7 04 41](https://github.com/user-attachments/assets/9b17a07e-212e-4ee9-b16b-8ace78fba935)


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
![다운로드](https://github.com/user-attachments/assets/48e57703-5842-434d-a06a-8972ae78eb24)

- Jenkins를 이용하여 스프링 무중단 CI/CD 배포를 구현하였습니다.
- Synology NAS 서버를 활용하였고, Docker의 DOOD 구조를 사용하여 하나의 서버 안에 Blue&Green Docker, Jenkins Docker를 설치하였습니다.
- 자세한 내용은 제 블로그 포스팅을 참고해주세요!
  - [(1) Jenkins 스프링 무중단 CI/CD 배포 구현 - Jenkins Docker 설치](https://skorea6.tistory.com/entry/1-Jenkins-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%AC%B4%EC%A4%91%EB%8B%A8-CICD-%EB%B0%B0%ED%8F%AC-%EA%B5%AC%ED%98%84-Jenkins-Docker-%EC%84%A4%EC%B9%98)
  - [(2) Jenkins 스프링 무중단 CI/CD 배포 구현 - Github 연동/Credentials](https://skorea6.tistory.com/entry/2-Jenkins-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%AC%B4%EC%A4%91%EB%8B%A8-CICD-%EB%B0%B0%ED%8F%AC-%EA%B5%AC%ED%98%84-Github-%EC%97%B0%EB%8F%99-Credentials)
  - [(3) Jenkins 스프링 무중단 CI/CD 배포 구현 - Docker Blue&Green, Jenkins 빌드설정](https://skorea6.tistory.com/entry/3-Jenkins-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%AC%B4%EC%A4%91%EB%8B%A8-CICD-%EB%B0%B0%ED%8F%AC-%EA%B5%AC%ED%98%84-Docker-BlueGreen-%EB%B9%8C%EB%93%9C%EC%84%A4%EC%A0%95)
