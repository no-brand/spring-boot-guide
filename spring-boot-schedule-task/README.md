# Scheduling Tasks
스케줄링 작업을 할 수 있는 방법을 확인합니다.

## Create Project
```bash
$ spring init -d=web -g=com.nobrand -a=spring-boot-schedule-task spring-boot-schedule-task
```

## Scheduled Task 생성
`@Scheduled` annotation 을 사용해서 Scheduled Task 의 함수를 정의합니다.<br>
해당 Class 는 `@Component` annotation 을 통해서 Bean 으로 인식하도록 합니다. (singleton) <br>
Scheduling 과 관련된 조건을 설정하기 위한 다양한 옵션들이 존재합니다.<br>

|option    |description                                                         |
|----------|--------------------------------------------------------------------|
|fixedRate |시작시점(invocation) 사이의 간격을 정의                             |
|fixedDelay|종료시점(completion) 에서 새로운 호출(invocation) 사이의 간격을 정의|
|cron      |조건을 정규식으로 정의                                              |

```java
package org.springframework.scheduling.annotation;

@Documented
@Repeatable(Schedules.class)
public @interface Scheduled {
    String CRON_DISABLED = "-";
    String cron() default "";
    long fixedDelay() default -1L;
    long fixedRate() default -1L;
    long initialDelay() default -1L;
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
```
`@Component` annotation 에 이름을 부여하는게 아니면 (value = ), 클래스명의 camelCase 가 Bean ID 로 지정됩니다.
```json
{
   contexts: {
      application: {
         beans: {
            scheduledTask: {
               aliases: [ ],
               scope: "singleton",
               type: "com.nobrand.springbootscheduletask.ScheduledTask",
               resource: "URL [jar:file:...!/BOOT-INF/classes!/com/nobrand/springbootscheduletask/ScheduledTask.class]",
               dependencies: [ ]
            }
         }
      }
   }
}
```

## Scheduling 활성화
Standalone Application 으로 설정하는 방법을 확인합니다.<br>
`@SpringBootApplication` annotation 은 3개의 annotation 들을 한꺼번에 정의해주는 역할을 합니다.<br>
 - `@Configuration` : 정의한 Class 를, Application Context 의 Bean Definition 에 등록합니다.
 - `@EnableAutoConfiguration` : classpath 기준으로 Bean 들을 추가합니다.<br>
   - `org.springframework:spring-webmvc` 가 classpath 에 포함되어 있으면, `DispatcherServlet` 을 셋업하고
   - `org.apache.tomcat.embed:tomcat-embed-core` 가 classpath 에 포함되어 있으면, 톰캣서버가 세업됩니다.
 - `@ComponentScan` : 패키지명 (com.nobrand) 를 기준으로 components, configurations, services 를 찾아서, controller 가 찾을 수 있도록 합니다.

```java
// @SpringBootApplication
package org.springframework.boot.autoconfigure;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication { }


// @SpringBootConfiguration
package org.springframework.boot;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Indexed
public @interface SpringBootConfiguration { }
```
그리고 `@EnableScheduling` annotation 이 background executor 를 생성합니다. <br>

```java
package org.springframework.scheduling.annotation;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SchedulingConfiguration.class})
@Documented
public @interface EnableScheduling { }
```

