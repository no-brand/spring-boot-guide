# Aspect Oriented Programming with Spring
AOP 는 다른방식으로 프로그램의 구조를 생각할 수 있는 방법을 제시해서, OOP 를 만족시킵니다. <br>
 - `Weaving` (Advice 로직을 Target 에 적용하는 시기) : Spring 에서 제공하는 IoC, DI 를 이용해서 Runtime 에 수행
 - `Writing custom annotation`
   - [Schema based approach](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-schema)
   - [@AspectJ annotation style](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-ataspectj) 
     : `@Aspect` annotation 을 사용하는 방법

|type                             |key unit of modularity                                                            |
|---------------------------------|----------------------------------------------------------------------------------|
|OOP (Object Oriented Programming)|Class                                                                             |
|AOP (Aspect Oriented Programming)|Aspect (관심의 모듈화, 횡단 관심사, cross-cutting) <br> ex. transaction management|

## AOP Concepts
|terminology  |meaning                                                                                                          |
|-------------|-----------------------------------------------------------------------------------------------------------------|
|Aspect       |corss-cutting concern (횡단 관심사) 모듈화 <br> ex. transaction management                                       |
|Join Point   |어디에 적용할지 (적용지점) 를 정의 <br> Spring 에서는 method 에만 적용할 수 있음 (ex. 함수실행, Exception 핸들링)|
|Advice       |`Join Point` 에서 취할 Action 을 의미 <br> ex. around, before, after                                             |
|Pointcut     |`Advice` 와 함께 사용되는 expression <br> 어떤 `Join Point` 라도 이 expression (`Pointcut`) 과 매칭되면 실행되게 됨 <br> Spring 은 AspectJ pointcut expression 을 언어 자체적으로 지원|
|Target Object|부가기능이 부여된 객체로 Spring 에서는 Proxy 로 감싸진 객체를 의미                                               |
|AOP Proxy    |AOP framework 에 의해서 구현된 객체를 의미 <br> Spring 에서의 AOP Proxy : JDK dynamic proxy, CGLIB proxy         |
|Weaving      |Advice 로직을 Target 에 적용하는 시기 <br> Spring 에서는 Runtime 에 적용                                         |

## Advice
Advice 는 가장 specific 한 것을 선택하는게 잠재적인 에러를 보장해줄 수 있습니다. <br>
일반적으로 가장 많이 사용되는 Advice 는 `@Around` 입니다.

|type of advice   |meaning                                                                                             |
|-----------------|----------------------------------------------------------------------------------------------------|
|`@Before`        |Join Point 전에 실행. throw Exception 을 하지 않는 한, 다음단계 (Join Point) 로의 실행을 막을수 없음|
|`@AfterReturning`|Join Point 가 정상적으로 종료된 경우에 실행                                                         |
|`@AfterThrowing` |Join Point 가 비정상 종료된 경우에 실행                                                             |
|`@After`         |Join Point 가 정상/비정상 종료된 경우에 실행 (모든 경우)                                            |
|`@Around`        |Join Point 를 감싸는 역할 <br> Join Point 를 실행할지, 비정상 종료를 시킬지를 선택할 수 있음        |

Advice 에는 실행조건에 해당하는 `Pointcut` 이 필요합니다. 이는 annotation 이나 method 를 명시할 수 있습니다.
 - annotation : "@annotation(PACKAGE.AnnotationClass)"
 - method : "execution(* PACKAGE.Class.Method(..))"

```java
public class BaseController {
    @CheckAround(actions = {"action1"})
    @RequestMapping(value = "/")
    public String index() {
        log.info("/");
        return "index page";
    }
}

@Before(value = "@annotation(com.nobrand.springbootaop.aspect.CheckAround)")
public void checkBeforeAnnotation(JoinPoint joinPoint) {
    log.info("Before execution (annotation signature): " + Arrays.toString(getActions(joinPoint)));
}

@Before(value = "execution(* com.nobrand.springbootaop.controller.BaseController.index(..))")
public void checkBeforeMethod(JoinPoint joinPoint) {
    log.info("Before execution (method signature): " + Arrays.toString(getActions(joinPoint)));
}
```
```bash
2021-10-22 17:25:02.024  INFO 28829 --- [nio-8080-exec-1] c.n.s.aspect.CheckAroundAspect           : Before execution (annotation): [action1]
2021-10-22 17:25:02.024  INFO 28829 --- [nio-8080-exec-1] c.n.s.aspect.CheckAroundAspect           : Before execution (method signature): [action1]
2021-10-22 17:25:02.037  INFO 28829 --- [nio-8080-exec-1] c.n.s.controller.BaseController          : /
```

## Spring AOP
 - Spring AOP 는 순수 Java 로 구현되어 있어서 별도의 컴파일 단계나, Class Loader 가 필요없습니다. 
그래서 Servlet Container 나 Application Server 에서 사용하기 적합합니다.
 - Spring AOP 가 지원하는 Join Point 는 method 입니다. 그 외 적용이 필요하다면 `AspectJ` 를 고려해야 합니다.
 - Spring AOP 는 Spring IoC Container 와 함께 동작하고, 따라서 일반적인 `@Bean` 으로 정의합니다.

## AOP Proxies

## Reference
1. [Aspect Oriented Programming with Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-understanding-aop-proxies)