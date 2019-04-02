# mastermind

Proyecto del juego Mastermind por Alejandro Lajusticia Delgado

## Setup

Este proyecto requiere **gradle** y **lombok**.

Para poder usar el proyecto en un IDE como **IntelliJ** o **Eclipse** hay que configurar lombok
### IntelliJ: https://projectlombok.org/setup/intellij
### Eclipse: https://projectlombok.org/setup/eclipse

Una vez que el proyecto compile, al bajar todas las dependencias de gradle y tener configurado correctamente lombok, se puede levantar el proyecto directamente desde el IDE y ocupara el puerto **80**.

He generado el bootjar resultante en el directorio: **/bootjar** se puede ejecutar mediante **$java -jar ./bootjar/mastermind-1.0.0.jar**

El proyecto utiliza una base de datos H2, que se puede acceder mediante el endpoint: http://localhost/h2
```
user: sa
password:
```
(Sin password)


Para verificar que la aplicación se ha levantado correctamente se puede consultar el endpoint de health del actuator: http://localhost/actuator/health

Se puede acceder a la documentación del **Swagger de la API REST** mediante: http://localhost/swagger-ui.html

Esta configurado **jacoco** para generar el detalle de los test en forma de HTML: con la taread de **gradle :jacocoTestReport**

## REST-API ENDPOINTS

### [POST] localhost/games - Crear un juego
Ej body: 
```
{ "maxAttempts": 10 }
```

### [GET] localhost/games/{id} - Ver un juego
El id se obtien como respuesta de la petición de creación de un juego

### [GET] localhost/games/{id}/attempts - Ver todos los intentos de un juego

### [PUT] localhost/games/{id}/attempt - Realizar un intento
Ej body: 
```
{ "attempt": ["RED", "BLUE", "PURPLE", "RED"] }
```

