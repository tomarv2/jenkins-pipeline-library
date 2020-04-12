#!/usr/bin/env groovy

def call(def nameSpace, def serviceName) {
    echo "deploying Kubernetes changes"

    def contexts = sh(script: "if test -d kubernetes; then ls kubernetes; fi", returnStdout: true).trim()

    echo "Found contexts:\n${contexts}"
    contexts.split('\n').each { context ->


        echo "Processing Kubernetes Context: ${context}"

        def resources = sh(script: "ls kubernetes/${context}", returnStdout: true).trim()
        echo "Found resources:\n${resources}"

        resources.split('\n').each { resource ->
            echo "Applying Kubernetes Resource ${resource} for Context ${context}"
            script {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'np-terraform-aws']]) {
                    sh  """
                        export PATH=$HOME/bin:$PATH

                        aws s3 cp s3://nonprod-devops-eks/${context}/kubeconfig.yaml kubeconfig.yaml
                        sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                        head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                        cat kubeconfig.yaml
                        kubectl config --kubeconfig=kubeconfig.yaml view
                        kubectl --kubeconfig=kubeconfig.yaml delete -n ${nameSpace} statefulsets ${serviceName} --cascade=false
                        kubectl --kubeconfig=kubeconfig.yaml delete pvc -l cluster=${serviceName} -n ${nameSpace}
                        kubectl --kubeconfig=kubeconfig.yaml delete pods -l cluster=${serviceName} -n ${nameSpace} --force --grace-period=0
                    """
                }
            }
        }
    }
}
