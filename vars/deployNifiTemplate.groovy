#!/usr/bin/env groovy


def call(def evironment, def nifiUrl, def serviceName) {
    sh "sleep 210"
    sh "wget http://tomarv2.bitbucket.com/projects/DOPS/repos/nifi/raw/nifi_setup_1.7_stateful/build/deploy/controller_services.py"
    sh "wget http://tomarv2.bitbucket.com/projects/DOPS/repos/nifi/raw/nifi_setup_1.7_stateful/build/deploy/deploy_nifi.py"
    sh "wget http://tomarv2.bitbucket.com/projects/DOPS/repos/nifi/raw/nifi_setup_1.7_stateful/build/deploy/DeployTemplateBase.py"
    sh "wget http://tomarv2.bitbucket.com/projects/DOPS/repos/nifi/raw/nifi_setup_1.7_stateful/build/deploy/parse_yaml.py"
    sh "wget http://tomarv2.bitbucket.com/projects/DOPS/repos/nifi/raw/nifi_setup_1.7_stateful/build/deploy/setup_connection.py"
    //sh "wget http://tomarv2.bitbucket.com/projects/DOPS/repos/nifi/raw/nifi_setup_1.7_stateful/build/deploy/requirements.txt"
    sh "curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py; sudo python get-pip.py"
    sh "sudo pip install gitpython"
    sh "sudo yum install -y epel-release jq"
    //sh "sudo pip install -r requirements.txt"
    sh "python deploy_nifi.py ${nifiUrl} ${serviceName} ${WORKSPACE}"
}
