# SkyRegister — Evidencia GA7-220501096-AA3-EV01

Aplicación web académica para administrar drones y programar vuelos. Está construida con Java 21, Spring Boot, Thymeleaf, Spring JDBC y base de datos H2. La interfaz se adapta a pantallas de computador y móvil.

## Funcionalidades implementadas

- Tablero de inicio con indicadores de drones y vuelos.
- Listado, registro, edición y eliminación de drones.
- Validación de campos y unicidad del número de serie.
- Consulta de vuelos y programación de nuevas operaciones.
- Asignación de vuelos solo a drones que figuran como disponibles.
- Persistencia de datos en una base H2 local.
- Datos de muestra para facilitar la demostración.

## Requisitos

- JDK 21 instalado.
- IntelliJ IDEA.
- Conexión a Internet la primera vez que Maven descarga las dependencias.

## Abrir y ejecutar desde IntelliJ IDEA

1. Descomprime la carpeta del proyecto.
2. Abre IntelliJ IDEA y selecciona **File > Open**.
3. Selecciona la carpeta `SkyRegister`, donde se encuentra `pom.xml`.
4. Si IntelliJ pregunta, importa el proyecto como Maven y selecciona un JDK 21 en **Project Structure**.
5. Espera a que Maven termine de descargar las dependencias.
6. Abre `src/main/java/com/skyregister/SkyRegisterApplication.java`.
7. Pulsa el triángulo verde junto al método `main` y selecciona **Run SkyRegisterApplication**. Como alternativa, si Maven está disponible en la terminal, ejecuta `mvn spring-boot:run`.
8. Abre en el navegador `http://localhost:8080`.

Para detener la aplicación, vuelve a la terminal y presiona `Ctrl+C`.

## URLs principales

- Inicio: `http://localhost:8080/`
- Gestión de drones: `http://localhost:8080/drones`
- Gestión de vuelos: `http://localhost:8080/flights`
- Consola H2 para desarrollo: `http://localhost:8080/h2-console`

En la consola H2 usa JDBC URL `jdbc:h2:file:./data/skyregister`, usuario `sa` y contraseña en blanco. No expongas esta consola en una instalación productiva.

## Estructura del proyecto

```text
SkyRegister/
├── pom.xml
├── README.md
├── .gitignore
├── ENLACE_REPOSITORIO.txt
└── src/
    ├── main/
    │   ├── java/com/skyregister/
    │   │   ├── SkyRegisterApplication.java
    │   │   ├── controller/
    │   │   ├── model/
    │   │   └── repository/
    │   └── resources/
    │       ├── application.properties
    │       ├── schema.sql
    │       ├── data.sql
    │       ├── static/css/styles.css
    │       └── templates/
    └── test/java/com/skyregister/
```

## Tecnologías y buenas prácticas

- **Spring Boot / Spring MVC:** estructura de aplicación web y controladores.
- **Thymeleaf:** representación de vistas HTML.
- **Spring JDBC / JdbcTemplate:** acceso a datos con consultas parametrizadas.
- **H2:** base relacional local para simplificar la ejecución académica.
- **Validación:** anotaciones como `@NotBlank`, `@Size`, `@Min` y `@Max`.
- **Código:** nombres descriptivos, separación entre controladores, modelos y repositorios, comentarios explicativos y `.gitignore`.
- **Versionamiento:** proyecto preparado para Git; este ZIP incluye el repositorio local si fue generado desde el paquete entregado. Para publicar el código, sigue `ENLACE_REPOSITORIO.txt`.

## Alcance

Esta versión demuestra un módulo web funcional para la evidencia académica. Incluye gestión CRUD de drones y creación/consulta de vuelos. No incorpora autenticación de usuarios, mapas en tiempo real, telemetría física ni despliegue productivo.
