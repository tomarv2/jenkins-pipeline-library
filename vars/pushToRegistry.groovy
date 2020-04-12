#!/usr/bin/env groovy

def call(body) {

    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    sh "sudo docker tag ${env.serviceName} dcr.demo.com/${env.dockerPath}:${env.image_tag}"
    sh "sudo docker push dcr.demo.com/${env.dockerPath}:${env.image_tag}"
}