## Execution
```bash
$ ./mvnw clean package
$ java -jar target/spring-boot-schedule-task-0.0.1-SNAPSHOT.jar

2021-10-13 14:11:02.809  INFO 2261 --- [           main] c.n.s.ScheduleTaskApplication            : Starting ScheduleTaskApplication v0.0.1-SNAPSHOT using Java 11.0.11
2021-10-13 14:11:02.811  INFO 2261 --- [           main] c.n.s.ScheduleTaskApplication            : No active profile set, falling back to default profiles: default
2021-10-13 14:11:03.483  INFO 2261 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-10-13 14:11:03.491  INFO 2261 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-10-13 14:11:03.491  INFO 2261 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.53]
2021-10-13 14:11:03.526  INFO 2261 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-10-13 14:11:03.527  INFO 2261 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 666 ms
2021-10-13 14:11:03.787  INFO 2261 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-10-13 14:11:03.793  INFO 2261 --- [   scheduling-1] c.n.s.ScheduledTask                      : 14:11:03
2021-10-13 14:11:03.795  INFO 2261 --- [           main] c.n.s.ScheduleTaskApplication            : Started ScheduleTaskApplication in 1.284 seconds (JVM running for 1.597)
2021-10-13 14:11:08.793  INFO 2261 --- [   scheduling-1] c.n.s.ScheduledTask                      : 14:11:08
2021-10-13 14:11:13.793  INFO 2261 --- [   scheduling-1] c.n.s.ScheduledTask                      : 14:11:13
2021-10-13 14:11:18.793  INFO 2261 --- [   scheduling-1] c.n.s.ScheduledTask                      : 14:11:18
2021-10-13 14:11:23.793  INFO 2261 --- [   scheduling-1] c.n.s.ScheduledTask                      : 14:11:23
```
Scheduled Task 는 background thread 에서 실행됩니다.

## Test
`@SpringBootTest` annotation 을 이용해서 유닛테스트를 작성합니다.

|Spring Boot version|Junit|필요한 annotations             |
|-------------------|-----|-------------------------------|
|v2.0               |4.x  |`@RunWith(SpringRunner.class)` |
|v2.1 이후          |5.x  |`@SpringBootTest`              |

JUnit 4.x 에서는 테스트 러너를 확장하는 방법으로 `@RunWith` 를 사용했습니다.<br>
JUnit 5.x 에서는 Extension 이란 일관된 방법으로 (`@ExtendWith`) 테스트 실행방법을 커스터마이징 합니다.
`@ExtendWith` 가 가지는 `@RunWith` 대비 장점은 다음과 같습니다.
 - Meta Annotation 지원 (`@SpringBootTest` annotation 이 `@ExtendWith(SpringExtension.class)` 를 내부적으로 선언)
 - 여러번 중복해서 사용 가능

```java
package org.springframework.boot.test.context;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ExtendWith({SpringExtension.class})
public @interface SpringBootTest { }
```

Mockito: 테스트 환경 (Database, API) 을 구성하는데 많은 수고가 들어가게 되는데, 이를 해결하기 위해 등장했습니다. <br>
 - `Mockito.mock`: Mock 객체 생성
 - `Mockito.when` 기대행위 정의 

Mockito 기본전략은 `Answer.RETURNS_DEFAULTS` 라서 Stub 되지 않은 메소드는 아무일없이 진행됩니다.

|annotation     |area             |description                                                                                                                                                                   |
|---------------|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|`@Mock`        |Mockito internal |`Mockito.mock` 대신 객체생성에 활용                                                                                                                                           |
|`@InjectMocks` |Mockito internal |`@Mock` 으로 만들어진 객체를 사용해서 객체를 생성할때 사용                                                                                                                    |
|`@Spy`         |Mockito internal |Stub 전략을 변경해서, Stub 되지 않은 메소드는 실제 구현된 내용을 사용                                                                                                         |
|`@MockBean`    |Spring Context   |`@Mock` 과 동일한 역할. Spring Context 에 mock 객체를 등록해서, @Autowired 동작 가능<br> - `@Mock` -> `@InjectMocks`<br> - `@MockBean` -> `@Autowired` (in `@SpringBootTest`) |
|`@SpyBean`     |Spring Context   |`@MockBean` 에서 Spy 개념만 (Mockito 기본전략, Stub 되지 않은 메소드는 실제 구현으로 동작하도록 함) 적용<br> 단, 실제 구현체가 반드시 Spring Context 에 등록되어 있어야 함    |

## Reference
1. [Guide, Scheduling Tasks](https://spring.io/guides/gs/scheduling-tasks/)
2. [Enable Scheduling Annotations](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-annotation-support)
3. [Mockito Annotation Description](https://cobbybb.tistory.com/16)