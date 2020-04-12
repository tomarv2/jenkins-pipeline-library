#!/usr/bin/env groovy

// Howto: loadNifiPlugins(env.devNifiUrl) for dev environment. same way:qaNifiUrl,stgNifiUrl,prodNifiUrl, for respectivelly: qa, stage, and prod 
// TODO: get script from master branch
def call(def nifiUrl) {
    sh "sleep 30"
    sh "sudo pip install gitpython"
    sh "sudo yum install -y epel-release jq"
    sh "python setup_custom_reporting_task.py  ${nifiUrl}"
}
