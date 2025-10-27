# Simulador de Crédito - Mivivienda

API REST para simulación de créditos hipotecarios con autenticación JWT.

## 🟥 Módulo Auth & Platform

### Endpoints Implementados

#### Autenticación

- **POST** `/api/v1/auth/register` - Registro de nuevos usuarios
- **POST** `/api/v1/auth/login` - Inicio de sesión
- **POST** `/api/v1/auth/refresh` - Renovación de token de acceso

#### Monitoreo

- **GET** `/api/v1/health` - Estado del sistema

### Requisitos

- **Java JDK 17+** (el proyecto usa Java 25)
- **Maven 3.6+**
- **MySQL 8.0+**

### Configuración

1. **Base de datos MySQL**

```sql
CREATE DATABASE simulador_credito;
```

2. **Variables de entorno** (opcional, configuradas en `application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/simulador_credito
spring.datasource.username=root
spring.datasource.password=root
```

### Ejecución

```bash
# Compilar
./mvnw clean compile

# Ejecutar
./mvnw spring-boot:run
```

En Windows:

```cmd
mvnw.cmd clean compile
mvnw.cmd spring-boot:run
```

### Endpoints de Autenticación

#### 1. Registro

```http
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "username": "usuario123",
  "email": "usuario@example.com",
  "password": "miPassword123"
}
```

**Respuesta:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "username": "usuario123",
  "email": "usuario@example.com",
  "role": "USER"
}
```

#### 2. Login

```http
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
  "username": "usuario123",
  "password": "miPassword123"
}
```

#### 3. Refresh Token

```http
POST http://localhost:8080/api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 4. Health Check

```http
GET http://localhost:8080/api/v1/health
```

**Respuesta:**

```json
{
  "status": "UP",
  "service": "Simulador de Crédito",
  "timestamp": "2025-10-27T19:00:00",
  "version": "1.0.0"
}
```

### Uso de Tokens

Para acceder a endpoints protegidos, incluir el token en el header:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Arquitectura

```
src/main/java/com/mivivienda/platform/simuladordecredito/
├── auth/
│   ├── application/
│   │   └── internal/
│   │       ├── commandservices/
│   │       │   └── AuthService.java
│   │       └── outboundservices/
│   │           ├── JwtService.java
│   │           └── UserDetailsServiceImpl.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   └── Role.java
│   │   └── persistence/
│   │       └── UserRepository.java
│   ├── dto/
│   │   ├── AuthResponse.java
│   │   ├── LoginRequest.java
│   │   ├── RefreshTokenRequest.java
│   │   └── RegisterRequest.java
│   ├── infrastructure/
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   └── filters/
│   │       └── JwtAuthenticationFilter.java
│   └── interfaces/
│       └── rest/
│           └── AuthController.java
└── platform/
    └── interfaces/
        └── rest/
            └── HealthController.java
```

### Tecnologías Utilizadas

- **Spring Boot 3.5.6**
- **Spring Security** - Autenticación y autorización
- **JWT (jjwt 0.12.3)** - Tokens de acceso
- **Spring Data JPA** - Persistencia
- **MySQL** - Base de datos
- **Lombok** - Reducción de código boilerplate
- **Jakarta Validation** - Validación de datos

### Justificación del Diseño

Este módulo responde directamente a los requisitos del informe:

- **Capítulo 3.2 - Diseño de la Interfaz**: Implementa las vistas de inicio de sesión, registro y recuperación
- **Capítulo ALCANCE**: Cumple con la implementación de base de datos estructurada para almacenar usuarios y simulaciones
- **Capítulo STUDENT OUTCOME**: Demuestra investigación y diseño de modelo de base de datos para registro de información

### Próximos Pasos

Implementar módulos adicionales:
- 📘 **Cálculos** - Endpoints de simulación de crédito
- 📊 **Reportes** - Historial y análisis de simulaciones
