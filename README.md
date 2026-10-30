# Jenkins: Pipelines, Job DSL y API de ejemplo

Repositorio de ejercicios para aprender Jenkins: pipelines declarativos, scripts de Job DSL,
despliegue de Jenkins con Docker Compose y una API de ejemplo en Node.js + Express.

## Estructura del repositorio

```
.
├── api/                      # API de ejemplo (Node.js + Express)
│   ├── Dockerfile            # Imagen de la API
│   ├── .dockerignore
│   ├── index.js              # Servidor Express (puerto 3000)
│   ├── package.json
│   ├── package-lock.json
│   └── test/test.js          # Tests (Mocha)
├── jenkins/                  # Todo lo relacionado con Jenkins
│   ├── docker-compose.yml    # Despliegue de Jenkins
│   ├── DSL/                  # Scripts de Job DSL
│   └── pipeline/            # Pipelines declarativos (ejercicios 1-9)
└── .gitignore
```

## Requisitos previos

- Docker y Docker Compose
- Node.js 20+ (para ejecutar la API y los tests localmente)
- Jenkins LTS con los plugins: *Docker Pipeline*, *NodeJS* y *Job DSL*

## Despliegue de Jenkins

```bash
cd jenkins
docker compose up -d
```

La interfaz web queda disponible en:

- **UI**: http://localhost:8089
- **Agentes (JNLP)**: puerto 50009

El `docker-compose.yml` monta el socket de Docker del host (`/var/run/docker.sock`), lo que
permite que los pipelines construyan imágenes usando el demonio del host. La configuración
persistente se guarda en `/opt/jenkins/jenkins_home`.

## Pipelines declarativos (`jenkins/pipeline/`)

| Archivo | Descripción |
|---|---|
| `1-primer-pipeline.groovy` | Pipeline declarativo básico (Build / Test / Deploy). |
| `2-pipeline-secuencial.groovy` | Stages anidados ejecutados de forma secuencial. |
| `3-pipeline-timeout.groovy` | Uso de `options { timeout(...) }` para limitar la duración. |
| `5-pipeline-params-and-inputs.groovy` | Parámetros (`string`, `booleanParam`) e inputs. |
| `6-pipeline-trigger.groovy` | Trigger programado con `cron('H/2 * * * *')`. |
| `7-pipeline-post.groovy` | Sección `post` (always, success, failure, etc.). |
| `8-pipeline-nodejs-1.groovy` | Pipeline Node.js: verificación SCM, tests con el plugin NodeJS y Docker Build & Push. |
| `9-pipeline-nodejs-2.groovy` | Variante que ejecuta los tests dentro de un contenedor `node:20-alpine` y hace Docker Build & Push. |

> El pipeline `4-pipeline-env-variable.groovy` (variables de entorno y credenciales) se encuentra en `jenkins/DSL/`.

## Job DSL (`jenkins/DSL/`)

| Archivo | Descripción |
|---|---|
| `javamavenDSL.groovy` | Placeholder para jobs de Java Maven. |
| `javamavenDSL2.groovy` | Job DSL para una app Java Maven (SCM Git). |
| `javamavenDSL3.groovy` | Variante del job DSL de Java Maven. |
| `nodejsDSL.groovy` | Job DSL para una app Node.js. |
| `nodejsDockerDSL.groovy` | Job DSL para una app Node.js con Docker. |
| `parametrizadoDSL.groovy` | Job DSL parametrizado. |
| `parametrizadoSSHDSL.groovy` | Job DSL con SCM configurado por SSH. |
| `4-pipeline-env-variable.groovy` | Pipeline con `environment` y credenciales. |

## API de ejemplo (`api/`)

La API es un servidor Express que responde `Hello World! Hola Mundo!` en `GET /` y escucha
en el puerto `3000`.

### Ejecutar localmente

```bash
cd api
npm install
npm start
```

Abre http://localhost:3000

### Ejecutar los tests

```bash
cd api
npm install --include=dev
npm test
```

### Construir y ejecutar con Docker

```bash
cd api
docker build -t mikkew94/nodejsapp .
docker run --rm -p 3000:3000 mikkew94/nodejsapp
```

## Pipeline de CI/CD de Node.js

Los pipelines `8-pipeline-nodejs-1.groovy` y `9-pipeline-nodejs-2.groovy` ejecutan tres stages:

1. **Verificación SCM**: `checkout scm` y obtención del commit corto (`git rev-parse --short HEAD`).
2. **test**: `npm install --only=dev` y `npm test` (con el plugin NodeJS o dentro de `node:20-alpine`).
3. **Docker Build & Push**: construye y publica `mikkew94/nodejsapp:<commit>` en Docker Hub,
   usando el contexto de build `api/`.

### Configuración necesaria en Jenkins

- **Credencial `docker-hub`**: usuario y contraseña (o token) de Docker Hub.
- **Instalación Node.js** con el nombre `nodejs-jenkins` (pipeline 8).
- Acceso al demonio de Docker desde el agente (el `docker-compose.yml` ya monta el socket).
