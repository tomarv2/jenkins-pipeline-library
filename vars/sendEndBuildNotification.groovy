#!/usr/bin/env groovy

def call(def status, def channel) {
    call(status, channel, "no build description available")
}

def call(def status, def channel, def buildDescription) {
    def message = "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'\n${buildDescription}\n\n${env.BUILD_URL}"
    switch (status) {
        case 'SUCCESS':
            slackSend(channel: "${channel}", color: '#00FF00', message: ":tada: *SUCCESSFUL:* ${message}")
            break

        case 'UNSTABLE':
            break

        case 'FAILURE':
            slackSend(channel: "${channel}", color: '#FF0000', message: ":facepalm: :sadpanda: *FAILED:* ${message}")
            break
    }
}