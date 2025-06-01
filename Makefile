run:
	mvn exec:java
init:
	npm install -g aws-cdk && \
	mkdir infra && cd infra && \
	cdk init app --language=typescript && \
	npm install aws-cdk-lib && \
	npm install @types/aws-lambda @types/node

deploy:
	cd infra && cdk deploy \
		--require-approval never

destroy:
	cd infra && cdk destroy --force