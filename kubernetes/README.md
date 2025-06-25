# Kubernetes Deployment for Spring Boot Microservices

This directory contains Kubernetes configurations for deploying the Spring Boot microservices architecture.

## Overview

The Kubernetes configurations are organized in numbered files to indicate the order in which they should be applied. Each service has its own configuration file that includes both the Deployment and Service resources.

## Prerequisites

- Minikube installed and running
- kubectl configured to use Minikube
- Docker with local images built (same as used in docker-compose.yml)

## Deployment Order

The deployment order is as follows:

1. ConfigMaps and Secrets
2. MySQL databases (accounts, cards, loans, keycloak)
3. Infrastructure services (RabbitMQ, Redis, MinIO)
4. Config Server
5. Discovery Service
6. Core services (accounts, loans, cards, message)
7. Gateway Service
8. Observability stack (Loki, Tempo, Prometheus, Grafana, Alloy)
9. Keycloak

## Deployment

To deploy the entire stack, use the provided deployment script:

```bash
# Navigate to the kubernetes directory
cd kubernetes

# Run the deployment script
./deploy.sh
```

This script will:
1. Configure Minikube to use the local Docker registry
2. Apply all Kubernetes configurations in the correct order
3. Wait for services to be ready before proceeding to dependent services
4. Verify the deployment and display service URLs

The script is designed to be run from within the kubernetes directory, as it uses relative paths to find the configuration files.

## Manual Deployment

If you prefer to deploy manually, apply the configurations in the numbered order:

```bash
kubectl apply -f 01_configmap.yaml
kubectl apply -f 02_mysql-accounts.yaml
# ... and so on
```

## Accessing Services

The following services are exposed with NodePort:

- Gateway Service: Entry point for the microservices
- Grafana: Monitoring dashboard
- Keycloak: Identity and access management

To get the URLs for these services:

```bash
minikube service gateway-service --url
minikube service grafana --url
minikube service keycloak --url
```

## Configuration Files

- `01_configmap.yaml`: Common configuration and secrets
- `02_mysql-accounts.yaml` to `05_mysql-keycloak.yaml`: Database configurations
- `06_rabbitmq.yaml` to `08_minio.yaml`: Infrastructure services
- `09_config-server.yaml` to `10_discovery-service.yaml`: Spring Cloud services
- `11_accounts.yaml` to `14_message.yaml`: Core microservices
- `15_gateway-service.yaml`: API Gateway
- `16_loki.yaml` to `20_alloy.yaml`: Observability stack
- `21_keycloak.yaml`: Identity and access management

## Volumes

Each service that requires persistent storage uses a PersistentVolumeClaim. In Minikube, these are automatically provisioned by the default StorageClass.
