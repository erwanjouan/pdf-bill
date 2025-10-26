import * as cdk from 'aws-cdk-lib';
import {Duration} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Bucket} from "aws-cdk-lib/aws-s3";
import {ManagedPolicy} from "aws-cdk-lib/aws-iam";
import {IFunction} from "aws-cdk-lib/aws-lambda/lib/function-base";

const lambda = require('aws-cdk-lib/aws-lambda');

export class LambdaStack extends cdk.Stack {

    public readonly lambdaFunction: IFunction;

    constructor(scope: Construct, id: string, props?: cdk.StackProps) {

        super(scope, id, props);

        const myBucket = new Bucket(this, 'myBucket', {
            removalPolicy: cdk.RemovalPolicy.DESTROY,
            autoDeleteObjects: true
        });

        this.lambdaFunction = new lambda.Function(this, 'myFunction', {
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

        this.lambdaFunction?.role?.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"))
    }
}
