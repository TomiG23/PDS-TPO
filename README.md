# PDS-TPO (Minimal Java MVC)

Proyecto Java minimal con estructura MVC para subir a GitHub.

Estructura:
- src/main/java/com/example/model (modelos)
- src/main/java/com/example/view  (vistas)
- src/main/java/com/example/controller (controladores)
- src/main/java/com/example/Main.java (entry point)

Requisitos: Java 17+ y Maven

Cómo compilar y ejecutar (Windows cmd):

```cmd
mvn package
java -cp target\pds-tpo-1.0-SNAPSHOT.jar com.example.Main
```

O ejecutar desde Maven:

```cmd
mvn exec:java -Dexec.mainClass="com.example.Main"
```

Puedes personalizar `pom.xml` según necesites.

