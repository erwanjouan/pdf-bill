import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {IUserPoolResourceServer, OAuthScope, ResourceServerScope, UserPool} from "aws-cdk-lib/aws-cognito";

export class AuthStack extends cdk.Stack {

    public userPool: UserPool;
    public scopeResourceName: string;
    private projectPrefix: string = 'pdf-bill';

    constructor(scope: Construct, id: string, props?: cdk.StackProps) {

        super(scope, id, props);

        this.userPool = new UserPool(this, 'PdfBillUserPool', {
            selfSignUpEnabled: false,
            removalPolicy: cdk.RemovalPolicy.DESTROY
        });

        let domain = `${this.projectPrefix}-domain`;
        this.userPool.addDomain(domain, {
            cognitoDomain: {
                domainPrefix: domain,
            },
        });

        const apiServerScope = new ResourceServerScope({
            scopeName: "write",
            scopeDescription: "write scope",
        });

        // exposes to other stacks
        this.scopeResourceName = `${this.projectPrefix}/${apiServerScope.scopeName}`;

        let userPoolResourceServer: IUserPoolResourceServer = this.userPool.addResourceServer('ResourceServer', {
            userPoolResourceServerName: `${this.projectPrefix}`,
            identifier: `${this.projectPrefix}`,
            scopes: [apiServerScope]
        });

        this.userPool.addClient("PdfBillUserPoolClient", {
            idTokenValidity: cdk.Duration.hours(1),
            accessTokenValidity: cdk.Duration.hours(1),
            authFlows: {
                userPassword: false,
                userSrp: false,
                custom: false,
            },
            oAuth: {
                flows: {
                    authorizationCodeGrant: false,
                    implicitCodeGrant: false,
                    clientCredentials: true
                },
                scopes: [OAuthScope.resourceServer(userPoolResourceServer, apiServerScope)]
            },
            generateSecret: true,
            preventUserExistenceErrors: false
        });

        new cdk.CfnOutput(this, 'OAuthTokenUrl', {
            description: 'OAuth Token URL',
            value: `https://${domain}.auth.${this.region}.amazoncognito.com/oauth2/token`
        });
    }
}
