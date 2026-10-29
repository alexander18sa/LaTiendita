# Lista de comprobación para APF3

Esta guía organiza evidencias solicitadas en la consigna. No reemplaza la comprobación real de ejecución ni certifica por sí sola el cumplimiento de la rúbrica.

## Arquitectura y calidad

- [ ] Abrir la carpeta raíz con `pom.xml` en NetBeans.
- [ ] Ejecutar `mvn clean test` y guardar evidencia de resultado exitoso.
- [ ] Ejecutar `mvn clean package` y verificar el JAR en `target/`.
- [ ] Probar desde la GUI los flujos de inventario, ventas, cola, clientes frecuentes, categorías, estantes, historial y reportes.
- [ ] Revisar validación de entradas, mensajes de error y tratamiento de archivos locales.
- [ ] Revisar que las responsabilidades de vista, controlador, modelo y DAO estén separadas.
- [ ] Documentar las clases públicas principales con Javadoc y generar la documentación si el docente la solicita.
- [ ] Ampliar pruebas unitarias y de integración; no afirmar TDD si no existe evidencia del ciclo de pruebas antes/después de los cambios.

## Git y GitHub

- [ ] Crear repositorio en GitHub.
- [ ] Subir el código fuente, `pom.xml`, pruebas, `README.md`, `.gitignore` y documentación pertinente.
- [ ] No subir `target/`, JAR compilados, datos reales, contraseñas, tokens ni archivos privados.
- [ ] Realizar commits pequeños y descriptivos que reflejen trabajo real.
- [ ] Tomar capturas del repositorio, historial de commits y ejecuciones de pruebas.
- [ ] Compartir el repositorio con el docente si es privado.

## Sustentación

Preparar una presentación de hasta aproximadamente 10 minutos según la consigna: problema y alcance, arquitectura MVC/DAO, estructuras de datos, librerías, interfaz, pruebas y control de versiones.
