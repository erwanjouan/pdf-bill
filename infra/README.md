# Welcome to your CDK TypeScript project

This is a blank project for CDK development with TypeScript.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

## Useful commands

* `npm run build`   compile typescript to js
* `npm run watch`   watch for changes and compile
* `npm run test`    perform the jest unit tests
* `npx cdk deploy`  deploy this stack to your default AWS account/region
* `npx cdk diff`    compare deployed stack with current state
* `npx cdk synth`   emits the synthesized CloudFormation template

## API Usage

### Get OAuth Token

```bash
curl -X POST https://<user-pool-domain>.auth.<region>.amazoncognito.com/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=<client-id>&client_secret=<client-secret>"
```

### Call PDF Bill API

```bash
curl -X POST <api-url>/pdfbill \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{"your": "payload"}'
```

## Reference

https://caesar-jd-bell.medium.com/unleash-the-power-of-aws-cdk-http-api-gateway-aws-lambda-and-sam-cli-1cf589c7e9e1

https://github.com/aws-samples/amazon-cognito-and-api-gateway-based-machine-to-machine-authorization-using-aws-cdk

https://buraktas.com/oauth-client-credentials-flow-aws-cdk/

### M2M

https://docs.aws.amazon.com/cognito/latest/developerguide/cognito-user-pools-define-resource-servers.html

https://github.com/aws-samples/amazon-cognito-and-api-gateway-based-machine-to-machine-authorization-using-aws-cdk