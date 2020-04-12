#!/usr/bin/env groovy

def call(def channel) {
    slackSend(channel: "${channel}", color: '#FFFF00', message: ":stopwatch: *STARTED:* Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
}