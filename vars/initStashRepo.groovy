#!/usr/bin/env groovy

def call(def gitProject, def gitRepo) {

    withCredentials([usernamePassword(credentialsId: 'git-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
        sh "git init"
        sh "git add --all"
        sh "git commit -m 'Initial Commit'"
        sh "git remote add origin http://${username}:${password}@demo.github.com/scm/${gitProject}/${gitRepo}.git"
        sh "git push -u origin master"
    }
}