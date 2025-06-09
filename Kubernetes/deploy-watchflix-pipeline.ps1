# Apply Secrets (TODO???)
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

# Refresh the pods
kubectl rollout restart deployment/eureka
kubectl rollout restart deployment/rabbitmq
kubectl rollout restart deployment/event-bus
kubectl rollout restart deployment/movie-service
kubectl rollout restart deployment/movie-file-service
kubectl rollout restart deployment/user-service
kubectl rollout restart deployment/gateway
kubectl rollout restart deployment/webapp