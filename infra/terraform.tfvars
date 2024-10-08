aws_region         = "us-east-1"
cluster_name       = "cluster-ecs"
task_family        = "token-validator"
execution_role_arn = "arn:aws:iam::471112644854:role/ecsTaskExecutionRole"
container_name     = "token-validator"
container_image    = "471112644854.dkr.ecr.us-east-1.amazonaws.com/ecr-token-validator:latest"
container_port     = 8080
log_group_name     = "/ecs/service-token-validator"
service_name       = "token-validator"
