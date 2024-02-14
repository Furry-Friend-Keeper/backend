pipeline {
    agent any

    stages {
        stage('Clear') {
            steps {
                script {
                    echo "INFO: Clear image, container, network"
                    sh "docker rmi backendimg || true"
                    sh "docker container rm -f backend || true"
                    sh "docker network rm FFK-network || true"
                    echo "INFO: All clear!!!"
                }
            }
        }
        stage('MVN Package') {
            steps {
                script {
                    echo "INFO: Build package"
                    sh "mvn clean"
                    sh "mvn package"
                    echo "INFO: Finish build package"
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    echo "INFO: Build backend image"
                    sh "docker build -t backendimg ."
                    sh "docker network create FFK-network || true"
                    echo "INFO: Finish build backend image"
                }
            }
        }
       stage('Deploy') {
            steps {
                script {
                    echo "INFO: Deploy backend container"
                    sh "docker run -d --name backend --network FFK-network -p 5000:8080 --restart on-failure backendimg"
                    echo "INFO: Finish deploy backend container"
                }
            }
        }
    }
}
