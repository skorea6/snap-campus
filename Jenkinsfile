pipeline{
    agent any
    tools {
        gradle 'gradle 8.6'
    }
    environment {
        GIT_COMMIT_MESSAGE = ''
    }
    stages{
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    GIT_COMMIT_MESSAGE = sh(
                        script: "git log -1 --pretty=%B",
                        returnStdout: true
                    ).trim()
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
                    cp ./docker/docker-compose.blue.yml /var/jenkins_home/custom/snapcampus
                    cp ./docker/docker-compose.green.yml /var/jenkins_home/custom/snapcampus
                    cp ./docker/Dockerfile /var/jenkins_home/custom/snapcampus
                    cp ./scripts/deploy.sh /var/jenkins_home/custom/snapcampus
                    cp ./build/libs/*.jar /var/jenkins_home/custom/snapcampus
                    chmod +x /var/jenkins_home/custom/snapcampus/deploy.sh
                    /var/jenkins_home/custom/snapcampus/deploy.sh
                '''
            }
        }
    }

    post {
        success {
            slackSend (
                color: '#00FF00',
                message: "성공: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}). 최근 커밋: '${env.GIT_COMMIT_MESSAGE}'"
            )
        }
        failure {
            slackSend (
                color: '#FF0000',
                message: "실패: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}). 최근 커밋: '${env.GIT_COMMIT_MESSAGE}'"
            )
        }
    }
}
