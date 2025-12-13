PROJECT_NAME:=pdf-bill

run:
	mvn exec:java
init:
	npm install -g aws-cdk && \
	mkdir infra && cd infra && \
	cdk init app --language=typescript && \
	npm install aws-cdk-lib && \
	npm install @types/aws-lambda @types/node

deploy:
	cd infra && cdk deploy --all \
		--require-approval never

destroy:
	cd infra && cdk destroy --force

prerequisites:
	aws cloudformation deploy \
			--stack-name $(PROJECT_NAME)-pre-requisites \
			--capabilities CAPABILITY_NAMED_IAM \
			--parameter-overrides \
            				ProjectName=$(PROJECT_NAME) \
			--template-file ./infra/pre-requisites/github-action-role.yml