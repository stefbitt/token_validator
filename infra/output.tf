output "load_balancer_dns" {
  value = aws_lb.alb.dns_name
}

output "api_gateway_url" {
  value = aws_apigatewayv2_stage.stage-gtw-valida-token.invoke_url
}