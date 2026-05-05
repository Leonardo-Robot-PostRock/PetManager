# PetManager

Sistema de gestión de mascotas desarrollado en **Java + Swing** para Programación 1.

Permite administrar dueños, mascotas, veterinarias y donantes, con persistencia en base de datos MySQL vía JDBC.

## Requisitos

- **Java 17** (Zulu JDK)
- **MySQL 8.x** corriendo en `localhost:3306`
- MySQL Connector/J `8.2.0` — incluido en `lib/mysql-connector-j-8.2.0.jar`

## Relaciones OOP implementadas

| Relación | Entidades | Tipo |
|----------|-----------|------|
| Herencia | `Person` → `Owner`, `Donor` | Abstract class |
| Herencia | `Pet` → `Dog`, `Cat` | Abstract class |
| Agregación | `Owner` ↔ `Pet` | Bidireccional (muchos-a-muchos) |
| Asociación | `Owner` → `Vet` | Unidireccional (muchos-a-uno, preferred_vet) |
| Composición | `Person` → `Address` | Unidireccional |
| Composición | `Vet` → `Address` | Unidireccional |
| Polimorfismo | `Pet`, `Person` | Métodos sobrescritos en subclases |

## Funcionalidades

- **Dashboard** — Estadísticas (total mascotas, perros, gatos, dueños, veterinarias, perdidas, fallecidas, en adopción)
- **Dueños** — ABM con sexo (Masculino/Femenino), veterinaria preferida y detalle con resumen de mascotas
- **Mascotas** — ABM con filtros, detalle editable (edad, peso, salud, estado), asignación de dueño, dar en adopción
- **Veterinarias** — ABM de clínicas veterinarias
- **Donantes** — ABM de contactos donantes con búsqueda
- **Adopciones** — Panel para adoptar mascotas sin dueño, con selector de dueño en card

## Configurar la base de datos

1. Asegurate de que MySQL esté corriendo.

2. Ejecutá el script de schema:

```bash
mysql -u root -p < resources/schema.sql
```

Esto crea la base `petmanager` con todas las tablas:
- `persons` — Dueños y Donantes con discriminador `type`, sexo (`MASCULINO`/`FEMENINO`) y `preferred_vet_id` (FK a `vets`)
- `pets` — Mascotas con edad textual (`VARCHAR`), peso decimal y estado (`ACTIVA`/`PERDIDA`/`FALLECIDA`)
- `vets` — Veterinarias
- `owner_pet` — Relación muchos-a-muchos Owner↔Pet

3. Configurá tus credenciales:

```bash
cp resources/database.properties.example resources/database.properties
```

Editá `resources/database.properties` con tu usuario y contraseña de MySQL.

## Compilar y ejecutar

### Desde IntelliJ IDEA

1. Abrí el proyecto como módulo IntelliJ (`.iml`)
2. El JAR de MySQL Connector/J ya está en `lib/` — IntelliJ lo detecta automáticamente vía el `.iml`
3. Corré `PetManagerMain.java`

### Desde terminal

```bash
# 1. Compilar
javac -cp "lib/mysql-connector-j-8.2.0.jar:." -d out \
    src/ar/com/petmanager/**/*.java

# 2. Copiar recursos al directorio de salida
cp -r src/ar/com/petmanager/assets out/ar/com/petmanager/
cp resources/database.properties out/

# 3. Ejecutar (out + resources en el classpath)
java -cp "out:lib/mysql-connector-j-8.2.0.jar" \
    ar.com.petmanager.presentation.PetManagerMain
```

> **Nota**: Si no hay MySQL disponible, los servicios usan `ArrayList` en memoria como fallback.

## Estructura del proyecto

```
PetManager/
├── lib/
│   └── mysql-connector-j-8.2.0.jar  # Driver JDBC incluido en el repo
├── resources/
│   ├── database.properties      # Configuración de conexión JDBC
│   └── schema.sql               # DDL para MySQL
├── src/ar/com/petmanager/
│   ├── assets/images/           # Imágenes de UI y background
│   ├── domain/                  # Entidades (Owner, Pet, Vet, Donor, Address, Sex)
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

El estado, edad, peso y condición de salud se pueden editar desde el botón **Editar** en el panel de detalle.

## Licencia

Proyecto académico — Programación 1.