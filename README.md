# Proyecto Odontología

## Descripción

Este proyecto es una aplicación de gestión de odontología desarrollada con **Spring Boot** utilizando **JDK 21**. La aplicación permite la gestión de pacientes, odontólogos y turnos. Se utiliza **Maven** como sistema de construcción y **H2** como base de datos en memoria para el almacenamiento de datos. Los registros se gestionan mediante **Log4j** y la interfaz de usuario está desarrollada con **HTML**, **CSS** y **JavaScript**.

## Tecnologías Utilizadas

- **JDK 21**: Versión del JDK utilizada para desarrollar el proyecto.
- **Spring Boot**: Framework para simplificar el desarrollo de aplicaciones Java.
- **Maven**: Sistema de gestión y construcción de proyectos.
- **Log4j**: Librería para el registro de logs.
- **H2**: Base de datos en memoria.
- **HTML/CSS/JS**: Tecnologías para el desarrollo de la interfaz de usuario.

## Uso

Al iniciar la aplicación, abre un navegador web y visita `http://localhost:8080` para acceder a la interfaz de usuario. Desde allí, puedes gestionar pacientes, odontólogos y turnos.

## Estructura del Proyecto

- **src/main/java/com/digitalhouse/odontologia**: Contiene el código fuente de la aplicación.
    - **controller**: Controladores para gestionar las solicitudes HTTP.
    - **entity**: Entidades de la base de datos.
    - **exception**: Clases para manejar excepciones personalizadas.
    - **repository**: Interfaces para el acceso a la base de datos.
    - **service**: Lógica de negocio y servicios de la aplicación.
- **src/main/resources**: Recursos estáticos y configuraciones.
    - **static**: Archivos estáticos como CSS, JavaScript e imágenes.
    - **application.properties**: Configuraciones de la aplicación.
- **src/test/java**: Contiene las pruebas unitarias para la aplicación.
