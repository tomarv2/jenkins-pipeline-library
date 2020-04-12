#!/usr/bin/env groovy

def call(def environment, def nameSpace, def serviceName) {
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
                kubectl --kubeconfig kubeconfig.yaml delete --insecure-skip-tls-verify=true pods -l cluster=${serviceName} -n ${nameSpace} --force --grace-period=0
            """
    }
    if(environment == 'demo' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl delete --namespace=demo --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd}  --insecure-skip-tls-verify=true pods -l cluster=${serviceName} -n demo --force --grace-period=0"
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'qa' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl delete --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd}  --insecure-skip-tls-verify=true pods -l cluster=${serviceName} -n ${nameSpace} --force --grace-period=0"
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'stg' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl delete --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd}  --insecure-skip-tls-verify=true pods -l cluster=${serviceName} -n ${nameSpace} --force --grace-period=0"
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'prod' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl delete --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd}  --insecure-skip-tls-verify=true pods -l cluster=${serviceName} -n ${nameSpace} --force --grace-period=0"
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
}
