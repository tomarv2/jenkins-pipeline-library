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

Collection of jenkins stages which can be glued together to setup mix and match pipelines.

**Medium post:** https://medium.com/tomarv2/jenkins-shared-libraries-ab64f7acac68

***
<p align="center">
  <img width="900" height="500" src="https://files.gitter.im/tomarv2/oy6L/Screen-Shot-2020-04-09-at-9.08.16-PM.png">
</p>


**Getting Started With Shared Library**

In our case shared library consists of groovy and shell scripts. 

You can clone this repo to get the basic structure of the shared library.

This repo contains list of all the files that we have are using to build and deploy our CICD.

There is a plan to split `Build` and `Deploy` and use **Jenkins** for building and **Spinnaker** for deploying.

**Structure of the repo**

 - **sample-jenkinsfile:**
    - Build -> artifact-repo (we are covering two use cases: `gradle` and `maven` projects, we are working on adding NiFi as we deploy lot of NiFi clusters)
    - Deploy -> config-repo (deploy k8s, Terraform related files)
 - **vars:**
    - List of all `sh` and `groovy` scripts
    
 In case you want to use the repo on full or part of the the groovy script feel free to use them.
 
 I cannot take all credit for the files in here, I got help from my teammates.
 
 
 
 
 





