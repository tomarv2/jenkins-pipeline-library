#!/usr/bin/env groovy

def call() {
    return call(null, null, null)
}


// push to both ECR and DCR for now

def call(serviceName, dockerRepo, imageTag) { // todo: standardize naming conventions, e.g. repository = tomarv2/services/personnel
    
    def serviceNameVar
    def dockerPath
    def imageTagVar

    // for backward compatibility
    if(serviceName != null) {
        serviceNameVar = serviceName
        dockerPath = dockerRepo
        imageTagVar = imageTag
    } else {
        serviceNameVar = env.serviceName
        dockerPath = env.dockerPath
        imageTagVar = env.imageTag
    }
    
    sh "docker tag ${serviceNameVar} dcr.tomarv2.com/${dockerPath}:${imageTagVar}"
    sh "docker push dcr.tomarv2.com/${dockerPath}:${imageTagVar}"


    // todo: this should probably be done by spinnaker when it deploys an image to QA (or any other environment)
    sh "docker tag ${serviceNameVar} dcr.tomarv2.com/${dockerPath}:qa"
    sh "docker push dcr.tomarv2.com/${dockerPath}:qa"


    try {
        tagAndPushDockerContainerAWS(serviceNameVar, dockerPath, imageTagVar)
    } catch (exception) {
        // handle build agents that aren't set up for AWS yet
    }

}
