#!/usr/bin/env groovy

def call(def currentBuild) {

    if(env.hasSpinnakerChanges == "true") {
      echo "deploying spinnaker changes"

        env.changedFiles.eachLine {
            if (it =~ /^_spinnaker\//) {
                echo "deploy file: ${it}"

                sh "[ -f ~/roer ] || curl -L -o ~/roer https://github.com/spinnaker/roer/releases/download/v0.11.3/roer-linux-amd64  && chmod +x ~/roer"
                withCredentials([usernamePassword(credentialsId: 'roer', passwordVariable: 'username', usernameVariable: 'password')]) {
                    sh "SPINNAKER_API=http://k8s-minion1.tomarv2.com:30084  ~/roer --fiatUser ${username} --fiatPass ${password} pipeline save $it"
                }
            }
        }

    }

    if(env.hasKubernetesChanges == "true") {
      echo "deploying kubernetes changes"
    }

    if(env.hasCodeChanges == "false") { //or manually started?
        currentBuild.result = 'ABORTED'
        error('No new code to build')
    }
}