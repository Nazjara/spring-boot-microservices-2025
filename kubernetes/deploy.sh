#!/bin/bash

# Exit on error
set -e

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

echo "Configuring Minikube to use local Docker registry..."
eval $(minikube docker-env)

# Load images into Minikube's Docker registry
echo "Loading images into Minikube's Docker registry..."
for img in $(docker images | grep nazjara | awk '{print $1 ":" $2}'); do
  echo "Loading image: $img"
  minikube image load $img
done

# Apply Kubernetes configurations in order
echo "Applying Kubernetes configurations..."

# Apply ConfigMaps and Secrets first
echo "Applying ConfigMaps and Secrets..."
kubectl apply -f 01_configmap.yaml

# Apply database configurations
echo "Applying database configurations..."
kubectl apply -f 02_mysql-accounts.yaml
kubectl apply -f 03_mysql-cards.yaml
kubectl apply -f 04_mysql-loans.yaml
kubectl apply -f 05_mysql-keycloak.yaml

# Wait for databases to be ready
echo "Waiting for databases to be ready..."
kubectl wait --for=condition=ready pod -l app=mysql-accounts --timeout=300s
kubectl wait --for=condition=ready pod -l app=mysql-cards --timeout=300s
kubectl wait --for=condition=ready pod -l app=mysql-loans --timeout=300s
kubectl wait --for=condition=ready pod -l app=mysql-keycloak --timeout=300s

# Apply infrastructure services
echo "Applying infrastructure services..."
kubectl apply -f 06_rabbitmq.yaml
kubectl apply -f 07_redis.yaml
kubectl apply -f 08_minio.yaml

# Wait for infrastructure services to be ready
echo "Waiting for infrastructure services to be ready..."
kubectl wait --for=condition=ready pod -l app=rabbitmq --timeout=300s
kubectl wait --for=condition=ready pod -l app=redis --timeout=300s
kubectl wait --for=condition=ready pod -l app=minio --timeout=300s

# Apply config server and discovery service
echo "Applying config server and discovery service..."
kubectl apply -f 09_config-server.yaml
kubectl wait --for=condition=ready pod -l app=config-server --timeout=300s
kubectl apply -f 10_discovery-service.yaml
kubectl wait --for=condition=ready pod -l app=discovery-service --timeout=300s

# Apply core services
echo "Applying core services..."
kubectl apply -f 11_accounts.yaml
kubectl apply -f 12_loans.yaml
kubectl apply -f 13_cards.yaml
kubectl apply -f 14_message.yaml

# Wait for core services to be ready
echo "Waiting for core services to be ready..."
kubectl wait --for=condition=ready pod -l app=accounts --timeout=300s
kubectl wait --for=condition=ready pod -l app=loans --timeout=300s
kubectl wait --for=condition=ready pod -l app=cards --timeout=300s
kubectl wait --for=condition=ready pod -l app=message --timeout=300s

# Apply gateway service
echo "Applying gateway service..."
kubectl apply -f 15_gateway-service.yaml
kubectl wait --for=condition=ready pod -l app=gateway-service --timeout=300s

# Apply observability stack
echo "Applying observability stack..."
kubectl apply -f 16_loki.yaml
kubectl apply -f 17_tempo.yaml
kubectl apply -f 18_prometheus.yaml
kubectl apply -f 19_grafana.yaml
kubectl apply -f 20_alloy.yaml

# Apply Keycloak
echo "Applying Keycloak..."
kubectl apply -f 21_keycloak.yaml

# Verify deployment
echo "Verifying deployment..."
kubectl get pods

# Get service URLs
echo "Service URLs:"
echo "Gateway Service: $(minikube service gateway-service --url)"
echo "Grafana: $(minikube service grafana --url)"
echo "Prometheus: $(minikube service prometheus --url)"
echo "Keycloak: $(minikube service keycloak --url)"

echo "Deployment completed successfully!"

# Set up port forwarding to make services accessible on localhost
echo ""
echo "Setting up port forwarding to make services accessible on localhost..."
echo "You can access the services at:"
echo "- Grafana: http://localhost:3000"
echo "- Prometheus: http://localhost:9090"
echo "- Keycloak: http://localhost:8080"
echo ""
echo "Starting port forwarding in the background..."
kubectl port-forward svc/grafana 3000:3000 > /dev/null 2>&1 &
echo "Grafana port forwarding started with PID $!"
kubectl port-forward svc/prometheus 9090:9090 > /dev/null 2>&1 &
echo "Prometheus port forwarding started with PID $!"
kubectl port-forward svc/keycloak 8080:8080 > /dev/null 2>&1 &
echo "Keycloak port forwarding started with PID $!"
echo ""
echo "Keep this terminal window open to maintain port forwarding."
echo "Press Ctrl+C to stop port forwarding and exit."

# Wait for Ctrl+C
trap "echo 'Stopping port forwarding...'; kill %1 %2 %3 2>/dev/null || true; exit 0" INT
wait
