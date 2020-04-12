#!/usr/bin/env groovy

def call(def env, def pipelineParams, def currentBuild) {
    pom = readMavenPom()

    env.commitHash = getGitCommitHash()
    env.serviceName = pom.getArtifactId()
    env.namespace = pipelineParams.namespace != null ? pipelineParams.namespace : "services"
    env.dockerPath = "demo/${env.namespace}/${pom.getArtifactId()}"
    env.serviceVersion = pom.getVersion()
    env.buildChangelog = getChangeDescription(currentBuild)
    env.imageTag = buildDockerTag(env.BRANCH_NAME, env.commitHash)

    echo env.buildChangelog

//    env.scmUrl = sh(returnStdout: true, script: 'git config remote.origin.url').trim()
//    echo "${env.scmUrl}"
    return env.imageTag
}