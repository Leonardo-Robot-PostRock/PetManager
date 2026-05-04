# TODO — PetManager

---

## 1. Atributo `sex` en Person (domain + DB)

### 1.1 Domain
- [ ] Crear enum `Sex` (`MASCULINO`, `FEMENINO`) en `domain/`
- [ ] Agregar `private Sex sex;` + getter/setter en `Person.java`
- [ ] Actualizar constructor de `Person` para recibir `Sex`
- [ ] Actualizar constructores de `Owner` y `Donor` (pasan `sex` a `super()`)

### 1.2 Schema
- [ ] Agregar columna `sex ENUM('M', 'F') NOT NULL` en `persons` (resources/schema.sql)
- [ ] Nota: requiere recrear la DB (`DROP DATABASE petmanager`)

### 1.3 Persistence
- [ ] `OwnerDAO.create()` / `update()` — incluir `sex` en INSERT/UPDATE
- [ ] `DonorDAO.create()` / `update()` — incluir `sex` en INSERT/UPDATE
- [ ] `OwnerDAO.mapResultSetToOwner()` — cargar `sex` desde `ResultSet`
- [ ] `DonorDAO.mapResultSetToDonor()` — cargar `sex` desde `ResultSet`

### 1.4 UI
- [ ] `OwnerUI` — agregar combo `Sex` en el formulario
- [ ] `ContactDonorsUI` — agregar combo `Sex` en el formulario

---

## 2. Detalle de Dueño (OwnerDetailPanel)

- [ ] Crear `OwnerDetailPanel` (similar a `PetDetailPanel`) con:
  - Información completa del dueño (nombre, dni, teléfono, dirección, vet preferida)
  - **Cantidad de mascotas**: `owner.getPets().size()`
  - **Tipos**: contar `Dog` vs `Cat` en `owner.getPets()`
  - **Nombres**: listar nombres de las mascotas
- [ ] Botón "Ver Detalle" en `OwnerUI` → abre `OwnerDetailPanel`
- [ ] Navegación con `CardLayout` (lista ↔ detalle)

---

## 3. Dueño en Detalle de Mascota (PetDetailPanel)

- [ ] En `PetDetailPanel`, mostrar dueños de la mascota:
  - Si el dueño tiene `sex == FEMENINO` → mostrar "Mamá: Nombre Apellido"
  - Si el dueño tiene `sex == MASCULINO` → mostrar "Papá: Nombre Apellido"
  - Si hay múltiples dueños, listar todos con su prefijo correspondiente
- [ ] Agregar sección de dueños en `createInfoSection()` o como panel separado

---

## Orden sugerido de implementación

1. `Sex` enum + `Person` domain
2. Schema + DAOs (persistence)
3. UI forms (OwnerUI, DonorsUI)
4. `OwnerDetailPanel`
5. Dueños en `PetDetailPanel`
