# 🐾 VetApp - Sistema de Gestión Veterinaria

Sistema completo de gestión veterinaria con **arquitectura modular** y **patrones de diseño**.

## 🏗️ Arquitectura y Patrones Implementados

### Patrones de Diseño
- **Repository Pattern**: Acceso a datos desacoplado
- **Service Layer Pattern**: Lógica de negocio centralizada
- **Facade Pattern**: Simplifica operaciones complejas (módulo Citas)
- **Factory Pattern**: Creación de notificaciones (módulo Notificación)
- **Strategy Pattern**: Diferentes tipos de cálculo de facturación
- **DTO Pattern**: Transferencia de datos segura

### Arquitectura Modular
```
vetapp/  
├── src/main/java/com/veterinaria/
│    ├── modules/
│    │   ├── propietario/      # Gestión de propietarios
│    │   ├── mascota/          # Gestión de mascotas
│    │   ├── cita/             # Agendamiento con notificaciones
│    │   ├── historia/         # Historias clínicas
│    │   ├── prescripcion/     # Prescripciones médicas
│    │   ├── facturacion/      # Facturación con estrategias
│    │   └── notificacion/     # Sistema de notificaciones
│    ├── shared/               # Componentes compartidos
│    └── config/               # Configuraciones
├──.gitignore
├──application.yml
├──docker-compose.yml
├──dockerfile
├──pom.xml
├──README.md
```

## 🚀 Stack Tecnológico

- **Backend**: Java 17 + Spring Boot 3.2.0
- **Base de Datos**: MySQL 8.0
- **Frontend**: HTML5 + CSS3 + JavaScript vanilla
- **Containerización**: Docker + Docker Compose
- **Email**: JavaMail con SMTP Gmail
- **Despliegue**: Railway

## 📋 Funcionalidades

1. ✅ **Registrar propietarios**
2. ✅ **Asignar mascotas a propietarios**
3. ✅ **Crear citas con notificación automática por email**
4. ✅ **Gestionar historias clínicas**
5. ✅ **Modificar historias clínicas**
6. ✅ **Prescribir medicamentos**
7. ✅ **Generar facturas completas**
8. ✅ **Notificaciones automáticas por Gmail**

## 🛠️ Instalación y Ejecución

### Prerrequisitos
- Docker y Docker Compose instalados
- Git

### Paso 1: Clonar el repositorio
```bash
git clone https://github.com/Samuel-Tabares/proyecto_nuclear
cd vetapp
```

### Paso 2: Configurar variables de entorno
```bash
cp .env.example .env
```

**Importante**: Para Gmail, debes:
1. Activar verificación en 2 pasos
2. Generar una "Contraseña de aplicación" en Google Account
3. Usar esa contraseña en `MAIL_PASSWORD`

### Paso 3: Levantar con Docker
```bash
docker-compose up --build
```

Esto levantará:
- **MySQL** en puerto 3306
- **Backend API** en puerto 8080
- Creará automáticamente la base de datos

### Paso 4: Acceder al sistema
- **API**: http://localhost:8080/api
- **Frontend**: Abrir `index.html` en el navegador

## 📡 Endpoints de la API

### Propietarios
- `POST /api/propietarios` - Crear propietario
- `GET /api/propietarios` - Listar todos
- `GET /api/propietarios/{id}` - Obtener por ID
- `PUT /api/propietarios/{id}` - Actualizar
- `DELETE /api/propietarios/{id}` - Eliminar

### Mascotas
- `POST /api/mascotas` - Registrar mascota
- `GET /api/mascotas` - Listar todas
- `GET /api/mascotas/{id}` - Obtener por ID
- `GET /api/mascotas/propietario/{propietarioId}` - Por propietario
- `PUT /api/mascotas/{id}` - Actualizar
- `DELETE /api/mascotas/{id}` - Eliminar

### Citas (con Facade Pattern)
- `POST /api/citas` - Crear cita + notificación automática
- `GET /api/citas` - Listar todas
- `GET /api/citas/{id}` - Obtener por ID
- `GET /api/citas/mascota/{mascotaId}` - Por mascota
- `PUT /api/citas/{id}` - Actualizar + notificar cambios
- `DELETE /api/citas/{id}` - Eliminar

### Historias Clínicas
- `POST /api/historias` - Crear historia
- `GET /api/historias` - Listar todas
- `GET /api/historias/{id}` - Obtener por ID
- `GET /api/historias/mascota/{mascotaId}` - Por mascota
- `PUT /api/historias/{id}` - Actualizar
- `DELETE /api/historias/{id}` - Eliminar

### Prescripciones
- `POST /api/prescripciones` - Crear prescripción
- `GET /api/prescripciones` - Listar todas
- `GET /api/prescripciones/{id}` - Obtener por ID
- `GET /api/prescripciones/mascota/{mascotaId}` - Por mascota
- `PUT /api/prescripciones/{id}` - Actualizar
- `DELETE /api/prescripciones/{id}` - Eliminar

