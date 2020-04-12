#!/usr/bin/env groovy

def call() {

    def changedFiles = sh(script: "git diff-tree --no-commit-id --name-only -r HEAD", returnStdout: true).trim()
    echo "changed files: ${env.changedFiles}"

    env.changedFiles = changedFiles
    env.hasSpinnakerChanges = changedFiles ==~ /(?sm).*^_spinnaker\/.*/
    env.hasKubernetesChanges = changedFiles ==~ /(?sm).*^_kube\/.*/
    env.hasCodeChanges = changedFiles ==~ /(?sm).*^(?!_kube\/|_spinnaker\/).*/

    echo "hasSpinnakerChanges: ${env.hasSpinnakerChanges}"
    echo "hasKubernetesChanges: ${env.hasKubernetesChanges}"
    echo "hasCodeChanges: ${env.hasCodeChanges}"
}
