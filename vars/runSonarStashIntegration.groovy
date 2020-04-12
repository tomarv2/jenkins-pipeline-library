#!/usr/bin/env groovy

def call(def branch, def gitProject, def gitRepo) {

    withSonarQubeEnv('Kubernetes Sonarqube') {
        withCredentials([usernamePassword(credentialsId: 'git-credentials', passwordVariable: 'jenkins_pwd', usernameVariable: 'jenkins_user')]) {
            def prId = branch.substring(branch.lastIndexOf('-') + 1)
            sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar -Dsonar.analysis.mode=issues -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.branch=$branch -Dsonar.git.notification=true -Dsonar.git.project=$gitProject -Dsonar.git.repository=$gitRepo -Dsonar.git.password=$jenkins_pwd -Dsonar.git.pullrequest.id=$prId"
        }
    }

}


