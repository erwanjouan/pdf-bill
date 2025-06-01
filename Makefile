run_local:
	mvn clean package && mvn exec:java

deploy:
	cd infra && cdk deploy \
		--require-approval never

destroy:
	cd infra && cdk destroy --force

init:
	npm install -g aws-cdk && \
	mkdir infra && cd infra && \
	cdk init app --language=typescript && \
	npm install aws-cdk-lib && \
	npm install @types/aws-lambda @types/node
