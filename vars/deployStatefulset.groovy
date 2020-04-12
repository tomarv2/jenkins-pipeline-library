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
                kubectl --kubeconfig kubeconfig.yaml apply --insecure-skip-tls-verify=true -f kubenetes/${context}/statefulset.yaml"
                sh("eval \$(kubectl describe svc ${serviceName} --namespace=${nameSpace} --kubeconfig kubeconfig.yaml --insecure-skip-tls-verify=true |grep -i nodeport |grep -i web |grep -v hook |awk {'print \$3'}|cut -f1 -d '/' > NodePort)")
                script {
                    env.NODEPORT = readFile('NodePort')
                    sh "cat NodePort"
                }
            """
    }
    if(environment == 'demo' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl create --namespace=demo --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true -f _kube/demo/statefulset.yaml"
                    sh("eval \$(kubectl describe svc ${serviceName} --namespace=demo --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true |grep -i nodeport |grep -i web |grep -v hook |awk {'print \$3'}|cut -f1 -d '/' > NodePort)")
                    script {
                        env.NODEPORT = readFile('NodePort')
                        sh "cat NodePort"
                    }
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'qa' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_qa', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "kubectl create --namespace=${nameSpace} --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true -f _kube/qa/statefulset.yaml"
                    sh("eval \$(kubectl describe svc ${serviceName} --namespace=${nameSpace} --server='https://qak8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true |grep -i nodeport |grep -i web |grep -v hook |awk {'print \$3'}|cut -f1 -d '/' > NodePort)")
                    script {
                        env.NODEPORT = readFile('NodePort')
                        sh "cat NodePort"
                    }
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'stg' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "cat _kube/stg/statefulset.yaml"
                    sh "kubectl create --namespace=${nameSpace} --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true -f _kube/stg/statefulset.yaml"
                    sh("eval \$(kubectl describe svc ${serviceName} --namespace=${nameSpace} --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true |grep -i nodeport |grep -i web |grep -v hook |awk {'print \$3'}|cut -f1 -d '/' > NodePort)")
                    script {
                        env.NODEPORT = readFile('NodePort')
                        sh "cat NodePort"
                    }
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
    if(environment == 'prod' ) {
        script {
         //   try {
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_aws_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh "cat _kube/prod/statefulset.yaml"
                    sh "kubectl create --namespace=${nameSpace} --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true -f _kube/prod/statefulset.yaml"
                    sh("eval \$(kubectl describe svc ${serviceName} --namespace=${nameSpace} --server='https://k8s-master.tomarv2.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true |grep -i nodeport |grep -i web |grep -v hook |awk {'print \$3'}|cut -f1 -d '/' > NodePort)")
                    script {
                        env.NODEPORT = readFile('NodePort')
                        sh "cat NodePort"
                    }
                }
         //   }
         //   catch (exc) {sh 'echo pass'}
        }
    }
}
