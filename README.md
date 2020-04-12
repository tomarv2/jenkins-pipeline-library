## Jenkins Shared Pipeline Libraries

**Medium post:** https://medium.com/@tomarv2/jenkins-shared-libraries-ab64f7acac68

***

![Image description](https://files.gitter.im/tomarv2/oy6L/Screen-Shot-2020-04-09-at-9.08.16-PM.png)

***

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
 
 
 
 
 





