
# 🚀 Social Media Backend Deployment Guide (Microservices on Minikube)

## 📋 Prerequisites

* **Minikube:** Installed and configured.
* **Docker Desktop:** Installed and running.
* **kubectl:** Installed.

---

## 1. Start Minikube and Configure Docker Environment

First, you need to start your local Kubernetes cluster using Minikube and then ensure your local Docker CLI builds images directly into the Minikube Docker daemon. This step is crucial for **Minikube to find your locally built images**.

```bash
# 1. Start the Minikube cluster (if not already running)
minikube start

# 2. Point your local Docker CLI to the Minikube daemon
#    This makes 'docker build' save images directly inside Minikube's environment.
eval $(minikube -p minikube docker-env) 
````

-----

## 2\. Build Docker Images

Use the configured Docker environment to build the container images for each of your microservices. The `:latest` tag is used for simplicity in local development, and the images are immediately available to your Minikube cluster because of the previous `docker-env` step.

```bash
# Build the images for all microservices
docker build -t auth-service:latest ./authservice
docker build -t user-service:latest ./userservice
docker build -t follow-service:latest ./followservice
docker build -t post-service:latest ./postservice
docker build -t feed-service:latest ./feedservice
```

-----

## 3\. Enable Ingress and Deploy to Kubernetes

Ingress is required to expose your services externally, allowing you to access them via URLs using hostnames (e.g., `auth.socialmedia.local`).

```bash
# 1. Enable the Ingress controller addon in Minikube
minikube addons enable ingress

# 2. Apply all Kubernetes manifests (Deployments, Services, Ingress, etc.) 
#    located in your './k8s' directory.
kubectl apply -f ./k8s
```

-----

## 4\. Expose Services with Minikube Tunnel

The `minikube tunnel` command is essential for making the ClusterIP Services and Ingress routes accessible from your host machine (your laptop).

> **⚠️ Note on `minikube tunnel`:**
> This command requires root privileges because it modifies your host's networking routing table. It **must be run in a separate, persistent terminal window** and will require your system password. Keep this window open as long as you are using the cluster.

```bash
# Run this command in a NEW, separate terminal window.
sudo minikube tunnel
```