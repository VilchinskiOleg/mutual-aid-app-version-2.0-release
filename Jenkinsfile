pipeline {
    agent any
    tools {
        maven 'maven 3.9.0'
    }
    stages {
        stage('Build') {
            steps {
               sh 'mvn -P qa clean install'
            }
        }
    }
}