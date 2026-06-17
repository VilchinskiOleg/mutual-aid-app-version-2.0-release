#!/bin/bash
set -e

mkdir -p "$HOME/postgres/backup"

RELEASE=$(cat /etc/system-release)

if [[ "$RELEASE" == *"Amazon Linux release 2023"* ]]; then
  echo "Detected Amazon Linux 2023"
  dnf update -y
  dnf install -y docker
elif [[ "$RELEASE" == *"Amazon Linux release 2"* ]]; then
  echo "Detected Amazon Linux 2"
  yum update -y
  amazon-linux-extras install docker -y
  yum install -y docker
else
  echo "Unsupported Amazon Linux version: $RELEASE"
  exit 1
fi

systemctl enable docker
systemctl start docker

echo "Docker installed and started successfully"
