# PetManager

Sistema de gestión de mascotas desarrollado en **Java + Swing** para Programación 1.

Permite administrar dueños, mascotas, veterinarias y donantes, con persistencia en base de datos MySQL vía JDBC.

## Requisitos

- **Java 17** (Zulu JDK)
- **MySQL 8.x** corriendo en `localhost:3306`
- MySQL Connector/J (`mysql-connector-j-*.jar`) en el classpath

## Relaciones OOP implementadas

| Relación | Entidades | Tipo |
|----------|-----------|------|
| Herencia | `Person` → `Owner`, `Donor` | Abstract class |
| Herencia | `Pet` → `Dog`, `Cat` | Abstract class |
| Agregación | `Owner` ↔ `Pet` | Bidireccional (muchos-a-muchos) |
| Composición | `Person` → `Address` | Unidireccional |
| Composición | `Vet` → `Address` | Unidireccional |
| Polimorfismo | `Pet`, `Person` | Métodos sobrescritos en subclases |

## Funcionalidades

- **Dashboard** — Estadísticas generales (total mascotas, perros, gatos, perdidas, fallecidas)
- **Dueños** — ABM de dueños con asignación de veterinaria preferida
- **Mascotas** — Listado con filtros (Todos/Perros/Gatos), detalle con foto, estado (Activa/Perdida/Fallecida)
- **Veterinarias** — Registro de clínicas veterinarias
- **Donantes** — Listado de contactos donantes
- **Adopciones** — Panel para adoptar mascotas disponibles

## Configurar la base de datos

1. Asegurate de que MySQL esté corriendo.

2. Ejecutá el script de schema:

```bash
mysql -u root -p < resources/schema.sql
```

Esto crea la base `petmanager` con todas las tablas:
- `persons` — Dueños y Donantes (discriminador `type`)
- `pets` — Mascotas con estado (`ACTIVA`/`PERDIDA`/`FALLECIDA`)
- `vets` — Veterinarias
- `owner_pet` — Relación muchos-a-muchos Owner↔Pet
- `owner_vet` — Relación Owner↔Vet con flag `is_preferred`

3. Configurá las credenciales en `resources/database.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/petmanager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=tu_password
db.driver=com.mysql.cj.jdbc.Driver
```

## Compilar y ejecutar

### Desde IntelliJ IDEA

1. Abrí el proyecto como módulo IntelliJ (`.iml`)
2. Agregá `mysql-connector-j-*.jar` como librería del módulo
3. Corré `PetManagerMain.java` o `PetManagerUI.java`

### Desde terminal

```bash
# Compilar
javac -cp "lib/mysql-connector-j-8.0.33.jar:." -d out src/ar/com/petmanager/**/*.java

# Ejecutar
java -cp "out:lib/mysql-connector-j-8.0.33.jar:resources" ar.com.petmanager.presentation.PetManagerMain
```

> **Nota**: Si no hay MySQL disponible, los servicios usan `ArrayList` en memoria como fallback.

## Estructura del proyecto

```
PetManager/
├── resources/
│   ├── database.properties      # Configuración de conexión JDBC
│   └── schema.sql               # DDL para MySQL
├── src/ar/com/petmanager/
│   ├── assets/images/           # Imágenes de UI y background
│   ├── domain/                  # Entidades (Owner, Pet, Vet, Donor, Address)
│   ├── service/                 # Lógica de negocio (CRUD services)
│   ├── data/                    # DataAccess (fachada JDBC)
│   ├── persistence/             # DAOs y DBConnector (JDBC)
│   ├── presentation/            # Punto de entrada
│   └── gui/
│       ├── constants/           # UIConstants (colores, fuentes, dimensiones)
│       ├── base/                # BasePanel con background
│       ├── utils/               # ImageUtils
│       ├── homeUI/              # Dashboard principal
│       ├── ownerUI/             # Gestión de dueños
│       ├── petUI/               # Listado y detalle de mascotas
│       ├── vetUI/               # Gestión de veterinarias
│       ├── donorsUI/            # Contacto donantes
│       └── adoptionUI/          # Panel de adopciones
└── db/                          # (opcional) scripts adicionales
```

## Estados de mascotas

| Estado | Descripción |
|--------|-------------|
| Activa | La mascota está presente y en buen estado |
| Perdida | La mascota está extraviada |
| Fallecida | La mascota ha fallecido |

El estado se puede cambiar desde el panel de detalle de cada mascota.

## Licencia

Proyecto académico — Programación 1.