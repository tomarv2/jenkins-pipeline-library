#!/usr/bin/env groovy

def call() {
    echo "deploying Kubernetes changes"

    def contexts = sh(script: "if test -d kubernetes; then ls kubernetes; fi", returnStdout: true).trim()

    echo "Found contexts:\n${contexts}"
    contexts.split('\n').each { context ->


        echo "Processing Kubernetes Context: ${context}"

        def resources = sh(script: "ls kubernetes/${context}", returnStdout: true).trim()
        echo "Found resources:\n${resources}"
        echo "Context:\n${context}"
        echo "Branch:\n${env.BRANCH_NAME}"

        script{
            if ("${context}".contains("dev")) {
                sh "echo '-------------- Dev Environment --------------'"
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'np-terraform-aws']]) {
                        env.s3_bucket = 'nonprod-devops-eks'
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
        }

        script {
            if (!"${context}".contains("prod")){sh "echo 'context is not prod' "}
            if ("${env.BRANCH_NAME}" == "production"){sh "echo 'branch production'"}
            if (!"${context}".contains("prod") || "${env.BRANCH_NAME}" == "production"){
                resources.split('\n').each { resource ->
                    echo "Applying Kubernetes Resource ${resource} for Context ${context}"

                            try {
                                if ("${context}".contains("aur-prod")){
                                    withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                                        sh "kubectl-v1.6 apply -f kubernetes/${context}/namespace.yaml --server='https://k8s-master1.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                                    }
                                }else{
                                    sh  """
                                        export PATH=$HOME/bin:$PATH
                                        aws s3 cp s3://${env.s3_bucket}/${context}/kubeconfig.yaml kubeconfig.yaml
                                        sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                                        head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                                        kubectl --kubeconfig kubeconfig.yaml apply -f kubernetes/${context}/namespace.yaml
                                    """
                                }
                            }catch (exc) {
                                    sh("echo 'error...'")
                            }


                            try {
                                if ("${context}".contains("aur-prod")){
                                        withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                                        sh "kubectl-v1.6 replace -f kubernetes/${context}/${resource} --server='https://k8s-master1.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                                        }
                                }else{
                                    sh  """
                                            export PATH=$HOME/bin:$PATH
                                            aws s3 cp s3://${env.s3_bucket}/${context}/kubeconfig.yaml kubeconfig.yaml
                                            sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                                            head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                                            kubectl --kubeconfig kubeconfig.yaml replace -f kubernetes/${context}/${resource}
                                    """
                                }
                            }catch (exc) {
                                if ("${context}".contains("aur-prod")){
                                    withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                                        sh "kubectl-v1.6 apply -f kubernetes/${context}/${resource} --server='https://k8s-master1.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                                    }
                                }else{
                                    sh  """
                                            export PATH=$HOME/bin:$PATH
                                            aws s3 cp s3://${env.s3_bucket}/${context}/kubeconfig.yaml kubeconfig.yaml
                                            sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                                            head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                                            kubectl --kubeconfig kubeconfig.yaml apply -f kubernetes/${context}/${resource}
                                    """
                                }
                         }
                         
                         
                    }
            }
        }

        script{
            if (!"${context}".contains("prod") || "${env.BRANCH_NAME}" == "production"){
                deploymentHealthCheckContext("${context}","${env.s3_bucket}")
            }
        }
           
    }
}
