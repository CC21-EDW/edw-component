![Build Status](https://github.com/CC21-EDW/edw-component/workflows/CI/badge.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/72c79a99b2c34d92b3fc495f5a455735)](https://www.codacy.com/gh/CC21-EDW/edw-component/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CC21-EDW/edw-component&amp;utm_campaign=Badge_Grade)
[![DepShield Badge](https://depshield.sonatype.org/badges/CC21-EDW/edw-component/depshield.svg)](https://depshield.github.io)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CC21-EDW_edw-component&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=CC21-EDW_edw-component)

# edw-component
Base component used for service connectors providing base functionality
- Add dependency to library
```XML
<dependency>
    <groupId>com.baloise.open.edw</groupId>
    <artifactId>edw-component</artifactId>
    <version>0.1.1</version>
</dependency>
```
- Add Github package registry as repository
```XML
 <repository>
  <id>github</id>
  <name>GitHub CC21-EDW Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/CC21-EDW/edw-component</url>
  <releases><enabled>true</enabled></releases>
</repository>
```
- Verify to have a valid github token in maven settings in order to access resources
```XML
<server>
  <id>github</id>
  <username>githubUSer</username>
  <password>ghp_xxxxxxxxxxxxxxxxxxxxxxx</password>
</server>
`````

## Tech-Stack
see https://github.com/CC21-EDW/documentation/blob/main/adr/003-technology-stack.md

### Kafka
- [Kafka Java Client](https://docs.confluent.io/clients-kafka-java/current/overview.html)
- [API JavaDoc](https://docs.confluent.io/platform/current/clients/javadocs/javadoc/index.html)

#### Example for Consumer
```Java
class ConsumerTest extends Consumer {

    public ConsumerTest(Properties configProps, String topic, String clientId) {
      super(configProps, topic, clientId);
    }
    
    public static void main(String[] args) {
      new ConsumerTest(new Properties(), "CC21_EDW_2128", "myTestClient").run();
    }
    
    @Override 
    public void process(ConsumerRecord<String, String> record) {
        System.out.printf("consumed message %s:%s", record.key(), record.value());
    }
}
```

#### Example for Producer
```Java
final Producer producer = Producer.create(new Properties(), "CC21_EDW_2128", "stravaConnect");
producer.pushEvent("CC21_EDW_2128", "MyKey", "Hello World").get();
```

#### Unit-Testing
With respect to unit testing and CI build, [Testcontainer](https://www.testcontainers.org/test_framework_integration/junit_5/)
concept is used to run tests against kafka docker image (see https://www.testcontainers.org/modules/kafka/)

## Logging
In order to support the logging of correlation IDs, the slf4j log pattern needs to contain ```[%X{CORRELATION-ID}]```
```
<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - [%X{CORRELATION-ID}] %msg%n"/>
```

## Releasing

Run e.g. on main: `mvnw -B release:prepare`

Subsequently the GitHub action worksflow "create release" will pick up the published tag and release and deploy the artifacts in the Github package registry.
