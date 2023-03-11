pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               sh 'pwd'
               sh 'ls'
               sh 'mvn -DskipTests -P qa clean install'
            }
        }
        stage('Dev Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'ls'
                }
            }
        }
    }
}