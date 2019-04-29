## _Simple Scala Examples_

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5bb417e18ece4b9e810c04f57f40f744)](https://app.codacy.com/app/zeab/simple-scala-examples?utm_source=github.com&utm_medium=referral&utm_content=zeab/simple-scala-examples&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/zeab/simple-scala-examples.svg?branch=master)](https://travis-ci.org/zeab/simple-scala-examples)

> Here is a collection of scala examples for various situation. They can be dockerized or pulled from dockerhub prebuilt. Use these as examples for building your own services of the same type or used them as already made buoys for manually testing intergration connection points.

#### _Simple Web Service_
> Very simple akka http web service

[Simple Web Service: Docker](https://hub.docker.com/r/zeab/simplewebservice)    
[Simple Web Service: Swagger](https://github.com/zeab/simple-scala-examples/blob/master/swaggers/simplewebservice/simple-web-service-swagger.yaml)

| Environment Variables       | Type         | Default        | Description                                    |
| --------------------------- |:------------:| :------------: | ---------------------------------------------- |
| WEB_SERVICE_HOST            | String       | 0.0.0.0        | Address this web service can be found at       |
| WEB_SERVICE_PORT            | Int          | 8080           | Port this web service can be found on          |

#### _Complex Web Service_
> Examples of some more complex web server situations like using actors inside the routes and custom routing
_TBD_
#### _Simple Kafka Producer_
> An application that send X number of messages per second to a specified kafka topic and logs the info to the console

[Simple Kafka Producer: Docker](https://hub.docker.com/r/zeab/simplekafkaproducer)

| Environment Variables       | Type         | Default        | Description                                    |
| --------------------------- |:------------:| :------------: | ---------------------------------------------- |
| KAFKA_ADDRESS               | String       | localhost:9092 | Full address of where kafka can be found       |
| KAFKA_PRODUCER_TOPIC        | String       | my-topic       | The topic this app pushes too                  |
| KAFKA_MESSAGES_PER_SECOND   | Int          | 1              | The amount of messages sent per second         |
| KAFKA_METRIC_PRODUCER_LABEL | String       | kafka.produce  | The front half of the UDP datagram             |
| KAFKA_METRIC_TAGS           | List[String] | {Blank}        | The tags for the last half of the UDP datagram |
| IS_UDP_CLIENT_EMIT          | Boolean      | false          | Turns emitting UDP metrics on or off           |
| UDP_CLIENT_HOST             | String       | localhost      | Host for UDP client to send too                |
| UDP_CLIENT_PORT             | Int          | 8125           | Port for UDP client to send too                |

#### _Simple Kafka Consumer_
> An application that consumes messages with the option to be throttled from a specified Kafka topic and consumer group

[Simple Kafka Consumer: Docker](https://hub.docker.com/r/zeab/simplekafkaconsumer)

| Environment Variables       | Type         | Default        | Description                                    |
| --------------------------- |:------------:| :------------: | ---------------------------------------------- |
| KAFKA_ADDRESS               | String       | localhost:9092 | Full address of where kafka can be found       |
| KAFKA_CONSUMER_TOPIC        | String       | my-topic       | The topic this app gets from                   |
| KAFKA_CONSUMER_GROUP        | String       | my-group       | The consumer group for kafka                   |
| KAFKA_METRIC_CONSUMER_LABEL | String       | kafka.consume  | The front half of the UDP datagram             |
| KAFKA_METRIC_TAGS           | List[String] | {Blank}        | The tags for the last half of the UDP datagram |
| IS_KAFKA_EARLIEST           | Boolean      | false          | Pull from earlist kafka message or not         |
| IS_UDP_CLIENT_EMIT          | Boolean      | false          | Turns emitting UDP metrics on or off           |
| UDP_CLIENT_HOST             | String       | localhost      | Host for UDP client to send too                |
| UDP_CLIENT_PORT             | Int          | 8125           | Port for UDP client to send too                |

#### _Simple Kafka Enricher_
> An application to pull from a kafka topic then enrich the data and push it into another topic

[Simple Kafka Enricher: Docker](https://hub.docker.com/r/zeab/simplekafkaenricher)

| Environment Variables       | Type         | Default           | Description                                    |
| --------------------------- |:------------:| :---------------: | ---------------------------------------------- |
| KAFKA_ADDRESS               | String       | localhost:9092    | Full address of where kafka can be found       |
| KAFKA_CONSUMER_TOPIC        | String       | my-topic          | The topic this app gets from                   |
| KAFKA_CONSUMER_GROUP        | String       | my-enriched-group | The consumer group for kafka                   |
| KAFKA_PRODUCER_TOPIC        | String       | my-enriched-topic | The topic this app pushes too                  |
| KAFKA_METRIC_CONSUMER_LABEL | String       | kafka.consume     | The front half of the UDP datagram             |
| KAFKA_METRIC_PRODUCER_LABEL | String       | kafka.produce     | The front half of the UDP datagram             |
| KAFKA_METRIC_TAGS           | List[String] | {Blank}           | The tags for the last half of the UDP datagram |
| IS_KAFKA_EARLIEST           | Boolean      | false             | Pull from earlist kafka message or not         |
| IS_UDP_CLIENT_EMIT          | Boolean      | false             | Turns emitting UDP metrics on or off           |
| UDP_CLIENT_HOST             | String       | localhost         | Host for UDP client to send too                |
| UDP_CLIENT_PORT             | Int          | 8125              | Port for UDP client to send too                |


