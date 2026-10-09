resource "aws_security_group" "mq_sg" {
  name        = "${var.env}-mq-sg"
  vpc_id      = var.vpc_id
  ingress {
    from_port   = 5671
    to_port     = 5671
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/8"]
  }
}

resource "aws_mq_broker" "rabbitmq" {
  broker_name        = "${var.env}-rabbitmq"
  engine_type        = "RabbitMQ"
  engine_version     = "3.12.13"
  host_instance_type = var.mq_instance_type
  security_groups    = [aws_security_group.mq_sg.id]
  subnet_ids         = [var.subnet_ids[0]]
  
  user {
    username = "admin"
    password = "AdminPassword123!"
  }
}
