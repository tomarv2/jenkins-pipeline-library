#!/usr/bin/env groovy
def call(serviceName, dockerRepo, imageTag) {


    // this is a hack -- repos should be created when our internal Git repo is created
    try {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'spinnaker_access_id']]) {
            sh "aws ecr create-repository --repository-name ${dockerRepo}" }
    } catch (exc) {
        sh 'echo "repository already exists..."'
    }

    // aws ecr registry


    sh "docker tag ${serviceName} dockerregistry.ecr.us-west-2.amazonaws.com/${dockerRepo}:${imageTag}"
    
    try {
      sh("eval \$(aws ecr get-login --no-include-email)")	
  	} catch (exc) {
      sh("eval \$(aws ecr get-login --include-email | sed 's|https://||')")
  	}
    sh "docker push dockerregistry.ecr.us-west-2.amazonaws.com/${dockerRepo}:${imageTag}"
}
