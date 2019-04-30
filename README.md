# Cachés y Bases de datos NoSQL - Caso Reserva de Boletos de Cine

A la aplicación 'Compra/reserva de boletos de cine con Websockets' se le agregará un caché para llevar de forma centralizada -y con un acceso rápido- el estado de los asientos de cada una de las salas facilitando su escalabilidad horizontal.

## Parte I
* Inicie la máquina virtual Ubuntu trabajada anteriormente, e instale el servidor REDIS siguiendo estas instrucciones, sólo hasta 'redis-cli'. Con esto, puede iniciar el servidor con 'redis-server'.

  * Nota: para poder hacer 'copy/paste' en la terminal (la de virtualbox no lo permite), haga una conexión ssh desde la máquina real hacia la virtual.

* Ejecute en la consola de REDIS los comandos provistos en el archivo redisCinemaInitialKeys.txt de manera que se tenga la información de varias funciones previamente cargadas.

* Agregue las dependencias requeridas para usar Jedis, un cliente Java para REDIS:

```xml
  <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>2.9.0</version>
    </dependency>
​
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.8.2</version>
    </dependency>
​
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.25</version>
    </dependency>  
```

* Copie las clases provistas en los fuentes a la aplicación en las siguientes rutas:

  * JedisUtil.java en /src/main/java/edu/eci/arsw/cinema/util/
  * jedis.properties en /src/main/resources

(Éstas ya tiene la configuración para manejar un pool de conexiones al REDIS)

##  Parte II

En la versión actual de la aplicación, en el método dentro del servidor que recibe los eventos, se tiene una lógica que considera -dentro de un bloque sincronizado-:

1.  Recibir el asiento a comprar.
2.  Realizar la compra en la respectiva función.
3.  Publicar la compra del asiento en el tópico correspondiente.

El esquema anterior sin embargo, SÓLO sirve cuando se tiene un único servidor. Cuando se tienen N bajo un esquema de balanceo de carga, evidentemente se pueden tener condiciones de carrera.

Para corregir esto, va a hacer uso de la base de datos Llave-valor REDIS, y su cliente correspondiente para Java Jedis:


```java
Jedis jedis = JedisUtil.getPool().getResource();
	    
	//Operaciones	    
	    
jedis.close();
```

Para facilitar el manejo de las operaciones sobre REDIS, crearemos diversos métodos utilizando el [API DE JEDIS](http://tool.oschina.net/uploads/apidocs/jedis-2.1.0/redis/clients/jedis/Jedis.html) en una clase llamada RedisMethods en el paquete 'util'.

1.  Método 'saveToREDIS(key,data)' para guardar un valor en una llave de REDIS, para esto debe

  * Crear un Objeto Jedis a través del JedisUtil
  * Iniciar una transacción REDIS, haciendo 'watch' sobre la llave.
  * Después crear una transacción sobre el objeto jedis
    * Transaction t1 = jedis.multi();
  * A la transacción asignar la respectiva llave y el nuevo valor
    * t1.set(key,data);
   * Y, ejecutar la transacción t1.exec();

2.  Puede comprobar la funcionalidad del método 'Save' ejecutándolo en una clase main independiente y enviándole una llave y un valor, posteriormente verifique en el terminal donde tiene abierta la interfaz de redis-cli si se registró la llave con su respectivo valor (Utilice el método [Get](https://redis.io/commands/getset) de redis).

3.  Método 'getFromREDIS(key)' Para a través de una llave su data correspondiente, para esto debe completar el siguiente código:
```java
public static String getFromREDIS(String key) {
    boolean intentar = true;
    String content = "";
    while (intentar) {
   	 // Inicializar jedis y obtener recursos
   	 // Hacer watch de la llave 
   	 // Crear la transacción t
   	 Response<String> data = t.get(key);
   	 List<Object> result = t.exec();
   	 if (result.size() > 0) {
   		intentar = false;
   		content = data.get();
   		//Cerrar recurso jedis	
   	 }
    }
   return content;
}
```

1.  Para este ejercicio sólo se centralizará la información de los asientos de las funciones, esto se hará registrando como llave la función (de la forma cinemaName + functionDate + functionMovieName) y como valor la matriz de asientos en formato JSON.

2. Ahora deberá crear un método 'buyTicketRedis' el cual retorne la matriz de booleanos asociada a la llave que representa a la función, recuerde que la matriz está guardada en como un string en formato JSON, es recomendable utilizar la librería 'jackson.databind' para la [conversión de JSON a objetos y viceversa.](https://www.mkyong.com/java/jackson-2-convert-java-object-to-from-json/)

3. Cree el método 'getSeatsRedis' el cual deberá al igual que 'buyTicketRedis' debe retornar la matriz de booleanos asociada a la llave que representa a la función pero sin modificarla, puede utilizar como parámetros de entrada el nombre del Cinema y la correspondiente CinemaFunction de modo que le sea útil como ayuda para formar la llave.

Ahora debe crear una nueva implementación de la clase CinemaPersitence pero llamada 'RedisCinemaPersistence', puede basarse en los mismos métodos de la clase 'InMemoryCinemaPersistence', pero se procederá a modificar algunos para que se consulté la información de las salas en REDIS.

1.  Edite el método 'buyTicket' de la clase 'RedisCinemaPersistence' de modo que utilice el método 'buyTicketRedis' creado anteriomente. No olvide asignarle a la clase CinemaFunction correspondiente una actualización de la matriz para que cuando el Front solicite los asientos, la información de estos esté los más actualizado posible.

2.  Edite el constructor de la clase 'RedisCinemaPersistence', para que la data stub que cargue sea actualizada con los datos más recientes del REDIS al momento de iniciar el servidor.
```java
CinemaFunction funct4 = new CinemaFunction(superheroes,functionDate2);
try {
   //LOAD DATA FROM REDIS
   funct1.setSeats(RedisMethods.getSeatsRedis("cinemaX",funct1));
   funct2.setSeats(RedisMethods.getSeatsRedis("cinemaX",funct2));
   funct3.setSeats(RedisMethods.getSeatsRedis("cinemaY",funct3));
   funct4.setSeats(RedisMethods.getSeatsRedis("cinemaY",funct4));
} catch (CinemaException ex) {
   Logger.getLogger(RedisCinemaPersistence.class.getName()).log(Level.SEVERE, null, ex);
}
functionsX.add(funct1);
```

1.  Ejecute la aplicación y compruebe que funciona de una manera correcta, después configure en otro equipo o máquina virtual otra aplicación de manera que ambas aplicaciones apunten al mismo servidor REDIS, verifique la funcionalidad.

## Nota - Error de SockJS
En caso de que con la configuración planteada (aplicación y REDIS corriendo en la máquina virtual) haya conflictos con SockJS pruebe configurar REDIS para aceptar conexiones desde máquinas externas, editando el archivo /home/ubuntu/redis-stable/redis.conf, cambiando "bind 127.0.0.1" por "bind 0.0.0.0", y reiniciando el servidor con:
```
redis-server /home/ubuntu/redis-stable/redis.conf
```
Una vez hecho esto, en la aplicación ajustar el archivo jedis.properties, poner la IP de la máquina virtual (en lugar de 127.0.0.1), y ejecutarla desde el equipo real (en lugar del virtual). **OJO: Esto sólo se hará como prueba de concepto!, siempre se le debe configurar la seguridad a REDIS antes de permitirle el acceso remoto!.**
