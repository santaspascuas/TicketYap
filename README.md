TickeyYap 🎟️

Backend desarrollado con Spring Boot para la compra y venta de tickets de eventos.
La aplicación gestiona autenticación, pagos, generación de entradas PDF con QR, almacenamiento de archivos y procesamiento asíncrono de eventos.

🚀 Tecnologías utilizadas
Java 21
Spring Boot 3
Spring Security
Spring JDBC
PostgreSQL
Flyway
Spring Batch
JWT Authentication
Stripe API
MinIO
Java Mail Sender
PDFBox
ZXing (QR Generation)
Actuator
🧩 Características principales
🎫 Gestión de tickets
Compra y venta de entradas
Gestión de eventos
Validación de disponibilidad
Emisión automática de tickets
🔐 Seguridad
Autenticación basada en JWT
Spring Security
Protección de endpoints
Roles y permisos
💳 Integración de pagos
Integración con Stripe
Procesamiento de pagos online
Validación de transacciones
📄 Generación de tickets PDF
Creación automática de PDFs
Inclusión de códigos QR únicos
Verificación de tickets mediante QR
☁️ Almacenamiento de archivos
Integración con MinIO
Gestión de imágenes y recursos de eventos
📧 Sistema de emails
Envío automático de confirmaciones
Notificaciones de compra
Emails transaccionales
⚡ Procesamiento asíncrono
Uso de listeners asíncronos
Eventos desacoplados
Mejora del rendimiento en operaciones críticas
🧹 Procesos Batch

Servicio batch encargado de:

Limpieza automática de eventos expirados
Eliminación de datos obsoletos
Mantenimiento periódico del sistema

Implementado con:

Spring Batch
🗄️ Persistencia

La aplicación utiliza:

Spring Data JDBC
PostgreSQL
Migraciones versionadas con Flyway
📁 Estructura del proyecto
src/
 ├── main/
 │   ├── java/
 │   │   ├── config/
 │   │   ├── controller/
 │   │   ├── service/
 │   │   ├── repository/
 │   │   ├── security/
 │   │   ├── batch/
 │   │   ├── listener/
 │   │   └── domain/
 │   └── resources/
 │       ├── db/migration/
 │       └── application.yml

 
 
⚙️ Configuración
Variables importantes
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ticketyap
    username: postgres
    password: password

jwt:
  secret: your-secret-key

stripe:
  secret-key: your-stripe-secret

minio:
  endpoint: http://localhost:9000
  access-key: minio
  secret-key: minio123

spring:
  mail:
    host: smtp.gmail.com
    port: 587
▶️ Ejecución del proyecto
Clonar repositorio
git clone https://github.com/tuusuario/ticketyap.git
cd ticketyap
Ejecutar aplicación
./mvnw spring-boot:run

o

mvn spring-boot:run
🧪 Testing
mvn test
📦 Dependencias destacadas
Dependencia	Uso
Spring Web	API REST
Spring Security	Seguridad
Spring JDBC	Acceso a datos
PostgreSQL	Base de datos
Flyway	Migraciones
Stripe	Pagos
MinIO	Almacenamiento
PDFBox	Generación PDF
ZXing	QR Codes
Spring Batch	Procesos batch
Spring Mail	Emails
JWT	Autenticación
📈 Monitorización

La aplicación incluye:

Spring Boot Actuator
Métricas y health checks
Endpoints de monitorización
🔄 Arquitectura

El backend sigue una arquitectura basada en capas:

Controllers
Services
Repositories
Domain
Security
Batch Processing
Async Event Listeners
🛠️ Futuras mejoras
Dockerización completa
Integración con Kafka/RabbitMQ
Cache distribuida con Redis
Sistema de recomendaciones
Panel administrativo
Rate limiting
Observabilidad avanzada
👨‍💻 Autor

Desarrollado como proyecto backend de gestión de tickets y eventos con enfoque en:

escalabilidad
procesamiento asíncrono
automatización batch
seguridad
generación documental
