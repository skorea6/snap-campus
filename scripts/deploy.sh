#!/bin/bash
# 작업 디렉토리를 /home/ubuntu/app으로 변경
cd /var/jenkins_home/custom/snapcampus

# 환경변수 DOCKER_APP_NAME을 spring으로 설정
DOCKER_APP_NAME=spring-snapcampus
LOG_FILE=./deploy.log


# 실행중인 blue가 있는지 확인
# 프로젝트의 실행 중인 컨테이너를 확인하고, 해당 컨테이너가 실행 중인지 여부를 EXIST_BLUE 변수에 저장
EXIST_BLUE=$(docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker-compose.blue.yml ps | grep "Up")
TEST_BLUE=$(docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker-compose.blue.yml ps)

# 배포 시작한 날짜와 시간을 기록
echo "$TEST_BLUE" >> $LOG_FILE
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

# green이 실행중이면 blue up
# EXIST_BLUE 변수가 비어있는지 확인
if [ -z "$EXIST_BLUE" ]; then

  # 로그 파일(/home/ubuntu/deploy.log)에 "blue up - blue 배포 : port:8081"이라는 내용을 추가
  echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

	# docker-compose.blue.yml 파일을 사용하여 spring-blue 프로젝트의 컨테이너를 빌드하고 실행
	docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  # 30초 동안 대기
  sleep 30

  # /home/ubuntu/deploy.log: 로그 파일에 "green 중단 시작"이라는 내용을 추가
  echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

  # docker-compose.green.yml 파일을 사용하여 spring-green 프로젝트의 컨테이너를 중지
  docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down

   # 사용하지 않는 이미지 삭제
  docker image prune -af

  echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

# blue가 실행중이면 green up
else
	echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
	docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  sleep 30

  echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE
  docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
  docker image prune -af

  echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

fi
  echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> $LOG_FILE

  echo "===================== 배포 완료 =====================" >> $LOG_FILE
  echo >> $LOG_FILE