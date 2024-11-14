# tacs-2024-2C

Documentación sobre nuestras decisiones de diseño: https://docs.google.com/document/d/1kYmrD5qZtwdmolbWqFbfbLGvZTbov2YZIHsyASKnbuw

Documentación sobre nuestras decisiones de arquitectura: https://docs.google.com/document/d/1lZdW1pqU6vr3iiSo8r5kPXE0CrkiC1BYGsyH9Pf6xNY

## Como acceder a este proyecto.
Actualmente, se encuentra deployado en AWS. Se puede acceder a traves de:

http://tacs-frontend.s3-website.us-east-2.amazonaws.com/#/

## Cómo ejecutar localmente este proyecto
Prerrequisitos: 
- Instalar **docker**.
- En `tacs-2024-2C/src/main/resources/application.properties`, setear `app.frontDomainDeploy=localhost`.

En la raiz del proyecto, ejecutar:
```
docker compose up --detach --build
```

Luego, se puede interactuar con la app a través de:
- Swagger, en http://localhost:8080/swagger-ui/index.html.
- mongo-express, en http://localhost:8081.
- El frontend, en http://localhost:80

