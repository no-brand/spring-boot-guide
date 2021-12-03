# Spring Cloud Gateway
Spring WebFlux 위에서 API Gateway 를 만드는 라이브러리 입니다. <br>
손쉽게 API 들을 라우팅하고, Cross Cutting Concerns (Security, Monitoring/Metrics, Resiliency) 를 위한 방법을 제공합니다. <br>

 - Dependency : `spring-cloud-starter-gateway`
 - Documentation : [Spring Cloud Gateway 3.1.0 Reference Documentation](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
 - Samples
   - [Spring Guide: Building a Gateway](https://spring.io/guides/gs/gateway/)
   - [Github: spring-cloud-gateway/spring-cloud-gateway-sample](https://github.com/spring-cloud/spring-cloud-gateway/tree/main/spring-cloud-gateway-sample)
   - [Github: spring-cloud-samples/spring-cloud-gateway-sample](https://github.com/spring-cloud-samples/spring-cloud-gateway-sample)
 - Support: https://spring.io/projects/spring-cloud-gateway#support
   - 3.1.x : 2021-11-30 ~ 2024-08-22

## Features
1. Spring Framework 5, Reactor and Spring Boot 2 기반
2. Request Attributes 를 이용한 라우팅 가능
3. 각 라우팅 마다 Predicates, Filters 적용 가능
4. Circuit Breaker 적용 가능
5. Spring Cloud DiscoveryClient 적용 가능
6. Request Rate Limiting
7. Path Rewriting

## About Spring Cloud Gateway
Spring Cloud Gateway 는 Spring Boot 2.x, Spring WebFlux, Reactor, Netty 기반으로 구성되었습니다. <br>
따라서 동기화 (Synchronous) 컨셉의 동작과 차이가 있습니다. (예. Spring Data, Spring Security 등) <br>
WAR 로 빌드할 수 없으며, Servlet Container 와 차이를 보입니다. <br>
Q. 정확히 이 차이점을 구분하도록 합니다. <br>

 - Group ID : `org.springframework.cloud`
 - Artifact ID : `spring-cloud-starter-gateway`
 - 라이브러리 의존성을 추가하고 비활성화 하고 싶으면 `spring.cloud.gateway.enabled=false` 로 설정합니다.

Glossary
 - Route 
   - Gateway 의 기본 구성요소로, Aggregate Predicates 가 true 일때 매칭 (교집합)
   - 구성 : ID, Destination URI, Predicates 집합, Filters 집합   
 - Predicate
   - Java 8 의 Predicate (java.util.function.Predicate<T>)
     - 인풋타입 T = ServerWebExchange (org.springframework.web.server.ServerWebExchange)
     - HTTP request 의 headers, parameters 값을 매칭 가능하게 지원
 - Filter
   - GatewayFilter (org.springframework.cloud.gateway.filter.GatewayFilter) 의 객체
   - Request, Response 의 값을 변경할 수 있음
