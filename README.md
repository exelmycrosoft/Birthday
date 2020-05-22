Latam API Cloud Skeleton
Un ejemplo para crear servicios utilizando el stack tecnológico para proyectos cloud

Documentación útil
https://kubernetes.io/docs/tasks/access-application-cluster/connecting-frontend-backend/
https://kubernetes.io/docs/concepts/services-networking/service/#discovering-services
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
https://spring.io/guides/gs/consuming-rest/
Backend example
Servicio de ejemplo correspondiente a la capa de negocio. Este ejemplo requiere el endpoint de capa de abstracción de datos.

Editar el proyecto
Se recomienda utilizar Intellij IDEA UE (versión de prueba 30 días) e importar como proyecto Gradle

Compilar y generar el ensamblado
Gradle : ./gradlew build
Test   : java -jar build/libs/nombre_jar.jar
URL    : http://localhost:8080/be/example
Crear imagen Docker
En la raíz del proyecto, ejecutar los siguientes comandos:

Build de la imagen
docker build -f src/main/docker/Dockerfile -t be-cloudskeleton:0.0.1 .
Test de la imagen:
docker run --name api --rm -p 8080:8080 be-cloudskeleton:0.0.1
URL: http://localhost:8080/be/example

Push de la imagen
docker tag be-cloudskeleton:0.0.1 REGISTRY_USER/be-cloudskeleton:0.0.1
docker push REGISTRY_USER/be-cloudskeleton:0.0.1
NOTA 1: En proyectos Maven, es necesario actualizar la tarea COPY en el dockerFile

NOTA 2: reemplazar REGISTRY_USER por vuestra cuenta de usuario Docker Hub

Configurar Kubernetes local
En la raíz del proyecto, realizar las siguientes actividades:

Editar el archivo deployment.yaml
actualizar la ruta de acceso a la imagen docker (REGISTRY_USER/nombreImagen:ver)
Iniciar Kubernetes local
minikube start
Consola de kubernetes
minikube dashboard
NOTA: esto puede tardar un tiempo

Deploy del servicio
kubectl create -f deployment.yaml
Acceder al endpoint
Servicio expuesto por kubernetes

minikube service be-cloudskeleton
NOTA1: agregar a la url "/be/example"

Despliegue en Google Cloud Platform (GCP)
Requisitos
Google Project (https://console.cloud.google.com)
gcloud (https://cloud.google.com/sdk/docs/quickstarts)
kubectl (https://kubernetes.io/docs/tasks/tools/install-kubectl/#install-kubectl)
Cloud SQL - MySQL (https://console.cloud.google.com/sql/instances)
Configurar Cloud SDK y Kubectl
Obtener variables básicas
En https://console.cloud.google.com/cloud-resource-manager

PROJECT_ID   : en el listado de proyectos Google
En https://console.cloud.google.com/kubernetes/list

CLUSTER_NAME : en el listado de clusters de Kubernetes
Ejecutar en consola
gcloud components update
gcloud auth login
gcloud config set project PROJECT_ID
gcloud components install kubectl
gcloud auth application-default login
gcloud container clusters get-credentials CLUSTER_NAME
kubectl version
Crear service account key
En https://console.cloud.google.com/apis/api/servicecontrol.googleapis.com

Seleccionar

left-navigation bar, click Credentials
Create credentials, select Service account key
Service account drop-down, click New service account
Service account name: API_NAME-api.endpoints.PROJECT_ID.cloud.goog
Role drop-down, Project > Editor
Key type, use the default JSON
Se descargara un archivo .json, renombrar a service-account-creds.json y dejarlo en la carpeta del proyecto

NOTA: el service account name es utilizado para configurar Google Endpoints.

Configurar Google Endpoints
Actividad específica para servicios expuestos, como servicios Backend o Backend for Frontend por ejemplo.

Actualizar descriptor OpenAPI
En el archivo openapi.yaml ubicado en la raíz del proyecto

Actualizar el parámetro host con el service account name:
host: "API_NAME-api.endpoints.PROJECT_ID.cloud.goog"
 
Actualizar/agregar los paths y métodos http:
paths:
  "/be/example":
    get:
NOTA: Por default Google Endpoints bloquea el acceso a paths/verbos no configurados en este descriptor.

Configurar Endpoints en Kubernetes
Ejecutar en la ubicación del archivo service-account-creds.json:

kubectl create secret generic service-account-creds --from-file=service-account-creds.json
Desplegar configuración en Google Cloud
Ejecutar en la ubicación del archivo openapi.yaml (raiz del proyecto)

gcloud endpoints services deploy openapi.yaml
Configurar deployment de Endpoints en Kubernetes
En el archivo deployment.yaml del proyecto:

Actualizar el targetPort de servicio:

8080 service, 8081 service with endpoints
targetPort: 8081
Actualizar el tipo de servicio:

LoadBalancer: Para exponer el servicio en GCP
NodePort: Para exponer el servicio solamente en el cluster de Kubernetes
Actualizar el service account name en la imagen de endpoints-runtime

--service=API_NAME-api.endpoints.PROJECT_ID.cloud.goog
NOTA: El service account name es el mismo del archivo openapi.yaml (host)

Desplegar deployment en el cluster de Kubernetes
Ejecutar el comando kubectl:

kubectl create -f deployment.yaml
Obtener un API Key
Seleccionar el proyecto y "APIs y servicios"

https://console.cloud.google.com/apis/credentials
left-navigation bar, click Credentials
Create credentials, select API Key
Consumir el servicio expuesto por Balanceador
Buscar el deployment correspondiente (por ejemplo be-cloudskeleton) y buscar los Endpoints externos

https://console.cloud.google.com/kubernetes/discovery
Acceder al endpoint del servicio, por ejemplo:

http://IP_BALANCER/be/example?key=API_KEY