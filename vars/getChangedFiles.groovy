#!/usr/bin/env groovy

def call() {
    return sh(script: "git diff-tree --no-commit-id --name-only -r HEAD", returnStdout: true).trim()
}