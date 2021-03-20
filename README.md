<p align="center">
    <a href="https://www.apache.org/licenses/LICENSE-2.0" alt="license">
        <img src="https://img.shields.io/github/license/tomarv2/jenkins-pipeline-library" /></a>
    <a href="https://github.com/tomarv2/jenkins-pipeline-library/tags" alt="GitHub tag">
        <img src="https://img.shields.io/github/v/tag/tomarv2/jenkins-pipeline-library" /></a>
    <a href="https://stackoverflow.com/users/6679867/tomarv2" alt="Stack Exchange reputation">
        <img src="https://img.shields.io/stackexchange/stackoverflow/r/6679867"></a>
    <a href="https://discord.gg/XH975bzN" alt="chat on Discord">
        <img src="https://img.shields.io/discord/813961944443912223?logo=discord"></a>
    <a href="https://twitter.com/intent/follow?screen_name=varuntomar2019" alt="follow on Twitter">
        <img src="https://img.shields.io/twitter/follow/varuntomar2019?style=social&logo=twitter"></a>
</p>

## Jenkins Shared Pipeline Library

Collection of jenkins stages which can be glued together to setup complex pipelines.

### Medium post: [link](https://medium.com/tomarv2/jenkins-shared-libraries-ab64f7acac68)

<p align="center">
  <img width="900" height="500" src="https://files.gitter.im/tomarv2/oy6L/Screen-Shot-2020-04-09-at-9.08.16-PM.png">
</p>

:wave: Most of the groovy scripts here are written for running on Kubernetes environment running on AWS.

### Getting Started With Shared Library

This library consists of `groovy` and `shell` scripts. 

Clone this repo to get the basic structure of the shared library.


### Structure of the repo:

 - ##### jenkinsfile directory:
    - `Build` -> artifact-repo (covering two use cases: `gradle` and `maven` projects)
    - `Deploy` -> config-repo (manage k8s, manage Terraform)
 
- ##### vars directory:
    - List of `sh` and `groovy` scripts. Scripts are to build:
         
       - Manage `docker` images
       - Manage `Kubernetes` (Statefulset, Deployments, PVCs)
       - Basic `Terraform` validation
       - Run `Sonarqube`
       - Manage `Git`(commits, approvals)
       - Small components for `OPA(Open Policy Agent)`, `Spinnaker`
       - Manage `NiFi`
       - Artifact management
   
### Note :

:information_source: There is a plan to split `Build` and `Deploy` and use **Jenkins** for building and **Spinnaker** for deploying.

 
 





