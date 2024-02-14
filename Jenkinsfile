pipeline {
    agent any
    
    tools { 
      maven 'MAVEN_HOME' 
      jdk 'JAVA_HOME' 
    }
    
    stages {
        stage('Clear') {
            steps {
                script {
                    echo "INFO: Clear image, container"
                    sh "docker rmi backendimg || true"
                    sh "docker container rm -f backend || true"
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
                    echo "INFO: Finish build backend image"
                }
            }
        }
       stage('Deploy') {
            steps {
                script {
                    echo "INFO: Deploy backend container"
                    sh "docker run -d --name backend --network FFK-network -p 5000:8080  -e SPRING_DATASOURCE_URL=jdbc:mysql://ffkdb:3306/furry-friend-keeper --restart on-failure backendimg"
                    echo "INFO: Finish deploy backend container"
                }
            }
        }
    }
}
