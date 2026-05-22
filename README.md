
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
Esta interfaz es la que contiene el contrato para que las clases Eventos puedan implementar los mismos metodos sin necesidad que SmulationEngine sepa cual evento se esta ejecutando (Strategy), para su implementacion SimulationEngine crea una lista (List<SimularionEvent>) de la interfaz, mediante un ciclo for emulamos el recorrido de los diferemtes Eventos y con cada iteracion del for generamos un `Random().nextDouble()` cuyo valor es comparado con la probabilidad del evento obtenida con `state.getConfig()` del archivo `park.properties`, si el valor arrojado por el `nextDouble()` es menor lanzamos el evento con `event.execute()`.
### El metodo execute
```http
event.execute(state, rng)
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `state` | `ParkState` | **Estado**. Contiene todo el estado del parque|
| `rng` | `Random` | **Probabilidad**. El random creado que dispara `nextDouble()` para lanzar o no el evento|

### Clases que implementan SimulationEvent
Todos los eventos implementan la interfaz SimulationEvent (para el correcto funcionamiento de Strategy en SimulationEngine)
- ***BlackoutEvent:*** Ejecuta los eventos de apagones masivos, cuando este evento ocurre *PowerPlant* lanza ejecuta el `triggerFailure()` que agrega un gasto de 2000.0 al estado del parque asi como el registro del evento en la base de datos.
- ***DealHourEvent:*** Lanza la hora de ofertas, cuando se lanza este evento ocurre se aplica un descuento a las ventas de souvenirs en *CentralHub* y las entradas en *ArrivalZone*; la actualizacion del estado de las ofertas en `state ` con el metodo `setDealHourActive(true)` y por supuesto con el agregado del descuento mediante `setCurrentDoiscount(discount)` (este valor puede obtenerse desde las properties mediante `state.getConfig().getDouble(key, defaultValue) ` o ser ingresado directamente).
- ***DinosaurEscapeEvent:*** Ejecuta el evento escape de dinosaurio, cuando este evento ocurre se filtran los dinosaurios que esten  `IN_CLOUSURE` si hay alguno, se elige uno aleatoriamente limitando el `rng.nextInt()` con el `size()` del arreglo resultante de la filtracion de los dinosaurios en `state.getDinosaurs()` es decir de `List<Dinosaur> dinosaurInClousere` resultado del `filter()` del API Stream de Java. Teniendo el dinosaurio se lanza se lanza `escape()` de la clase **Dinosaur** para cambiar el estado del dinosaurio a `ESCAPED` un `rng.nextDouble()` el cual se evalua si el daño que hace es menor a lo obtenido al azar, si es menor se elige un turista de los presentes en `state.getTourist()` filtrando aquellos que esten `IN_PARK` y eligiendo uno al azar para ser marcado como `ATTACKED`.
- ***StormEvent:*** Ejecuta los eventos de tormentas, al ocurrir este evento todos los turistas en el parque salen de el con `recordVisit(type)`, ademas de ello se guarda el evento en la base de datos accediento mediante `state.getDb()` y la ejecucion del metodo `appendExpense(event)`.
- ***VehiculeFailureEvent:*** Este evento cambia el estado de un vehiculo `AVALIABLE` por `BROKEN` cuando es lanzando, reduciendo el numero de vehiculos disponibles cuando los tecnicos los necesiten al tratar de reparar la planta. Los vehiculos se reparan automaticamente `tick()` despues del numero de pasos configurados o steps del ciclo de *SimulationEngine*.

#### Metodos lanzados en los eventos
```http
plant.triggerFailure(db)
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `db` | `DatabaseService` | **Base de datos**. La referencia a la clase que permite la persistencia de datos, normalmente se recupera desde ParkState|
```http
setCurrentDoiscount(discount)
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `discount` | `double` | **Descuento**. El descuento ha aplicar en las entradas y souvenirs|
```http
IN_PARK, IN_CLOUSURE, ATTACKED, ESCAPED
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `IN_PARK` | `TouristStatus` | **ENUM Estado de turista**. El turista esta en el parque|
| `ATTACKED` | `TouristStatus` | **ENUM Estado de turista**. El turista fue atacado|
| `IN_CLOSURE` | `DinosaurStatus` | **ENUM Estado de dinosaurios**. El dinosaurios esta encerrado|
| `ESCAPED` | `DinosaurStatus` | **ENUM Estado de dinosaurios**. El dinosaurios esta suelto|
```http
recordVisit(type)
appendExpense(event)
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `type` | `String` | **Tipo**. Describe la razon de la salida|
| `event` | `ExpenseRecord` | **Record Gastos**. El record utilizado para modelar los gastos|


## Ejecucion y pruebas

Estos comando ejecutan las pruebas unitarias (lanzando de manera automatica el desarrollo de JaCoCo con un umbral del 65%).
Asi como la ejecucion del proyecto.
```bash
  mvn test
  mvn exec:java
```

    
