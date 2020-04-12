#!/usr/bin/env groovy

def call(def gitProject, def gitRepo) {

    withCredentials([usernamePassword(credentialsId: 'git-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
        sh "curl -u ${username}:${password} -X POST -H \"Accept: application/json\"  -H \"Content-Type: application/json\" \"http://tomarv2.bitbucket.com/projects/${gitProject}/repos/\" -d '{\"name\": \"${gitRepo}\"}'"
    }

}