#!/usr/bin/env groovy

def call(body) {

    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    sh "sudo docker tag ${env.serviceName} dcr.tomarv2.com/${env.dockerPath}:${env.image_tag}"
    sh "sudo docker push dcr.tomarv2.com/${env.dockerPath}:${env.image_tag}"
}