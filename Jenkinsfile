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
        
        stage('✅ Tests unitaires') {
            steps {
                bat 'cd backend\\BackendSidilec && mvn test'
            }
        }

        stage('🔨 Compilation Maven') {
            steps {
                bat 'cd backend\\BackendSidilec && mvn clean package -DskipTests'
            }
        }

        stage('🐳 Build Docker') {
            steps {
                bat 'docker build -t backend-sidilec backend\\BackendSidilec'
            }
        }

        stage('📦 Lancer avec Docker Compose') {
            steps {
                bat 'docker-compose -f docker-compose.yml down' // Nettoyage
                bat 'docker-compose -f docker-compose.yml up -d --build'
            }
        }
    }
}
