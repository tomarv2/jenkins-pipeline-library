#!/usr/bin/env groovy

def call(def branch) {

    withSonarQubeEnv('Kubernetes Sonarqube') {
        sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.branch=$branch"
    }

}
