resource "aws_apigatewayv2_api" "valida-token-gateway" {
  name          = "valida-token-gateway"
  protocol_type = "HTTP"
}

resource "aws_eip" "nat_eip_1" {
}

resource "aws_eip" "nat_eip_2" {
}

resource "aws_eip" "nat_eip_3" {
}

resource "aws_vpc" "vpc_private" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "public_subnet_1" {
  vpc_id            = aws_vpc.vpc_private.id
  cidr_block        = "10.0.0.0/24"
  availability_zone = "us-east-1a"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "public_subnet_2" {
  vpc_id            = aws_vpc.vpc_private.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "us-east-1b"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "public_subnet_3" {
  vpc_id            = aws_vpc.vpc_private.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "us-east-1c"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "private_subnet_1" {
  vpc_id            = aws_vpc.vpc_private.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a"
}

resource "aws_subnet" "private_subnet_2" {
  vpc_id            = aws_vpc.vpc_private.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "us-east-1b"
}

resource "aws_subnet" "private_subnet_3" {
  vpc_id            = aws_vpc.vpc_private.id
  cidr_block        = "10.0.5.0/24"
  availability_zone = "us-east-1c"
}

resource "aws_nat_gateway" "nat_gateway_1" {
  allocation_id = aws_eip.nat_eip_1.id
  subnet_id     = aws_subnet.public_subnet_1.id
}

resource "aws_nat_gateway" "nat_gateway_2" {
  allocation_id = aws_eip.nat_eip_2.id
  subnet_id     = aws_subnet.public_subnet_2.id
}

resource "aws_nat_gateway" "nat_gateway_3" {
  allocation_id = aws_eip.nat_eip_3.id
  subnet_id     = aws_subnet.public_subnet_3.id
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc_private.id
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.vpc_private.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
}

resource "aws_route_table" "private_route_table_1" {
  vpc_id = aws_vpc.vpc_private.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gateway_1.id
  }
}

resource "aws_route_table" "private_route_table_2" {
  vpc_id = aws_vpc.vpc_private.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gateway_2.id
  }
}

resource "aws_route_table" "private_route_table_3" {
  vpc_id = aws_vpc.vpc_private.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gateway_3.id
  }
}

resource "aws_route_table_association" "private_subnet_1_assoc" {
  subnet_id      = aws_subnet.private_subnet_1.id
  route_table_id = aws_route_table.private_route_table_1.id
}

resource "aws_route_table_association" "private_subnet_2_assoc" {
  subnet_id      = aws_subnet.private_subnet_2.id
  route_table_id = aws_route_table.private_route_table_2.id
}

resource "aws_route_table_association" "private_subnet_3_assoc" {
  subnet_id      = aws_subnet.private_subnet_3.id
  route_table_id = aws_route_table.private_route_table_3.id
}

resource "aws_route_table_association" "public_subnet_assoc_1" {
  subnet_id      = aws_subnet.public_subnet_1.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "public_subnet_assoc_2" {
  subnet_id      = aws_subnet.public_subnet_2.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "public_subnet_assoc_3" {
  subnet_id      = aws_subnet.public_subnet_3.id
  route_table_id = aws_route_table.public_route_table.id
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
  vpc_id      = aws_vpc.vpc_private.id

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
  vpc_id      = aws_vpc.vpc_private.id

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

resource "aws_api_gateway_vpc_link" "ecs_vpc_link" {
  name           = "vpc-link-valida-token"
  target_arns    = [aws_lb.nlb.arn]
}

resource "aws_api_gateway_rest_api" "valida_token_gateway" {
  name        = "valida-token-gateway"
  description = "API para validar tokens"
}

resource "aws_api_gateway_resource" "resource_api" {
  rest_api_id = aws_api_gateway_rest_api.valida_token_gateway.id
  parent_id   = aws_api_gateway_rest_api.valida_token_gateway.root_resource_id
  path_part   = "api"
}

resource "aws_api_gateway_resource" "resource_v1" {
  rest_api_id = aws_api_gateway_rest_api.valida_token_gateway.id
  parent_id   = aws_api_gateway_resource.resource_api.id
  path_part   = "v1"
}

resource "aws_api_gateway_resource" "resource_token" {
  rest_api_id = aws_api_gateway_rest_api.valida_token_gateway.id
  parent_id   = aws_api_gateway_resource.resource_v1.id
  path_part   = "token"
}

resource "aws_api_gateway_resource" "resource_validate" {
  rest_api_id = aws_api_gateway_rest_api.valida_token_gateway.id
  parent_id   = aws_api_gateway_resource.resource_token.id
  path_part   = "validate"
}

resource "aws_api_gateway_integration" "validate_integration" {
  rest_api_id             = aws_api_gateway_rest_api.valida_token_gateway.id
  resource_id             = aws_api_gateway_resource.resource_validate.id
  http_method             = aws_api_gateway_method.get_token_validate.http_method
  integration_http_method = "GET"
  type                    = "HTTP_PROXY"
  uri                     = "http://${aws_lb.nlb.dns_name}/api/v1/token/validate"
  connection_type         = "VPC_LINK"
  connection_id           = aws_api_gateway_vpc_link.ecs_vpc_link.id
  depends_on = [
    aws_api_gateway_vpc_link.ecs_vpc_link
  ]
}

resource "aws_api_gateway_method" "get_token_validate" {
  rest_api_id   = aws_api_gateway_rest_api.valida_token_gateway.id
  resource_id   = aws_api_gateway_resource.resource_validate.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_deployment" "validate_deployment" {
  rest_api_id = aws_api_gateway_rest_api.valida_token_gateway.id
  stage_name  = "dev"
  depends_on = [
    aws_api_gateway_method.get_token_validate,
    aws_api_gateway_integration.validate_integration
  ]
}

resource "aws_lb" "nlb" {
  name               = "${var.service_name}-nlb"
  internal           = true
  load_balancer_type = "network"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = [aws_subnet.private_subnet_1.id, aws_subnet.private_subnet_2.id, aws_subnet.private_subnet_3.id]
}

resource "aws_lb_target_group" "ecs_tg" {
  name        = "${var.service_name}-tg"
  port        = var.container_port
  protocol    = "TCP"
  vpc_id      = aws_vpc.vpc_private.id
  target_type = "ip"

  health_check {
    path                = "/actuator"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

resource "aws_lb_listener" "tpc" {
  load_balancer_arn = aws_lb.nlb.arn
  port              = 80
  protocol          = "TCP"

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
    subnets          = [aws_subnet.private_subnet_1.id, aws_subnet.private_subnet_2.id, aws_subnet.private_subnet_3.id]
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.ecs_tg.arn
    container_name   = var.container_name
    container_port   = var.container_port
  }

  depends_on = [aws_lb_listener.tpc, aws_ecs_cluster.cluster_valida_token]
}
