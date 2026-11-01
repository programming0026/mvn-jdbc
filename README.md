# CRUD con JDBC y H2

Aplicación de consola que implementa el CRUD completo de personas usando JDBC puro y una base de datos H2 embebida; no necesita instalar ningún servidor.

## Características

- Crear, listar, buscar por ID, actualizar y eliminar personas.
- Consultas parametrizadas (`PreparedStatement`) contra inyección SQL.
- Patrón DAO (`PersonDao`) y DTO (`Person`).
- Datos de ejemplo cargados automáticamente la primera vez.
- Empaquetable en un JAR ejecutable (`maven-shade-plugin`).

## Requisitos

- JDK 21 y Maven 3.9 o superior.

## Compilar y ejecutar

```bash
mvn clean package
java -jar target/mvn-jdbc-1.0-SNAPSHOT-launcher.jar
```

## Notas

- La base de datos se guarda en el archivo `crud_db.mv.db` dentro de tu carpeta de usuario (`~`). Bórralo para empezar de cero.
- El correo electrónico es único: no se pueden crear dos personas con el mismo.

## Estructura

```text
src/main/java/com/juniverse/jdbc/
├── Main.java            # prepara la base de datos y abre el menú
├── PersonMenu.java      # menú de consola: depende de PersonRepository, no de JDBC
├── dao/
│   ├── PersonRepository.java  # contrato de acceso a datos
│   └── PersonDao.java         # implementación con JDBC y H2
├── dto/                 # Person
└── util/                # DatabaseUtil
```

## Pruebas

```bash
mvn test
```

El menú se prueba con una implementación en memoria de `PersonRepository` y una entrada simulada, sin base de datos.

## Licencia

Código de ejemplo de uso libre con fines educativos.
