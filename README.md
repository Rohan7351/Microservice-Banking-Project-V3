<h3 align="center">Mini Bank System</h3>

<p align="center">
    The project showcase for microservice position.
    <br />
    <a href="#"><strong>Learn more »</strong></a>
    <br />
    <br />
    <a href="#Minibank-Microservice"><strong>Introduction</strong></a> ·
    <a href="#Tech-Stack"><strong>Tech Stack</strong></a> ·
    <a href="#overview"><strong>Overview</strong></a> ·
    <a href="#contributing-me"><strong>Contributing</strong></a>
</p>

<p align="center">
  <a href="https://www.linkedin.com/in/rohan-rana-228a2b227/">
    <img src="https://logos-world.net/wp-content/uploads/2020/05/Linkedin-Logo.png?style=flat&label=%40anguyenit98&logo=twitter&color=0bf&logoColor=fff" alt="Linkedin" />
  </a>

</p>

<br/>

# Minibank Microservice

A project showcase for applying Microservice position

## Contents

- [Tech stack](#Tech-Stack)
- [Overview](#Overview)
- [Setup](#Setup)
- [API guide](#API-guide)
- [UTR](#Unit-test-result)

## Tech Stack

- **Server:** Spring cloud gateway, Spring config, Eureka

- **Async comunication**: Spring cloud functions, Spring cloud stream, RabbitMQ, Kafka

- **Resilience**: Resilience4j (Circuit breaker, Retry pattern)

- **Observability and Monitoring**: Grafana, Loki, Alloy, Open telemetry

## Overview



## Setup

### Docker compose

Inside [docker-compose](docker-compose) run:

```
docker compose up -d
```

### Start RabbitMQ server

```
docker run -d -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
```
