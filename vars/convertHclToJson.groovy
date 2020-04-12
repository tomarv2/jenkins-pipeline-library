#!/usr/bin/env groovy

def call() {
        sh "tfjson2 --plan terraform_plan > terraform.json; cat terraform.json"

}
