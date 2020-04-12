#!/usr/bin/env groovy

def call() {
    echo "deploying Kubernetes changes"

    // def contexts = sh(script: "if test -d kubernetes; then ls kubernetes; fi", returnStdout: true).trim()
    script {
        if (env.BRANCH_NAME.contains('feature')) {
            env.contexts = 'dev-devops-01' 
        } else if (env.BRANCH_NAME.contains('dev')) {
            env.contexts = 'qa-devops-01' 
        } else if (env.BRANCH_NAME == 'stage') {
            env.contexts = 'stg-devops-01' 
        } else (env.BRANCH_NAME == 'master') {
            env.contexts = 'prod-devops-01' 
        }
    }
    
    def contexts = env.contexts
    def nameSpace = 'devops'

    echo "Found contexts:\n${contexts}"
    contexts.split('\n').each { context ->


        echo "Processing Kubernetes Context: ${context}"

        def resources = sh(script: "ls kubernetes/${context}", returnStdout: true).trim()
        echo "Found resources:\n${resources}"

        resources.split('\n').each { resource ->
            echo "Applying Kubernetes Resource ${resource} for Context ${context}"
            script {
                    if ("${context}".contains("dev")) {
                        sh "echo '-------------- Dev Environment --------------'"
                            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'np-terraform-aws']]) {
                                env.s3_bucket = 'nonprod-devops-eks'
                                env.AWS_ACCESS_KEY_ID = "${AWS_ACCESS_KEY_ID}"
                                env.AWS_SECRET_ACCESS_KEY = "${AWS_SECRET_ACCESS_KEY}"
                            }
                    }else if ("${context}".contains("qa")) {
                        sh "echo '-------------- QA Environment --------------'"
                            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'np-terraform-aws']]) {
                                env.s3_bucket = 'nonprod-devops-eks'
                                env.AWS_ACCESS_KEY_ID = "${AWS_ACCESS_KEY_ID}"
                                env.AWS_SECRET_ACCESS_KEY = "${AWS_SECRET_ACCESS_KEY}"
                            }
                    }else if ("${context}".contains("stg")){
                            sh "echo '-------------- Stage Environment --------------'"
                            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'prod-terraform-aws']]) {
                                env.s3_bucket = 'prod-devops-eks'
                                env.AWS_ACCESS_KEY_ID = "${AWS_ACCESS_KEY_ID}"
                                env.AWS_SECRET_ACCESS_KEY = "${AWS_SECRET_ACCESS_KEY}"
                            }
                    }else if ("${context}".contains("prod")){
                            sh "echo '-------------- Prod Environment --------------'"
                            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'prod-terraform-aws']]) {
                                env.s3_bucket = 'prod-devops-eks'
                                env.AWS_ACCESS_KEY_ID = "${AWS_ACCESS_KEY_ID}"
                                env.AWS_SECRET_ACCESS_KEY = "${AWS_SECRET_ACCESS_KEY}"
                            }
                    }else{
                            sh "echo '-------------- Non Prod Environment --------------'"
                            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'np-terraform-aws']]) {
                                env.s3_bucket = 'nonprod-devops-eks'
                                env.AWS_ACCESS_KEY_ID = "${AWS_ACCESS_KEY_ID}"
                                env.AWS_SECRET_ACCESS_KEY = "${AWS_SECRET_ACCESS_KEY}"
                            }
                    }
                    try {
                        sh  """
                            export PATH=$HOME/bin:$PATH
                            aws s3 cp s3://${env.s3_bucket}/${context}/kubeconfig.yaml kubeconfig.yaml
                            sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                            head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                            kubectl config --kubeconfig=kubeconfig.yaml view
                            kubectl --kubeconfig=kubeconfig.yaml delete -n ${nameSpace} statefulsets ${serviceName} --cascade=false
                            kubectl --kubeconfig=kubeconfig.yaml delete pvc -l cluster=${serviceName} -n ${nameSpace}
                            kubectl --kubeconfig=kubeconfig.yaml delete pods -l cluster=${serviceName} -n ${nameSpace} --force --grace-period=0
                            """
                    }catch (exc) {
                            sh("echo 'unable to delete/recreate resources...'")
                    }
                    try {
                        sh  """
                            export PATH=$HOME/bin:$PATH
                            aws s3 cp s3://${env.s3_bucket}/${context}/kubeconfig.yaml kubeconfig.yaml
                            sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                            head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                            kubectl --kubeconfig kubeconfig.yaml apply -f kubernetes/${context}/statefulset.yaml
                            """
                    }catch (exc) {
                            sh("echo 'unable to deploy nifi...'")
                    }
            }
        }
    }
}
