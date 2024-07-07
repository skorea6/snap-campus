pipeline{
    agent any
    stages{
        stage('Prepare'){
            steps {
                sh 'gradle clean'
            }
        }
        stage('Replace Prod Properties') {
            withCredentials([file(credentialsId: 'snapCampusProd', variable: 'snapCampusProd')]) {
                script {
                    sh 'cp $snapCampusProd ./src/main/resources/application-prod.yml'
                }
            }
        }
        stage('Build') {
            steps {
                sh 'gradlew build -x test'
            }
        }
        stage('Test') {
            steps {
                sh 'gradlew test'
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                    cp ./docker/docker-compose.blue.yml /volume1/docker/snapcampus
                    cp ./docker/docker-compose.green.yml /volume1/docker/snapcampus
                    cp ./docker/Dockerfile /volume1/docker/snapcampus
                    cp ./scripts/deploy.sh /volume1/docker/snapcampus
                    cp ./build/libs/*.jar /volume1/docker/snapcampus
                    sudo /volume1/docker/snapcampus/deploy.sh
                '''
            }
        }
    }
}
