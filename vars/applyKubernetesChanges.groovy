#!/usr/bin/env groovy

def call() {
    echo "deploying Kubernetes changes"

    def contexts = sh(script: "if test -d kubernetes; then ls kubernetes; fi", returnStdout: true).trim()

    echo "Found contexts:\n${contexts}"
    contexts.split('\n').each { context ->


        echo "Processing Kubernetes Context: ${context}"

        def resources = sh(script: "ls kubernetes/${context}", returnStdout: true).trim()
        echo "Found resources:\n${resources}"

        resources.split('\n').each { resource ->
            echo "Applying Kubernetes Resource ${resource} for Context ${context}"
            sh "kubectl --kubeconfig /home/jenkins/.kubeconfig-cicd --context ${context}_context apply -f kubernetes/${context}/${resource}"
        }
    }

}