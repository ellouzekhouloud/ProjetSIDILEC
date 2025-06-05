pipeline {
    agent any

    tools {
        maven 'Maven_3.9.4'
        jdk 'JDK_21'
    }

    environment {
        SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/controle_Qualite'
        SPRING_DATASOURCE_USERNAME = 'root'
        SPRING_DATASOURCE_PASSWORD = 'root'
    }

    stages {
        stage('Debug Git Branch') {
            steps {
                sh 'git branch -a'
            }
        }

        stage('🔨 Compilation Maven') {
            steps {
                sh 'cd backend/BackendSidilec && mvn clean package -DskipTests'
            }
        }

        stage('🐳 Build Docker') {
            steps {
                sh 'docker build -t backend-sidilec ./backend/BackendSidilec'
            }
        }

        stage('📦 Lancer avec Docker Compose') {
            steps {
                sh 'docker-compose -f docker-compose.yml up -d --build'
            }
        }
    }
}