### Facturas (con Strategy Pattern)
- `POST /api/facturas` - Crear factura
- `GET /api/facturas` - Listar todas
- `GET /api/facturas/{id}` - Obtener por ID
- `DELETE /api/facturas/{id}` - Eliminar

## 🧪 Pruebas con Postman

### 1. Registrar Propietario
```json
POST /api/propietarios
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "documento": "123456789",
  "telefono": "3001234567",
  "email": "juan@example.com",
  "direccion": "Calle 123"
}
```

### 2. Registrar Mascota
```json
POST /api/mascotas
{
  "propietarioId": 1,
  "nombre": "Max",
  "especie": "Perro",
  "raza": "Labrador",
  "sexo": "Macho",
  "peso": 25.5
}
```

### 3. Crear Cita (envía email automáticamente)
```json
POST /api/citas
{
  "mascotaId": 1,
  "fechaHora": "2024-12-15T10:00:00",
  "motivo": "Vacunación anual",
  "observaciones": "Traer carnet de vacunas"
}
```

### 4. Crear Historia Clínica
```json
POST /api/historias
{
  "mascotaId": 1,
  "fechaConsulta": "2024-12-10T14:30:00",
  "diagnostico": "Otitis leve",
  "sintomas": "Rascado excesivo de orejas",
  "tratamiento": "Gotas óticas",
  "pesoRegistrado": 25.5,
  "temperatura": 38.5
}
```

### 5. Crear Prescripción
```json
POST /api/prescripciones
{
  "mascotaId": 1,
  "medicamento": "Amoxicilina",
  "dosis": "250mg",
  "frecuencia": "Cada 12 horas",
  "duracionDias": 7,
  "fechaInicio": "2024-12-10",
  "indicaciones": "Administrar con comida"
}
```

### 5.5. Modificar Historia Clínica
```json
PUT /api/historias/1
{
  "mascotaId": 1,
  "fechaConsulta": "2024-12-10T14:30:00",
  "diagnostico": "Otitis moderada - Actualizado",
  "sintomas": "Rascado excesivo de orejas, enrojecimiento",
  "tratamiento": "Gotas óticas + Antibiótico oral",
  "observaciones": "Mejoría después de 3 días de tratamiento",
  "pesoRegistrado": 26.0,
  "temperatura": 38.2
}
```
*Nota: Reemplaza el `1` en la URL por el ID de la historia que deseas modificar*

### 6. Crear Factura
```json
POST /api/facturas
{
  "propietarioId": 1,
  "mascotaId": 1,
  "observaciones": "Consulta y medicamentos",
  "detalles": [
    {
      "descripcion": "Consulta veterinaria",
      "cantidad": 1,
      "precioUnitario": 50000
    },
    {
      "descripcion": "Amoxicilina 250mg",
      "cantidad": 2,
      "precioUnitario": 25000
    }
  ]
}
```

## 🐳 Comandos Docker Útiles

```bash
# Levantar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Detener servicios
docker-compose down

# Reconstruir después de cambios
docker-compose up --build

# Acceder a MySQL
docker exec -it vetapp-mysql mysql -uroot -proot vetapp
```

## 🚢 Despliegue en Railway

1. Crear cuenta en [Railway.app](https://railway.app)
2. Conectar repositorio de GitHub
3. Railway detectará automáticamente Docker
4. Agregar servicio MySQL desde marketplace
5. Configurar variables de entorno en Railway
6. Deploy automático

## 📁 Estructura de Archivos

```
vetapp/
├── src/
│   └── main/
│       ├── java/com/veterinaria/
│       │   ├── modules/
│       │   ├── shared/
│       │   ├── config/
│       │   └── VetAppApplication.java
│       └── resources/
│           └── application.yml
├── frontend/
│   ├── index.html
│   ├── styles.css
│   └── app.js
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── .env.example
└── README.md
```

## 🎯 Características Técnicas

- ✅ Arquitectura limpia y modular
- ✅ Separación de responsabilidades
- ✅ Patrones de diseño aplicados
- ✅ Validación de datos con Bean Validation
- ✅ Manejo global de excepciones
- ✅ DTOs para seguridad
- ✅ Transacciones con @Transactional
- ✅ Logging con SLF4J
- ✅ CORS habilitado para desarrollo
- ✅ Dockerización completa
- ✅ Notificaciones asíncronas

## 📝 Notas Importantes

1. **Gmail**: Requiere "Contraseña de aplicación", no tu contraseña normal
2. **MySQL**: Los datos persisten en volumen Docker
3. **Puertos**: 8080 (API) y 3306 (MySQL) deben estar libres
4. **Frontend**: Es básico, enfocado en probar funcionalidad

## 👨‍💻 Desarrollo

```bash
# Compilar sin Docker
./mvnw clean package

# Ejecutar tests
./mvnw test

# Ejecutar localmente
./mvnw spring-boot:run
```

## 📞 Soporte

Para problemas o dudas, revisa:
- Los logs con `docker-compose logs -f`
- Que MySQL esté corriendo
- Que las credenciales de email sean correctas
- Que los puertos no estén ocupados

---

**Desarrollado con arquitectura modular y patrones de diseño** 🚀