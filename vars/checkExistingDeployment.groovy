#!/usr/bin/env groovy


def call(def environment, def nameSpace  = 'devops') {
    if(environment == 'dev' ) {
        sh "echo '-------------- Dev Environment --------------'"
            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'np-terraform-aws']]) {
                env.s3_bucket = 'nonprod-devops-eks'
                env.AWS_ACCESS_KEY_ID = "${AWS_ACCESS_KEY_ID}"
                env.AWS_SECRET_ACCESS_KEY = "${AWS_SECRET_ACCESS_KEY}"
            }

            sh  """
                export PATH=$HOME/bin:$PATH
                aws s3 cp s3://${env.s3_bucket}/${context}/kubeconfig.yaml kubeconfig.yaml
                sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                kubectl --kubeconfig kubeconfig.yaml apply -f kubernetes/${context}/${resource}
            """
    }
    if(environment == 'qa' ) {
        script {
            try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl create -f _kube/qa/deployment.yaml --namespace=${nameSpace} --server='https://qak8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                    sh "kubectl create -f _kube/qa/service.yaml --namespace=${nameSpace} --server='https://qak8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                    sh "kubectl create -f _kube/qa/ingress.yaml --namespace=${nameSpace} --server='https://qak8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                }
            }
            catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'stg' ) {
        script {
            try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl create -f _kube/stg/deployment.yaml --namespace=stg-demo --server='https://k8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                    sh "kubectl create -f _kube/stg/service.yaml --namespace=stg-demo --server='https://k8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                    sh "kubectl create -f _kube/stg/ingress.yaml --namespace=stg-demo --server='https://k8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                }
            }
            catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'prod' ) {
        script {
            try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl create -f _kube/prod/deployment.yaml --namespace=${nameSpace} --server='https://k8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                    sh "kubectl create -f _kube/prod/service.yaml --namespace=${nameSpace} --server='https://k8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                    sh "kubectl create -f _kube/prod/ingress.yaml --namespace=${nameSpace} --server='https://k8s-master.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true "
                }
            }
            catch (exc) {sh 'echo pass'}
        }
    }
}
