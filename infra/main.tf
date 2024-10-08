resource "aws_vpc" "vpc_private" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "subnet_private_01" {
  vpc_id     = aws_vpc.vpc_private.id
  cidr_block = "10.0.16.0/20"
}

resource "aws_subnet" "subnet_private_02" {
  vpc_id     = aws_vpc.vpc_private.id
  cidr_block = "10.0.32.0/20"
}

resource "aws_subnet" "subnet_private_03" {
  vpc_id     = aws_vpc.vpc_private.id
  cidr_block = "10.0.48.0/20"
}

resource "awscc_ecr_repository" "lifecycle_policy_example" {
  repository_name      = "ecr-token-validator"
  image_tag_mutability = "MUTABLE"

  lifecycle_policy = {
    lifecycle_policy_text = <<EOF
        {
            "rules": [
                {
                    "rulePriority": 1,
                    "description": "Expire images older than 1 day",
                    "selection": {
                        "tagStatus": "untagged",
                        "countType": "sinceImagePushed",
                        "countUnit": "days",
                        "countNumber": 1
                    },
                    "action": {
                        "type": "expire"
                    }
                }
            ]
        }
        EOF
  }
}

resource "aws_ecs_cluster" "cluster_valida_token" {
  name = var.cluster_name

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

resource "aws_security_group" "alb_sg" {
  name        = "${var.service_name}-alb-sg"
  description = "Allow HTTP and HTTPS traffic to ALB"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_sg" {
  name        = "${var.service_name}-ecs-sg"
  description = "Allow HTTP traffic to ECS tasks"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = var.container_port
    to_port         = var.container_port
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb" "alb" {
  name               = "${var.service_name}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = [aws_subnet.subnet_private_01, aws_subnet.subnet_private_02, aws_subnet.subnet_private_03]

  enable_deletion_protection = false
}

resource "aws_lb_target_group" "ecs_tg" {
  name        = "${var.service_name}-tg"
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    path                = "/actuator"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
    matcher             = "200-299"
  }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.ecs_tg.arn
  }
}

resource "aws_ecs_task_definition" "task_definition" {
  family                   = var.task_family
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.task_cpu
  memory                   = var.task_memory
  execution_role_arn       = var.execution_role_arn

  container_definitions = jsonencode([
    {
      name         = var.container_name
      image        = var.container_image
      cpu          = var.container_cpu
      memory       = var.container_memory
      essential    = true
      portMappings = [
        {
          containerPort = var.container_port
          hostPort      = var.container_port
          protocol      = "tcp"
        }
      ]
      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "aws"
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options   = {
          awslogs-group         = var.log_group_name
          awslogs-region        = var.aws_region
          awslogs-stream-prefix = var.container_name
        }
      }
    }
  ])
}

resource "aws_cloudwatch_log_group" "ecs_log_group" {
  name              = var.log_group_name
  retention_in_days = 7
}

resource "aws_ecs_service" "ecs_service" {
  name            = var.service_name
  cluster         = aws_ecs_cluster.cluster_valida_token.arn
  task_definition = aws_ecs_task_definition.task_definition.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.subnets
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.ecs_tg.arn
    container_name   = var.container_name
    container_port   = var.container_port
  }

  depends_on = [aws_lb_listener.http, aws_ecs_cluster.cluster_valida_token]
}
