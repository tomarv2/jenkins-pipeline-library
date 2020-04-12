#!/usr/bin/env groovy

def call(def envOrBranch) {

    String branch

    // for backward compatibility
    if(envOrBranch instanceof String) {
        branch = envOrBranch;
    } else {
        branch = envOrBranch.BRANCH_NAME
    }

    def commitHash = getGitCommitHash()
    return "rev-" + cleanBranchName(branch) + "-${commitHash}"
}


def static cleanBranchName(String branchName) {
    return branchName.replaceAll("/", "-")
}