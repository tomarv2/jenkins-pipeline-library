#!/usr/bin/env groovy

def call() {
    echo "deploying Spinnaker changes"

    def spinnakerFiles = sh(script: "if test -d _spinnaker; then ls _spinnaker; fi", returnStdout: true).trim()

    spinnakerFiles.eachLine {
        echo "applying Spinnaker template: ${it}"

        // todo: install roer on the agent
        sh "[ -f ~/roer ] || (curl -L -o ~/roer https://github.com/spinnaker/roer/releases/download/v0.11.3/roer-linux-amd64  && chmod +x ~/roer)"

        withCredentials([usernamePassword(credentialsId: 'roer', passwordVariable: 'password', usernameVariable: 'username')]) {
            sh "SPINNAKER_API=http://k8s-minion1.tomarv2.com:30084  ~/roer --fiatUser '${username}' --fiatPass '${password}' pipeline save _spinnaker/$it || true"
        }
    }

}