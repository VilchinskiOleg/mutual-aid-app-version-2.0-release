#!/bin/bash
mkdir -p /home/ec2-user/postgres/backup

if grep -q "Amazon Linux release 2" /etc/system-release; then
  yum update -y
  amazon-linux-extras install docker -y
elif grep -q "Amazon Linux release 2023" /etc/system-release; then
  dnf update -y
  dnf install -y docker
else
  echo "Unsupported Amazon Linux version"
  exit 1
fi

systemctl enable docker
systemctl start docker