Kafka prototype in Java with Avro
=================================

A very simple demonstration of how to build a Kafka producer/consumer in Java
using Apache Avro for serialization.

* More up to date than the other tutorials I found.
* Based off the official Javadocs rather than a random (old) tutorial.
* See the POM file for the exact Kafka version compatibility.

Prerequisites
-------------

* Java 8
* Maven

Getting started
---------------

    mvn package
    docker-compose up
    java -jar consumer/target/consumer-1.0-SNAPSHOT-jar-with-dependencies.jar
    java -jar producer/target/producer-1.0-SNAPSHOT-jar-with-dependencies.jar

Resources
---------

* [Building a runnable jar with Maven 2](https://stackoverflow.com/questions/2022032/building-a-runnable-jar-with-maven-2) - an excellent and concise description of how to build a runnable JAR file.
* [KafkaConsumer](https://kafka.apache.org/22/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html) - Javadocs for the KafkaConsumer class
* https://docs.confluent.io/current/schema-registry/serializer-formatter.html
* [Confluent Platform and Apache Kafka Compatibility](https://docs.confluent.io/current/installation/versions-interoperability.html)
