# tacs-2024-2C

### Cómo ejecutar este proyecto
Prerrequisito: instalar **docker**.

En la raiz del repositorio, buildear la imagen con:
```
docker build -t tp-tacs .
```

Esto genererará una imagen local con el nombre `tp-tacs`.

Ejecutar el container con:
```
docker run -d -p 8080:8080 tp-tacs
```
Se puede interactuar con la app a través un swagger en http://localhost:8080/swagger-ui/index.html.

Documentatción sobre nuestras decisiones de diseño: https://docs.google.com/document/d/1kYmrD5qZtwdmolbWqFbfbLGvZTbov2YZIHsyASKnbuw/edit?usp=sharing
