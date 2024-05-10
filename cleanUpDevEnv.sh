#!/bin/sh

# Stop containers based on name prefixes (modify if needed)
SERVICE_PREFIXES=("poc-" "akhq" "connect" "kafka" "zookeeper")

# Stop containers with a loop and error handling
for prefix in "${SERVICE_PREFIXES[@]}"; do
  container_ids=$(docker ps -a -q --filter name="^${prefix}")
  if [[ ! -z "$container_ids" ]]; then
    echo "Stopping containers starting with '${prefix}':"
    for id in $container_ids; do
      docker stop "$id" || echo "Error stopping container: $id"
    done
  else
    echo "No containers found starting with '${prefix}'"
  fi
done

# Remove all stopped containers
stopped_containers=$(docker ps -a -q)
if [[ ! -z "$stopped_containers" ]]; then
  echo "Removing stopped containers:"
  docker rm $stopped_containers || echo "Error removing containers"
else
  echo "No stopped containers found"
fi

echo "Cleanup completed."
