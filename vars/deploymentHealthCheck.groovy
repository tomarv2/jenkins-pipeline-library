#!/usr/bin/env groovy

def call(def DeploymentName, def NameSpace) {
        sh "echo '-------------- Checking deployment status... --------------'"
        sh  """
            retry=12
            ready=-1

            while ((retry>0 && ready!=0))
            do
            kubectl rollout status deployment $DeploymentName -n $NameSpace; if((\$?==0)); then ready=0; continue; fi;
            ((retry-=1))
            sleep 10
            done
            if ((ready!=0)); then echo 'deployment failed'; exit 1; fi;
        """
}
