#!/usr/bin/env node
import * as cdk from 'aws-cdk-lib';
import {LambdaStack} from "../lib/lambda-stack";
import {AuthStack} from "../lib/auth-stack";
import {ApiStack} from "../lib/api-stack";

const createStack = async () => {
    try {
        const app = new cdk.App();
        const lambdaStack = new LambdaStack(app, "pdf-bill-lambda-stack");
        const authStack = new AuthStack(app, "pdf-bill-auth-stack");
        new ApiStack(app, "pdf-bill-api-gw-stack", {
            lambdaFunction: lambdaStack.lambdaFunction,
            userPool: authStack.userPool,
            scopeResourceName: authStack.scopeResourceName
        });
        app.synth();
        return "CloudFormation stacks successfully created!";
    } catch (e) {
        return e;
    }
}

createStack()
    .then(console.log)
    .catch(console.error);