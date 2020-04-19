pipeline {
    agent {
        docker {
            image 'maven:11-jdk'
        }
    }

    stages {
        stage(' Clean workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Install dependencies') {
            steps {
                sh 'mvn clean verify --no-transfer-progress'
            }
        }

        stage('Test & Build application') {
            steps {
                sh 'mvn clean install --no-transfer-progress'
            }
        }
    }
}
