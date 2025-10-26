import {CfnOutput, Stack, StackProps} from "aws-cdk-lib";
import {
    AuthorizationType,
    CognitoUserPoolsAuthorizer,
    Cors,
    LambdaIntegration,
    MockIntegration,
    ResourceOptions,
    RestApi
} from "aws-cdk-lib/aws-apigateway";
import {IUserPool} from "aws-cdk-lib/aws-cognito";
import {Construct} from "constructs";
import {IFunction} from "aws-cdk-lib/aws-lambda/lib/function-base";
import {Role, ServicePrincipal} from "aws-cdk-lib/aws-iam";

interface ApiStackProps extends StackProps {
    lambdaFunction: IFunction;
    userPool: IUserPool;
    scopeResourceName: string;
}

export class ApiStack extends Stack {

    readonly restApiRole: Role;
    readonly cognitoAuthorizer: CognitoUserPoolsAuthorizer;

    constructor(scope: Construct, id: string, props: ApiStackProps) {
        super(scope, id, props);

        const api = new RestApi(this, "AwsCognitoTutorialRestApi", {
            deployOptions: {
                stageName: "prod",
                throttlingRateLimit: 10,
                throttlingBurstLimit: 20,
            },
        });

        //Cognito authorizor
        this.cognitoAuthorizer = new CognitoUserPoolsAuthorizer(this, "AwsCognitoTutorialRestApiAuthorizor", {
            cognitoUserPools: [props.userPool]
        });

        //CORS options
        const optionsWithCors: ResourceOptions = {
            defaultCorsPreflightOptions: {
                allowOrigins: Cors.ALL_ORIGINS,
                allowMethods: Cors.ALL_METHODS,
            },
        };

        //base resource
        const resource = api.root.addResource('bill', optionsWithCors);

        this.restApiRole = new Role(this, `GatewayIntegrationRole`, {
            roleName: `${this.stackName}-GatewayIntegrationRole`,
            assumedBy: new ServicePrincipal('apigateway.amazonaws.com'),
        });
        this.restApiRole.addManagedPolicy({managedPolicyArn: 'arn:aws:iam::aws:policy/AWSLambda_FullAccess'});

        const lambdaFunc = props.lambdaFunction
        const integration = new LambdaIntegration(lambdaFunc, {
            credentialsRole: this.restApiRole,
        });

        // Mock for testing
        resource.addMethod('GET',
            new MockIntegration({
                integrationResponses: [{
                    statusCode: "200",
                    responseTemplates: {
                        "application/json": JSON.stringify({
                            statusCode: 200,
                            message: 'Hello From Protected Resource',
                        })
                    },
                    responseParameters: {
                        'method.response.header.Content-Type': "'application/json'",
                    },
                }],
                requestTemplates: {
                    'application/json': "{ 'statusCode': 200 }",
                }
            }),
            {
                methodResponses: [{
                    statusCode: "200",
                    responseParameters: {
                        "method.response.header.Content-Type": true,
                    },
                }],
                authorizationType: AuthorizationType.COGNITO,
                authorizer: this.cognitoAuthorizer,
                authorizationScopes: [props.scopeResourceName]
            }
        );

        // Service
        resource.addMethod('POST', integration, {
            authorizationType: AuthorizationType.COGNITO,
            authorizer: this.cognitoAuthorizer,
            authorizationScopes: [props.scopeResourceName]
        });

        new CfnOutput(this, "ApiOutput", {
            value: api.url,
        });
    }
}