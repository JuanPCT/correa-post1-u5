# Reservas de laboratorios

Aplicación de la unidad 5: API REST y páginas MVC para reservar laboratorios universitarios. Construida con Java 17, Spring Boot 3.2.12, Spring Web, Spring Data JPA, H2, Validation, Lombok y Thymeleaf. La base H2 en memoria se inicializa con dos laboratorios de ejemplo.

## Ejecutar

```bash
mvn spring-boot:run
mvn test
```

La aplicación queda disponible en `http://localhost:8080`. Las pruebas verifican la consulta de solapamientos, las reglas de creación/cancelación, el catálogo REST, el formulario MVC y el mismo conflicto de dominio en ambas interfaces.

## Organización

```text
src/main/java/com/universidad/reservaslabs/
├── model/        Laboratorio, Reserva y EstadoReserva
├── repository/   persistencia y consulta JPQL de solapamientos
├── service/      ReservaService y ReservaRequest
├── exception/    errores de conflicto, validación y recurso inexistente
├── controller/   controladores REST y manejo de errores JSON
└── web/          controlador MVC y manejo de errores para las páginas
```

`ReservaService` centraliza la validación de datos, horario (07:00–21:00), duración (30 minutos–3 horas), la búsqueda de solapamientos y la cancelación. Las reservas quedan `PENDIENTE`; solo los estados `PENDIENTE` y `CONFIRMADA` bloquean el horario. Una cancelación conserva el registro y deja disponible ese intervalo.

## API REST

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/laboratorios` | Listar catálogo |
| GET | `/api/laboratorios/{id}` | Consultar laboratorio |
| POST | `/api/laboratorios` | Crear laboratorio |
| GET | `/api/reservas` | Listar reservas por inicio |
| GET | `/api/reservas/{id}` | Consultar reserva |
| GET | `/api/reservas/laboratorio/{id}` | Consultar reservas de un laboratorio |
| POST | `/api/reservas` | Crear solicitud |
| DELETE | `/api/reservas/{id}` | Cancelar sin borrar el historial |

`requests.http` contiene ejemplos para cada operación. Los errores se traducen a HTTP 409 (conflicto), 404 (recurso inexistente) y 400 (datos inválidos).

## Puntos de decisión

1. **Consulta de solapamientos: repositorio o servicio.** La consulta vive en `ReservaRepository.buscarSolapamientos`, porque filtra datos persistidos con JPQL y usa límites estrictos (`inicio existente < fin solicitado` y `fin existente > inicio solicitado`). `ReservaService` decide si el resultado impide guardar. La alternativa de calcular el solapamiento solo en el servicio obligaría a cargar y recorrer reservas, mezclaría acceso a datos con reglas de coordinación y sería más costosa.

2. **Horario y duración: entidad, servicio o repositorio.** La regla de 07:00–21:00 y la duración entre 30 minutos y 3 horas se valida en `ReservaService`, que también comprueba orden temporal y fecha. Son reglas de negocio de una solicitud, no consultas de persistencia. La alternativa de incorporarlas en el repositorio convertiría validaciones sencillas en filtros de datos y no impediría reservar fuera del rango al usar otros puntos de entrada. La entidad conserva el estado y los datos válidos, sin delegar estas reglas al repositorio.

3. **MVC y REST: dos servicios o uno compartido.** `ReservaPageController` y `ReservaApiController` llaman al mismo `ReservaService`; así comparten disponibilidad, horario, duración y cancelación. La alternativa de duplicar esas reglas en cada controlador puede producir respuestas diferentes y permitir inconsistencias entre el formulario y la API.

4. **Errores: respuesta REST o redirección MVC.** Los controladores lanzan las mismas excepciones de dominio. `ApiExceptionHandler` las presenta como JSON con estado HTTP apropiado; `ReservaPageExceptionHandler`, limitado al controlador MVC, presenta el mensaje en una página navegable. La alternativa de que cada controlador convierta errores por separado duplicaría la traducción y haría más difícil mantener una respuesta coherente.

## Evidencias

Las pruebas automatizadas incluyen la colisión de intervalo en el servicio, un error 409 en REST y el mismo mensaje en la vista MVC. Para obtener capturas de pantalla de las interfaces durante la demostración, inicia la aplicación y abre:

- `http://localhost:8080/api/laboratorios` (catálogo REST).
- `http://localhost:8080/reservas` (lista MVC).
- `http://localhost:8080/reservas/nueva` (formulario MVC).
- Para la evidencia de conflicto, intenta crear por REST y por MVC una reserva que se cruce con una ya existente.

Los ejemplos de solicitudes están en `requests.http` y la consulta visual de JSON está en `src/main/resources/static/evidencias/api.html`.
