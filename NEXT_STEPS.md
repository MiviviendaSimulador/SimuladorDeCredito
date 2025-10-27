# 📋 Próximos Pasos - Checklist

## ⚠️ Requisitos Previos

### 1. Instalar Java JDK 17 o superior
**Estado**: ❌ Pendiente (actualmente tienes Java 8)

**Opciones de descarga**:
- **OpenJDK 17** (recomendado, gratis): https://adoptium.net/
- **Oracle JDK 17+**: https://www.oracle.com/java/technologies/downloads/
- **Amazon Corretto 17**: https://aws.amazon.com/corretto/

**Pasos**:
1. Descargar el instalador para Windows
2. Ejecutar el instalador
3. Configurar `JAVA_HOME`:
   ```cmd
   setx JAVA_HOME "C:\Program Files\Java\jdk-17"
   setx PATH "%PATH%;%JAVA_HOME%\bin"
   ```
4. Verificar instalación:
   ```cmd
   java -version
   ```
   Debería mostrar: `java version "17.x.x"` o superior

### 2. Instalar MySQL
**Estado**: ❓ Por verificar

**Descargar**: https://dev.mysql.com/downloads/mysql/

**Pasos**:
1. Instalar MySQL Server
2. Crear la base de datos:
   ```sql
   CREATE DATABASE simulador_credito;
   ```
3. Verificar que está corriendo en puerto 3306
4. Actualizar credenciales en `application.properties` si es necesario

## 🚀 Compilar y Ejecutar

### 3. Compilar el proyecto
```cmd
cd C:\Users\ventu\SimuladorDeCredito
.\mvnw.cmd clean compile
```

**Esperado**: 
```
[INFO] BUILD SUCCESS
```

### 4. Ejecutar la aplicación
```cmd
.\mvnw.cmd spring-boot:run
```

**Esperado**: 
```
Started SimuladorDeCreditoApplication in X.XXX seconds
```

### 5. Verificar que funciona
Abrir en navegador o Postman:
```
http://localhost:8080/api/v1/health
```

**Esperado**: 
```json
{
  "status": "UP",
  "service": "Simulador de Crédito",
  "timestamp": "2025-10-27T...",
  "version": "1.0.0"
}
```

## 🧪 Probar los Endpoints

### 6. Probar Registro
```bash
curl -X POST http://localhost:8080/api/v1/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"email\":\"test@test.com\",\"password\":\"123456\"}"
```

O usar el archivo `api-tests.http` en VS Code/IntelliJ

### 7. Probar Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"123456\"}"
```

### 8. Verificar Base de Datos
```sql
USE simulador_credito;
SELECT * FROM users;
```

Deberías ver el usuario registrado con password encriptado.

## 📝 Git - Guardar Cambios

### 9. Revisar archivos modificados
```cmd
git status
```

### 10. Agregar archivos al stage
```cmd
git add .
```

### 11. Commit
```cmd
git commit -m "feat: implementar módulo Auth & Platform con JWT

- Agregar AuthController con endpoints register, login, refresh
- Agregar HealthController para monitoreo
- Configurar Spring Security con JWT
- Agregar CorsConfig para frontend
- Documentación completa en README.md
- Resolver conflicto en .gitignore

Endpoints:
- POST /api/v1/auth/register
- POST /api/v1/auth/login
- POST /api/v1/auth/refresh
- GET /api/v1/health"
```

### 12. Push al repositorio
```cmd
git push origin main
```

## 🎯 Verificación Final

### ✅ Checklist Completo

- [ ] Java 17+ instalado y verificado
- [ ] MySQL instalado y base de datos creada
- [ ] Proyecto compila sin errores
- [ ] Aplicación se ejecuta correctamente
- [ ] Endpoint `/health` responde
- [ ] Registro de usuario funciona
- [ ] Login de usuario funciona
- [ ] Tokens JWT se generan correctamente
- [ ] Base de datos almacena usuarios
- [ ] Cambios guardados en git
- [ ] Cambios pusheados al repositorio

## 📚 Recursos Adicionales

### Documentación
- `README.md` - Guía completa del proyecto
- `IMPLEMENTACION.md` - Detalles de la implementación
- `api-tests.http` - Colección de pruebas HTTP

### Testing
Usar cualquiera de estos:
- **VS Code**: Extensión "REST Client" + archivo `api-tests.http`
- **IntelliJ IDEA**: HTTP Client integrado + archivo `api-tests.http`
- **Postman**: Crear colección manualmente
- **cURL**: Comandos en esta guía

## 🆘 Troubleshooting

### Error: "No compiler is provided"
**Solución**: Instalar JDK (no JRE) y configurar JAVA_HOME

### Error: "Access denied for user 'root'@'localhost'"
**Solución**: Verificar usuario/password en `application.properties`

### Error: "Table 'simulador_credito.users' doesn't exist"
**Solución**: Asegurarse que `spring.jpa.hibernate.ddl-auto=update` en properties

### Error: Puerto 8080 ocupado
**Solución**: Cambiar puerto en `application.properties`:
```properties
server.port=8081
```

## 🎉 ¡Listo!

Una vez completes todos estos pasos, el módulo **Auth & Platform** estará completamente funcional y desplegado.

Puedes continuar con la implementación de los siguientes módulos:
- 📘 Módulo de Simulaciones
- 📊 Módulo de Reportes
- 👤 Módulo de Perfil de Usuario
