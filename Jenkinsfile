pipeline {
    agent any

//     stages {
        stage('Fetch') {

//             steps {
//                 git branch: 'main', url: 'https://github.com/VilchinskiOleg/mutual-aid-app-version-2.0-release.git/'
//             }
        }

        stage('Build') {

            steps {
                maven('3.9.0') {
                    sh '''mvn -P qa clean install'''
                }
            }
        }

        stage('Deploy') {

//             environment {
//                 AN_ACCESS_KEY = credentials('my-predefined-secret-text')
//             }

        }
//     }
}