#!/usr/bin/env groovy

def call(def gitProject, def gitRepo, def reviewerNames) {

    withCredentials([usernamePassword(credentialsId: 'git-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
        sh """ 
            wget http://tomarv2.bitbucket.com/projects/CIJ/repos/pipeline-library/raw/vars/add-reviewers.sh -O add-reviewers.sh
            chmod +x add-reviewers.sh
            cat add-reviewers.sh
            sleep 5
            ./add-reviewers.sh ${username} ${password} ${gitProject} ${gitRepo} ${reviewerNames}
        """
    }

}
