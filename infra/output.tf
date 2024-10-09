output "load_balancer_dns" {
  value = aws_lb.nlb.dns_name
}

output "api_gateway_url" {
  value = aws_api_gateway_deployment.validate_deployment.invoke_url
}