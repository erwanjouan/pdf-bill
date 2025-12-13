import * as cdk from 'aws-cdk-lib';
import {Duration} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {OAuthScope, ResourceServerScope, UserPool, UserPoolResourceServer} from "aws-cdk-lib/aws-cognito";

export class AuthStack extends cdk.Stack {

    public userPool: UserPool;
    public scopeResourceName: string;

    constructor(scope: Construct, id: string, props?: cdk.StackProps) {

        super(scope, id, props);

        this.userPool = new UserPool(this, 'PdfBillUserPool', {
            selfSignUpEnabled: true,
            signInAliases: {
                email: true,
                username: true
            }
        });

        const apiServerScope = new ResourceServerScope({
            scopeName: "write",
            scopeDescription: "write scope",
        });

        // exposes to API
        this.scopeResourceName = apiServerScope.scopeName;

        const resourceServer = new UserPoolResourceServer(this, "ClientCredentialsResourceServer", {
            identifier: "prod",
            userPool: this.userPool,
            scopes: [apiServerScope],
        });

        this.userPool.addClient("PdfBillUserPoolClient", {
            generateSecret: true,
            enableTokenRevocation: true,
            accessTokenValidity: Duration.minutes(60),
            refreshTokenValidity: Duration.days(1),
            oAuth: {
                flows: {
                    clientCredentials: true,
                },
                scopes: [
                    //one for each scope defined above
                    OAuthScope.resourceServer(resourceServer, apiServerScope),
                ],
            },
            authFlows: {
                adminUserPassword: true,
                userPassword: true,
                userSrp: true,
                custom: true,
            },
        })

        new cdk.CfnOutput(this, 'OAuthTokenUrl', {
            description: 'OAuth Token URL',
            value: `https://${this.userPool.userPoolProviderName}.auth.${this.region}.amazoncognito.com/oauth2/token`
        });
    }
}
