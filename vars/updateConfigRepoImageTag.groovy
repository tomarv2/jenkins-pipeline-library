#!/usr/bin/env groovy


def call(dockerRepo, imageTag, configRepo, configRepoPath) {
    withCredentials([usernamePassword(credentialsId: 'git-credentials', passwordVariable: 'jenkins_pwd', usernameVariable: 'jenkins_user')]) {
        sh "rm -rf ${configRepo}"
        sh "git clone http://jenkins:${jenkins_pwd}@${configRepoPath}"
        def contexts = sh(script: "if test -d ${configRepo}/kubernetes; then ls ${configRepo}/kubernetes; fi", returnStdout: true).trim()

        // todo: this fails if there are no changes to commit (e.g. you re-run the build) - we need to handle that use case (rather than swallowing all exceptions)
        contexts.split('\n').each { context ->
            echo "Processing Kubernetes Context: ${context}"
            script {
              def image_tag = sh(returnStdout: true, script: "echo ${env.imageTag}").trim()
              env.image_tag = "${image_tag}"
            }
            sh "sed -i -e 's|image: \\(.*\\):.*\$|image: \\1:${env.image_tag}|' ${configRepo}/kubernetes/${context}/deployment.yaml "
        }

        //loopOverRepos(deploymentRepos, dockerRepo, imageTag, configRepo)
        sh "cd ${configRepo} && git add . && git commit -m \"Automated update of image tag in config repo(s) - ${dockerRepo}:${imageTag}\" && git push http://jenkins:${jenkins_pwd}@${configRepoPath} || true"

    }
}


@NonCPS // has to be NonCPS or the build breaks on the call to .each
def loopOverRepos(deploymentRepos, dockerRepo, imageTag, configRepo) {
    echo "deploymentRepos: ${deploymentRepos}"
    deploymentRepos.each {
          script {
            sh """
              if [ -d "${configRepo}/kubernetes/${it}" ]; then
                echo "applying change to config repo: ${it}";
                sed -i -e 's|image: \\(.*\\)${dockerRepo}:.*\$|image: \\1${dockerRepo}:${imageTag}|' ${configRepo}/kubernetes/${it}/deployment.yaml ;
              else 
                echo  "${configRepo}/kubernetes/${it} does not exist!"; 
              fi
            """
          }      
      }
}
