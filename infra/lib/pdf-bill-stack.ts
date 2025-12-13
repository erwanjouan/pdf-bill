import * as cdk from 'aws-cdk-lib';
import {Duration} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Bucket} from "aws-cdk-lib/aws-s3";
import {ManagedPolicy} from "aws-cdk-lib/aws-iam";
import {UserPool, UserPoolClient, OAuthScope} from "aws-cdk-lib/aws-cognito";
import {CognitoUserPoolsAuthorizer} from "aws-cdk-lib/aws-apigateway";

const lambda = require('aws-cdk-lib/aws-lambda');
const apigw = require('aws-cdk-lib/aws-apigateway');

export class PdfBillStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {

        super(scope, id, props);

        const myBucket = new Bucket(this, 'myBucket');

        // Cognito User Pool for OAuth2 Client Credentials
        const userPool = new UserPool(this, 'PdfBillUserPool', {
            userPoolName: 'pdf-bill-user-pool'
        });

        const userPoolClient = new UserPoolClient(this, 'PdfBillUserPoolClient', {
            userPool,
            generateSecret: true,
            oAuth: {
                flows: {
                    clientCredentials: true
                },
                scopes: [OAuthScope.custom('pdf-bill/generate')]
            }
        });

        const myLambda = new lambda.Function(this, 'myFunction', {
            code: lambda.Code.fromAsset('../target/pdf-bill-1.0-SNAPSHOT.jar'),
            runtime: lambda.Runtime.JAVA_17,
            handler: 'com.theatomicity.pdf.bill.LambdaHandler',
            timeout: Duration.minutes(15),
            memorySize: 4096,
            //snapStart: true,
            environment: {
                PDF_BILL_BUCKET: myBucket.bucketName,
                SPRING_PROFILES_ACTIVE: 'awsLambda'
            }
        })

        myLambda.role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"))

        const authorizer = new CognitoUserPoolsAuthorizer(this, 'PdfBillAuthorizer', {
            cognitoUserPools: [userPool]
        });

        const api = new apigw.RestApi(this, 'PdfBillApi', {
            restApiName: 'PdfBillApi'
        });

        const pdfBillResource = api.root.addResource('pdfbill');
        pdfBillResource.addMethod('POST', new apigw.LambdaIntegration(myLambda), {
            authorizer
        });

        new cdk.CfnOutput(this, 'myEndpointWithArgs', {
            description: 'output URL',
            value: `${endpoint.url}pdfbill`
        });

        new cdk.CfnOutput(this, 'UserPoolId', {
            description: 'Cognito User Pool ID',
            value: userPool.userPoolId
        });

        new cdk.CfnOutput(this, 'UserPoolClientId', {
            description: 'Cognito User Pool Client ID',
            value: userPoolClient.userPoolClientId
        });
    }
}
