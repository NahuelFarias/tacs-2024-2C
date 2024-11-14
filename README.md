# tacs-2024-2C

Documentación sobre nuestras decisiones de diseño: https://docs.google.com/document/d/1kYmrD5qZtwdmolbWqFbfbLGvZTbov2YZIHsyASKnbuw

Documentación sobre nuestras decisiones de arquitectura: https://docs.google.com/document/d/1lZdW1pqU6vr3iiSo8r5kPXE0CrkiC1BYGsyH9Pf6xNY

## Cómo ejecutar este proyecto
Prerrequisito para la ejecución: instalar **docker**.

### 1. Buildear el backend.

En la raiz del repositorio, buildear la imagen con:
```
docker build -t tacs-backend .
```

Esto genererará una imagen local con el nombre `tacs-backend`.

En caso de que solo se quiera probar el backend, se puede ejecutar:
```
docker run -d -p 8080:8080 tacs-backend
```
Se puede interactuar con la app a través un swagger en http://localhost:8080/swagger-ui/index.html.

### 2. Buildear el frontend.

En la carpeta /front/, buildear la imagen con

```
docker build -t tacs-frontend .
```

En caso de que solo se quiera probar el frontend, se puede ejecutar con:

```
docker run -d -p 3000:3000 tacs-frontend
```

### 3. Ejecutar el proyecto.
Ejecutar el proyecto con
```
docker compose up --detach
```
