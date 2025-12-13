#!/usr/bin/env node
import * as cdk from 'aws-cdk-lib';
import {LambdaStack} from "../lib/lambda-stack";

const createStack = async () => {
    try {
        const app = new cdk.App();
        const lambdaStack = new LambdaStack(app, "PdfBillLambdaStack");
        app.synth();
        return "CloudFormation stacks successfully created!";
    } catch (e) {
        return e;
    }
}

createStack()
    .then(console.log)
    .catch(console.error);