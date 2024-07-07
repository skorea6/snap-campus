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
                        sh 'sudo cp $snapCampusProd ./src/main/resources/application-prod.yml'
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
                    sudo cp ./docker/docker-compose.blue.yml /volume1/docker/snapcampus
                    sudo cp ./docker/docker-compose.green.yml /volume1/docker/snapcampus
                    sudo cp ./docker/Dockerfile /volume1/docker/snapcampus
                    sudo cp ./scripts/deploy.sh /volume1/docker/snapcampus
                    sudo cp ./build/libs/*.jar /volume1/docker/snapcampus
                    sudo /volume1/docker/snapcampus/deploy.sh
                '''
            }
        }
    }
}
