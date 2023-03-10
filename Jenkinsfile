pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               maven('3.9.0') {
                   sh '''mvn -P qa clean install'''
               }
            }
        }
    }
}