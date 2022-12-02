# virtual-intercom

Integrantes
- Daniel Valencia
- Juan Camilo Bernal
- Juan Pablo Uribe

## Procedimiento

El cliente requiere un programa de citófono para un edificio de dos apartamentos.
Este debe tener una terminal en portería donde se puede anunciar un visitante por el nombre y el apartamento al que se dirige.
También tiene una terminal en cada apartamento donde se tiene un chat entre los mismos,
admitir o negar el ingreso de un visitante anunciado y pulsar un botón de pánico que envie un correo
electrónico a una persona elegida previamente e informe al portero.

Para facilitar la conexión inicial, se abre una conexión en los puertos 8888 tanto en el cliente como en el servidor para
que se pueda obtener la dirección IP del servidor sin necesidad de tener que introducirla manualmente. Este procedimiento es por UDP

Para dar solución al problema anterior se realizó un programa mediante el lenguaje de programación java. Fue necesario
identificar las clases que se ven involucradas: La portería que será usada como servidor además de sus funciones solicitadas.
Los apartamentos quienes usarán TCP para el funcionamiento del chat; SMTP para el botón de pánico y correo. En tercer lugar, se ve la necesidad
de asignar una clase independiente que se encarga de conectar el modelo con las interfaces gráficas. Finalmente, para las interfaces gráficas
se empleó JavaFX, se necesitó de (3) pantallas, una para seleccionar si es el apartamento "A01", "A02" 0 "Portería", otra para cada apartamento (Comparten la misma interfaz)
y una para el control de la Portería.

## Dificultades

Se encontró dificultad en el chat. Esto fue causado por el código de TCP en donde se utiliza el mismo puerto tanto para escuchar como para enviar, lo que no permite lograr
el correcto funcionamiento. Otro lugar donde se encontraron dificultades fue para enviar una alerta de visitante a los apartamentos. Este problema se presentaba
por la forma en como se manejaba el chat entre apartamentos, donde los puertos por turnos eran de escucha o escritura, causando que, al realizar
algún cambio, este orden se modifica y genera errores en la comunicación.

## Soluciones

Para chat entre apartamentos fue necesario modificar el total de puertos utilizados en el código de TCP. Esto permitió que el chat dejara de ser por turnos a ser
más realista. El tener un puerto de escuchar y otro para enviar por cada apartamento (en total cuatro puertos) permite que uno de los interlocutores mande más de un mensaje en lugar de tener
que esperar a la respuesta. Esta misma acción solucionó el problema de la alerta por visitante; por lo que se tiene un puerto que esta recibiendo todo el tiempo,
este se utiliza para comunicar al residente de un visitante. Se identifica con la primera letra del mensaje que es una alerta, "V" de visitante que lo diferencia de
los demás ("A" de apartamento y "P" de pánico). Lo anterior también es utilizado para enviar la alerta de pánico desde un apartamento a la portería.
