# setup cluster
k3d cluster create cityapi --api-port 6550 -p "1024:8080@loadbalancer" --agents 2

# install chart
helm install cityapi --generate-name
