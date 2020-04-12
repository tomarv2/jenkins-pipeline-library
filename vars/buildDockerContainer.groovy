#!/usr/bin/env groovy

def call() {
    // for backwards compatibility - refactoring to remove references to the environment
    call(env.serviceName, env.serviceVersion)
}

def call(serviceName, serviceVersion) {
    sh "docker build --build-arg NPM_TOKEN_ARG=1389c765 --build-arg version=${serviceVersion} --build-arg service=${serviceName} -t ${serviceName} \\."
}
