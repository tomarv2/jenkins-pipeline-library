#!/usr/bin/env groovy

def call(def env) {

    env.commitHash = getGitCommitHash()
    env.namespace = env.namespace != null ? env.namespace : "services"
    env.registryNamespace = env.registryNamespace != null ? env.registryNamespace : env.namespace

    def props = readProperties file: 'sonar-project.properties'
    env.serviceName = props['sonar.projectName']
    env.dockerPath = "tomarv2/${env.registryNamespace}/${env.serviceName}"
    env.dockerRepo = env.dockerPath
    env.imageTag = buildDockerTag(env.BRANCH_NAME, env.commitHash)
//    currentBuild.description = env.image_tag
//    env.serviceVersion = pom.getVersion()

    return env.imageTag
}