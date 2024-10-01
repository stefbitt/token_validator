terraform {
  required_version = "1.9.6"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.43.0"
    }
  }

  backend "s3" {
    bucket         = "token-validator-terraform"
    key            = "backend.tf"
    region         = "us-east-1"
  }
}

provider "aws" {
  region = var.region
}