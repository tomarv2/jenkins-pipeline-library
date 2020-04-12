#!/usr/bin/env groovy

def call(def branchName, def commitHash) {
    return "rev-" + cleanBranchName(branchName) + "-${commitHash}"
}


def static cleanBranchName(String branchName) {
    return branchName.replaceAll("/", "-")
}