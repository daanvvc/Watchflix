# minikube start --driver=docker --cpus=4 --disk-size=20g  --memory=3000

# Apply Secrets
kubectl apply -f secrets.yaml

# Apply backend services
kubectl apply -f eureka-service.yaml
kubectl apply -f eureka-deployment.yaml

kubectl apply -f rabbitmq-service.yaml
kubectl apply -f rabbitmq-deployment.yaml

kubectl apply -f event-bus-service.yaml
kubectl apply -f event-bus-deployment.yaml

kubectl apply -f movie-service-service.yaml
kubectl apply -f movie-service-deployment.yaml

kubectl apply -f movie-file-service-service.yaml
kubectl apply -f movie-file-service-deployment.yaml

kubectl apply -f user-service-service.yaml
kubectl apply -f user-service-deployment.yaml

kubectl apply -f gateway-service.yaml
kubectl apply -f gateway-deployment.yaml

# Apply frontend services
kubectl apply -f webapp-service.yaml
kubectl apply -f webapp-deployment.yaml

# Apply autoscalers (HPA)
kubectl apply -f user-service-hpa.yaml
kubectl apply -f gateway-hpa.yaml
kubectl apply -f movie-service-hpa.yaml
kubectl apply -f movie-file-service-hpa.yaml
kubectl apply -f event-bus-hpa.yaml