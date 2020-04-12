#!/usr/bin/env groovy

def call(def policyToEvaluate) {

    sh "/opa eval --data ${policyToEvaluate} --input 'terraform.json' \"data.terraform.analysis.authz\""

}


