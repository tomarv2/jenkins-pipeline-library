#!/usr/bin/env groovy

def call(def envDeprecated) {
    return "v${env.BUILD_NUMBER}"
}

