# La Tiendita — Sistema de gestión administrativa

Aplicación de escritorio desarrollada en Java 17 y Swing para apoyar la gestión de productos, ventas, clientes, categorías, cola de atención, clientes frecuentes, distribución de estantes e historial.

## Requisitos

- JDK 17 o superior.
- Apache Maven 3.9 o compatible (NetBeans puede gestionar Maven).
- Conexión a Internet durante la primera compilación para descargar dependencias.

## Abrir en Apache NetBeans

1. Descomprime el proyecto.
2. Selecciona **File → Open Project**.
3. Abre la carpeta raíz que contiene `pom.xml` (esta carpeta).
4. Clic derecho en el proyecto → **Clean and Build**.
5. Clic derecho → **Run**. La clase principal configurada es `com.discount3a.Main`.

No abras solamente `src` como proyecto; abre la carpeta que contiene el `pom.xml`.

## Compilar, probar y empaquetar

```bash
mvn clean test
mvn clean package
java -jar target/la-tiendita-1.0.jar
```

El ejecutable empaquetado incluye las dependencias de ejecución mediante Maven Shade.

## Arquitectura

- `com.discount3a.vista`: interfaces Swing y componentes visuales.
- `com.discount3a.controlador`: coordinación de operaciones y validaciones.
- `com.discount3a.modelo`: entidades de dominio.
- `com.discount3a.dao`: interfaces DAO e implementaciones de persistencia en archivos locales.
- `com.discount3a.structures`: estructuras de datos propias (vector, árbol, cola, lista, pila y matriz).
- `test`: pruebas automatizadas con JUnit 5.

## Librerías declaradas

- Google Guava: validaciones y utilidades.
- Apache Commons Lang / IO: utilidades de texto y archivos.
- Apache POI: generación de reportes Excel.
- SLF4J + Logback: registro de eventos.
- JUnit Jupiter: pruebas automatizadas.

## Persistencia y datos

La aplicación utiliza archivos locales; no requiere configurar una base de datos relacional. Los archivos generados en ejecución no deben contener información personal real al publicar capturas o ejemplos del proyecto.

## Pruebas

Ejecuta `mvn test`. Las pruebas actuales cubren operaciones de inventario, stock y cálculos/validaciones de venta. Antes de una entrega, revisa el resultado de cada prueba y agrega pruebas para los flujos adicionales implementados.

## Control de versiones

Consulta `docs/GITHUB_Y_APF3.md` para una lista de comprobación de publicación y evidencias.
