pipeline{
    agent any
    environment {
        SCRIPT_PATH = '/var/jenkins_home/custom/snapcampus'
    }
    tools {
        gradle 'gradle 8.6'
    }
    stages{
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Get Commit Message') {
            steps {
                script {
                    def gitCommitMessage = sh(
                        script: "git log -1 --pretty=%B",
                        returnStdout: true
                    ).trim()
                    echo "Commit Message: ${gitCommitMessage}"
                    env.GIT_COMMIT_MESSAGE = gitCommitMessage
                }
            }
        }
        stage('Prepare'){
            steps {
                sh 'gradle clean'
            }
        }
        stage('Replace Prod Properties') {
            steps {
                withCredentials([file(credentialsId: 'snapCampusProd', variable: 'snapCampusProd')]) {
                    script {
                        sh 'cp $snapCampusProd ./src/main/resources/application-prod.yml'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'gradle build -x test'
            }
        }
        stage('Test') {
            steps {
                sh 'gradle test'
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                    cp ./docker/docker-compose.blue.yml ${SCRIPT_PATH}
                    cp ./docker/docker-compose.green.yml ${SCRIPT_PATH}
                    cp ./docker/Dockerfile ${SCRIPT_PATH}
                    cp ./scripts/deploy.sh ${SCRIPT_PATH}
                    cp ./build/libs/*.jar ${SCRIPT_PATH}
                    chmod +x ${SCRIPT_PATH}/deploy.sh
                    ${SCRIPT_PATH}/deploy.sh
                '''
            }
        }
    }

    post {
        success {
            slackSend (
                color: '#00FF00',
                message: "성공: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}). 최근 커밋: '${env.GIT_COMMIT_MESSAGE}'",
                iconEmoji: ":tada:",
                username: "봇이야"
            )
        }
        failure {
            slackSend (
                color: '#FF0000',
                message: "실패: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}). 최근 커밋: '${env.GIT_COMMIT_MESSAGE}'",
                iconEmoji: ":tada:",
                username: "봇이야"
            )
        }
    }
}
