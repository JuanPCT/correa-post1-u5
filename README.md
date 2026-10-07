# Reserva de laboratorios · Unidad 5

Aplicación Spring Boot con API REST y páginas Thymeleaf que consumen el mismo `ReservaService`. Java 17, Spring Boot 3.5.16 y H2 en memoria.

## Ejecutar

```bash
mvn spring-boot:run
mvn test
```

Catálogo inicial: laboratorios LAB-101 y LAB-202. La API ofrece `GET /api/reservas`, `GET /api/reservas/{id}`, `POST /api/reservas` y `DELETE /api/reservas/{id}` (cancelación lógica). Interfaz: `/reservas` y `/reservas/nueva`. Ejemplos HTTP: [requests.http](requests.http).

## Diseño

`ReservaApiController` y `ReservaPageController` delegan en el mismo servicio; las reglas de horario, duración y solapamiento no se duplican entre REST y MVC. `ReservaRepository.existeSolapamiento` expresa la consulta del dominio y excluye reservas canceladas. La reserva guarda laboratorio, solicitante, inicio, fin y estado; una reserva solo se cancela antes de empezar y el registro se conserva. Las respuestas REST usan 404 para identificadores inexistentes, 409 para conflictos de disponibilidad/estado y 400 para datos o rangos inválidos. En MVC, los mismos errores vuelven al formulario como mensaje legible.

## Evidencias y pruebas

`ReservaServiceTest` cubre creación, colisión de horario, duración y cancelación lógica sobre H2. La página [evidencias/api.html](src/main/resources/static/evidencias/api.html) consulta en vivo `GET /api/reservas` y presenta el JSON para una captura legible; las pantallas MVC están en `/reservas` y `/reservas/nueva`. Con la aplicación iniciada, captura esas tres vistas para anexarlas a la entrega.
