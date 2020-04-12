#!/usr/bin/env groovy

def call() {
    def props = readProperties file: 'gradle.properties'

    def publishedName = sh(
            script: "./gradlew properties -q | grep name: | awk '{print \$2}'", returnStdout: true
    ).trim()

    def publishedVersion = sh(
            script: "./gradlew properties -q | grep version: | awk '{print \$2}'", returnStdout: true
    ).trim()

    return [props.group, publishedName, publishedVersion]
}
