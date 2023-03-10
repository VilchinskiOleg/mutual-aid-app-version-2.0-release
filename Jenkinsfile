pipeline {
    agent any
    tools {
        maven 'maven 3.9.0'
    }
    stages {
        stage('Build') {
            steps {
               sh ''' echo "Inside dir = ${pwd}" '''
               sh ''' echo "Have dirs = ${ls}" '''
               sh 'mvn -P qa clean install'
            }
        }
    }
}