
# Laboratorio 360

Este labororatorio busca aplicar los conocimientos del desarrollo en Java, mediante el uso de herramientas y tecnologias.


## Tecnologias

**Test:** JaCoCo, JUnit

**Desarrollo:** Java, Maven

**Persistencia:** H2, Liquibase


## Clase ParkConfig

Este clase es la que permite al proyecto acceder a toda la informacion que necesita en cada una de las clases que componen el proyecto. Usa un singlento para obtener la instancia de la clase ParkConfig y asi acceder al arhivo .properties.

`PARK.PROPERTIES`



### Quienes utilizan ParkConfig

- *SimulatorEngine* (el motor de simulacion)
- *ParkState* (esta clase contiene la referencia general de ParkConfig)
- *SimulationEvent* (todas las **clases que implementa esta interfaz**, con el unico proposito de guardar una referencia de las actualizacion en las **revenues** & **expenses**)
- *ParkMonitor* (como ParkState contiene el estado del parque, monitor lo plasma)

## SimulationEvent
Esta interfaz es la que contiee el contrato para que las clases Eventos puedas implementar los mismos metodos sin necesidad que SmulationEngine sepa cual evento se esta ejecutando (Strategy), para su implementacion SimulationEngine crea una lista (List<SimularionEvent>) de la interfaz, mediante un ciclo for emulamos el recorrido de los diferemtes Eventos y con cada iteracion del for generamos un `Random().nextDouble()` cuyo valor es comparado con la probabilidad del evento obtenida con `state.getConfig()` del archivo `park.properties`, si el valor arrojado por el `nextDouble()` es menor lanzamos el evento con `event.execute()`.
### El metodo execute
```http
event.execute(state, rng)
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `state` | `ParkState` | **Estado**. Contiene todo el estado del parque|
| `rng` | `Random` | **Probabilidad**. El random creado que dispara `nextDouble()` para lanzar o no el evento|

### Clases que implementan SimulationEvent
Todos los evento implementan la interfaz SimulationEvent (para el correcto funcionamiento de LSP en SimulationEngine)
- *BlackoutEvent:* Ejecuta los eventos de apagones masicos, cuando este evento ocurre *PowerPlant* lanza ejecuta el `triggerFailure()` que agrega un gasto de 2000.0 al estado del parque asi como el registro del evento en la base de datos.
- *DealHourEvent:* Lanza la hora de ofertas, cuando se lanza este evento ocurre se aolica un descuento a las ventas aplicadas en *ArrivalZone*; la actualizacion del estado de las ofertas en `state ` con el metodo `setDealHourActive(true)` y por supuesto con el agregado del descuento mediante `setCurrentDoiscount(0.30)` (este valor puede obtenerse desde las properties mediante `state.getConfig().getDouble(key, defaultValue) `).

#### Metodos lanzados en los eventos
```http
plant.triggerFailure(db)
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `db` | `DatabaseService` | **Base de datos**. La referencia a la clase que permite la persistencia de datos, normalmente se recupera desde ParkState|

## Ejecucion y pruebas

Estos comando ejecutan las pruebas unitarias (lanzando de manera automatica el desarrollo de JaCoCo con un umbral del 65%).
Asi como la ejecucion del proyecto.
```bash
  mvn test
  mvn exec:java
```

    
