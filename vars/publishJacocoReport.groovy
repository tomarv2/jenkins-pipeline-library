#!/usr/bin/env groovy

def call() {
    step([$class: 'JacocoPublisher'])
}
