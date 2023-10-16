# Pre-requisites

- localstack pro(I'm using aws command instead of awslocal command because of an error with it)
  - I'm using localstack pro with student license for free. FYI.

## Setup

### Compile and package

```bash
cd spark && . ./enter-container.sh
cd kafka-spark-streaming && mvn clean package
```

### Start localstack and EMR

```bash
dc up -d

# Prepare S3 bucket for EMR
export S3_BUCKET=test
aws s3 mb s3://$S3_BUCKET --endpoint-url=http://localhost:4566
aws s3 cp spark/project/kafka-spark-streaming/target/kafka-spark-streaming-1.0-SNAPSHOT.jar s3://${S3_BUCKET}/code/java-spark/ --endpoint-url=http://localhost:4566

# Role for EMR
export JOB_ROLE_ARN=arn:aws:iam::000000000000:role/emr-serverless-job-role

# Create EMR cluster
aws emr-serverless create-application \
    --endpoint-url=http://localhost:4566 \
    --type SPARK \
    --name serverless-java-demo \
    --release-label "emr-6.9.0" \
    --initial-capacity '{
        "DRIVER": {
            "workerCount": 1,
            "workerConfiguration": {
                "cpu": "4vCPU",
                "memory": "16GB"
            }
        },
        "EXECUTOR": {
            "workerCount": 3,
            "workerConfiguration": {
                "cpu": "4vCPU",
                "memory": "16GB"
            }
        }
    }'
export APPLICATION_ID='<application-id>'  # Replace with the application id from the output of the previous command

aws emr-serverless start-application \
    --endpoint-url=http://localhost:4566 \
    --application-id $APPLICATION_ID

awslocal emr-serverless start-job-run \
    --application-id $APPLICATION_ID \
    --execution-role-arn $JOB_ROLE_ARN \
    --job-driver '{
        "sparkSubmit": {
            "entryPoint": "s3://'${S3_BUCKET}'/code/java-spark/java-demo-1.0.jar",
            "sparkSubmitParameters": "--class HelloWorld"
        }
    }' \
    --configuration-overrides '{
        "monitoringConfiguration": {
            "s3MonitoringConfiguration": {
                "logUri": "s3://'${S3_BUCKET}'/logs/"
            }
        }
    }'
```
