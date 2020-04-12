#!/usr/bin/env groovy

def call(def gitProject, def gitRepo) {

    withCredentials([usernamePassword(credentialsId: 'git-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
        sh """ 
            curl -u ${username}:${password} -H "Content-Type: application/json" -X PUT "http://demo.bitbucket.com/rest/webhook/1.0/projects/${gitProject}/repos/${gitRepo}/configurations" -d '
            {
                "title": "'script-webhook'",  "url": "http://dynamic-jenkins.services.demo.com/bitbucket-scmsource-hook/notify","enabled": true
            }' 
        """
    }

}
