#!/usr/bin/env groovy


def call(def environment, def serviceName, def dockerPath, def imageTag, def nameSpace = 'demo' ) {
    if(environment == 'dev' ) {
        script {
            try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl set image deployment/${env.serviceName} --namespace=${nameSpace} --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true ${env.serviceName}=dockerregistry.ecr.us-west-2.amazonaws.com/${env.dockerPath}:${env.imageTag} "
                }
            }
            catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'qa' ) {
        script {
            try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl set image deployment/${env.serviceName} --namespace=${nameSpace} --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true ${env.serviceName}=dockerregistry.ecr.us-west-2.amazonaws.com/${env.dockerPath}:${env.imageTag} "
                }
            }
            catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'stg' ) {
        script {
            try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl set image deployment/${env.serviceName} --namespace=${nameSpace} --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true ${env.serviceName}=dockerregistry.ecr.us-west-2.amazonaws.com/${env.dockerPath}:${env.imageTag} "
                }
            }
            catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'prod' ) {
        script {
            try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl set image deployment/${env.serviceName} --namespace=${nameSpace} --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true ${env.serviceName}=dockerregistry.ecr.us-west-2.amazonaws.com/${env.dockerPath}:${env.imageTag} "
                }
            }
            catch (exc) {sh 'echo pass'}
        }
    }
}


        stage('QA: Deploy') {
            when {
                anyOf {
                    branch "development"
                    branch "dev"
                    branch "devops"
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl set image deployment/${env.serviceName} --namespace=demo --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true ${env.serviceName}=dockerregistry.ecr.us-west-2.amazonaws.com/${env.dockerPath}:${env.imageTag} "
                }
            }
        }