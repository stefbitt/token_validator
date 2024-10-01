variable "aws_region" {
  description = "AWS region where the resources will be created"
  type        = string
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "Name of the ECS cluster"
  type        = string
}

variable "cluster_arn" {
  description = "ARN of the ECS cluster"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID for ECS service and ALB"
  type        = string
}

variable "subnets" {
  description = "Subnets for ECS service and ALB"
  type        = list(string)
}

variable "task_family" {
  description = "Family name of the task definition"
  type        = string
}

variable "task_cpu" {
  description = "The number of CPU units used by the task"
  type        = string
  default     = "256"
}

variable "task_memory" {
  description = "The amount of memory (in MiB) used by the task"
  type        = string
  default     = "512"
}

variable "execution_role_arn" {
  description = "ARN of the IAM role for ECS task execution"
  type        = string
}

variable "container_name" {
  description = "Name of the container in the task"
  type        = string
}

variable "container_image" {
  description = "Docker image to use for the container"
  type        = string
}

variable "container_cpu" {
  description = "The number of CPU units used by the container"
  type        = number
  default     = 256
}

variable "container_memory" {
  description = "The amount of memory (in MiB) used by the container"
  type        = number
  default     = 512
}

variable "container_port" {
  description = "Port the container listens on"
  type        = number
}

variable "log_group_name" {
  description = "Name of the CloudWatch log group"
  type        = string
}

variable "service_name" {
  description = "Name of the ECS service"
  type        = string
}

variable "desired_count" {
  description = "The number of instances of the task definition to run"
  type        = number
  default     = 1
}
