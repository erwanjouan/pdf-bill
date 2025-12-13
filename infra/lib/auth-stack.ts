import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {UserPool} from "aws-cdk-lib/aws-cognito";

export class AuthStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {

        super(scope, id, props);

        const userPool = new UserPool(this, 'PdfBillUserPool', {
            selfSignUpEnabled: true,
            signInAliases: {
                email: true,
                username: true
            }
        });

        new cdk.CfnOutput(this, 'OAuthTokenUrl', {
            description: 'OAuth Token URL',
            value: `https://${userPool.userPoolProviderName}.auth.${this.region}.amazoncognito.com/oauth2/token`
        });
    }
}
