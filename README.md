
# Laboratorio 360

Este labororatorio busca aplicar los conocimientos del desarrollo en Java, mediante el uso de herramientas y tecnologias.


## Tecnologias

**Test:** JaCoCo, JUnit

**Desarrollo:** Java, Maven

**Persistencia:** H2, Liquibase


## Clase ParkConfig

Este clase es la que permite al proyecto acceder a toda la informacion que necesita en cada una de las clases que componen el proyecto. Usa un singlento para obtener la instancia de la clase ParkConfig y asi acceder al arhivo .properties.

`PARK.PROPERTIES`



## Quienes utilizan ParkConfig

- SimulatorEngine (el motor de simulacion)
- ParkState (esta clase contiene la referencia general de ParkConfig)
- SimulationEvent (todas las **clases que implementa esta interfaz**, con el unico proposito de guardar una referencia de las actualizacion en las **revenues** & **expenses**)
- ParkMonitor (como ParkState contiene el estado del parque, monitor lo plasma)


## Ejecucion y pruebas

Estos comando ejecutan las pruebas unitarias (lanzando de manera automatica el desarrollo de JaCoCo con un umbral del 65%).
Asi como la ejecucion del proyecto.
```bash
  mvn test
  mvn exec:java
```

    
