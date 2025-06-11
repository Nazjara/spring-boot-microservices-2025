#!/bin/bash

set -e

IMAGES=(
  "nazjara/accounts:1.0.0-SNAPSHOT"
  "nazjara/cards:1.0.0-SNAPSHOT"
  "nazjara/configserver:1.0.0-SNAPSHOT"
  "nazjara/discovery-service:1.0.0-SNAPSHOT"
  "nazjara/gateway-service:1.0.0-SNAPSHOT"
  "nazjara/loans:1.0.0-SNAPSHOT"
)

MODULES=(
  "accounts"
  "cards"
  "configserver"
  "discovery-service"
  "gateway-service"
  "loans"
)

echo "=== Removing Docker images if exist ==="
for IMAGE in "${IMAGES[@]}"; do
  if docker images -q "$IMAGE" > /dev/null 2>&1 && [ -n "$(docker images -q "$IMAGE")" ]; then
    docker rmi -f "$IMAGE"
    echo "Deleted $IMAGE"
  else
    echo "$IMAGE not found, skipping"
  fi
done

echo "=== Building all components with Maven (skip tests) ==="
for MODULE in "${MODULES[@]}"; do
  if [ -d "$MODULE" ]; then
    echo "Building $MODULE"
    (cd "$MODULE" && mvn clean package -DskipTests)
  else
    echo "Directory $MODULE does not exist, skipping"
  fi
done

echo "=== Building Docker images with Maven Jib plugin ==="
for MODULE in "${MODULES[@]}"; do
  if [ -d "$MODULE" ]; then
    echo "Building image for $MODULE"
    (cd "$MODULE" && mvn jib:dockerBuild -DskipTests)
  fi
done

echo "=== Done ==="