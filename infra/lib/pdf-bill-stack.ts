import * as cdk from 'aws-cdk-lib';
import {Duration} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Bucket} from "aws-cdk-lib/aws-s3";
import {ManagedPolicy} from "aws-cdk-lib/aws-iam";

const lambda = require('aws-cdk-lib/aws-lambda');
const apigw = require('aws-cdk-lib/aws-apigateway');

export class PdfBillStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {

        super(scope, id, props);

        const myBucket = new Bucket(this, 'myBucket');

        const myLambda = new lambda.Function(this, 'myFunction', {
            code: lambda.Code.fromAsset('../target/pdf-bill-1.0-SNAPSHOT.jar'),
            runtime: lambda.Runtime.JAVA_17,
            handler: 'com.theatomicity.pdf.bill.Handler',
            timeout: Duration.minutes(15),
            memorySize: 4096,
            snapStart: true,
            environment: {
                PDF_BILL_BUCKET: myBucket.bucketName,
                SPRING_PROFILES_ACTIVE: 'awsLambda'
            }
        })

        myLambda.role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"))

        const endpoint = new apigw.LambdaRestApi(this, 'myEndpoint', {
            handler: myLambda,
            restApiName: "PdfBillApi"
        });

        new cdk.CfnOutput(this, 'myEndpointWithArgs', {
            description: 'output URL',
            value: `${endpoint.url}pdfbill`
        });
    }
}
