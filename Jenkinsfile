pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    echo "INFO: Clear image+container | Build backend image"
                    sh "docker rmi backendimg || true"
                    sh "docker container rm -f backend || true"
                    sh "docker network rm FFK-network || true"
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
