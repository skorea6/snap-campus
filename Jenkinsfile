pipeline{
    agent any
    tools {
        gradle 'gradle 8.6'
    }
    stages{
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
                    cp ./docker/docker-compose.blue.yml /custom/snapcampus
                    cp ./docker/docker-compose.green.yml /custom/snapcampus
                    cp ./docker/Dockerfile /custom/snapcampus
                    cp ./scripts/deploy.sh /custom/snapcampus
                    cp ./build/libs/*.jar /custom/snapcampus
                    /custom/snapcampus/deploy.sh
                '''
            }
        }
    }
}
