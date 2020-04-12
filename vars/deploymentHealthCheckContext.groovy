#!/usr/bin/env groovy

def call(def context, def s3_bucket) {
        sh "echo '-------------- Checking deployment status... --------------'"
        if(fileExists("${env.WORKSPACE}/kubernetes/$context/deployment.yaml")){
            sh "echo Exist!"
            if ("$context".contains("aur-prod")){
                withCredentials([usernamePassword(credentialsId: 'k8s_cluster_pwd_prod', passwordVariable: 'k8s_pwd', usernameVariable: 'k8s_user')]) {
                    sh """
                        retry=12
                        ready=-1

                        while ((retry>0 && ready!=0))
                        do
                        kubectl  rollout status  -f kubernetes/$context/deployment.yaml --server='https://k8s-master1.demo.com' --username=${k8s_user} --password=${k8s_pwd} --insecure-skip-tls-verify=true; if((\$?==0)); then ready=0; continue; fi;
                        ((retry-=1))
                        sleep 10
                        done
                        if ((ready!=0)); then echo 'deployment failed'; exit 1; fi;"""
                }
            }else{
                sh """
                        export PATH=$HOME/bin:$PATH
                        aws s3 cp s3://$s3_bucket/$context/kubeconfig.yaml kubeconfig.yaml
                        sed '/^\$/d' kubeconfig.yaml > kubeconfig.yaml.tmp
                        head -n -3 kubeconfig.yaml.tmp > kubeconfig.yaml
                        retry=12
                        ready=-1

                        while ((retry>0 && ready!=0))
                        do
                        kubectl --kubeconfig kubeconfig.yaml rollout status  -f kubernetes/$context/deployment.yaml; if((\$?==0)); then ready=0; continue; fi;
                        ((retry-=1))
                        sleep 10
                        done
                        if ((ready!=0)); then echo 'deployment failed'; exit 1; fi;
                    """
            }
        }

        // sh  """
        //     retry=12
        //     ready=-1

        //     while ((retry>0 && ready!=0))
        //     do
        //     kubectl --kubeconfig kubeconfig.yaml rollout status deployment $DeploymentName -n $NameSpace; if((\$?==0)); then ready=0; continue; fi;
        //     ((retry-=1))
        //     sleep 10
        //     done
        //     if ((ready!=0)); then echo 'deployment failed'; exit 1; fi;
        // """
}
