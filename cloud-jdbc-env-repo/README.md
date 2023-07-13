# Spring Cloud: JDBCEnvironmentRepository Sample Application
Sample of [Spring Cloud Config Server][2] embedded application that uses database as backend for configuration properties.

## Background
[Spring Cloud Config Server][2] provides several options to store configuration for an application. In general it is handled
by [Environment Repository][3].

Available options are git, file system, vault, svn, and database. This application demonstrates usage of [JdbcEnvironmentRepository][1]
which allows an application to store its configuration in database.

## Configuration
In order to enable this feature we will include `spring-boot-starter-jdbc` as one of the dependencies for the application and
include `jdbc` as one of its active profiles.

### Include JDBC as dependency
This can be seen in [build.gradle](build.gradle).

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
```

### Include `jdbc` as active profile
This can be seen in [bootstrap.yml][4].

```yaml
spring:
  profiles:
    active: jdbc
```

## Creating table and populating data
By default the [JdbcEnvironmentRepository][1] will look into a table called `PROPERTIES` which contains the following columns:

- KEY
- VALUE
- APPLICATION
- PROFILE
- LABEL

### Create table schema
Schema to create the table can found in [init-script.sql][6]:

```sql
CREATE TABLE `PROPERTIES` (
    `KEY` VARCHAR(128),
    `VALUE` VARCHAR(128),
    `APPLICATION` VARCHAR(128),
    `PROFILE` VARCHAR(128),
    `LABEL` VARCHAR(128),
    PRIMARY KEY (`KEY`, `APPLICATION`, `LABEL`)
);
```

In the script above KEY, APPLICATION, PROFILE, and LABEL are marked as composite key in order to avoid duplicated entry.

### Populate table
For this demonstration will we have a configuration called `app.greet.name` and this will be populated upon start-up.
Its script can be found in [init-script.sql][6].

```sql
INSERT INTO PROPERTIES (`APPLICATION`, `PROFILE`, `LABEL`, `KEY`, `VALUE`) VALUES ('demo', 'default', 'master', 'app.greet.name', 'Demo');
```

The script above explains that the configuration `app.greet.name` belongs to:

- an application called _demo_
- with profile called _default_
- and labelled _master_

## Configure Application Properties
In order for [JdbcEnvironmentRepository][1] to retrieve properties for this application it will need to be informed on
its name, profile, and label. This configurations can be found in [bootstrap.yml][4]

```yaml
spring:
  application:
    name: demo
``` 

We are not configuring `spring.cloud.profile` because its default value is `default`.

## Create Bootstrap Application Context
Finally, we will need to inform Spring Cloud on what are the classes needed in order to build the
bootstrap application context. This can found in [spring.factories][8]:

```text
org.springframework.cloud.bootstrap.BootstrapConfiguration=\
  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
  org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
```

These two classes will help us to build [JdbcTemplate][10] which is needed to construct [JdbcEnvironmentRepository][9].

## Verify Configuration Properties
In order to ensure that the application will use configurations from database we will create same configuration in [application.yml][11]:

```yaml
app:
  greet:
    name: Default
```

## Configure Environment Properties Retrieval SQL
By default, [there are two SQL statements that are used to retrieve properties from database](https://github.com/spring-cloud/spring-cloud-config/blob/main/spring-cloud-config-server/src/main/java/org/springframework/cloud/config/server/environment/JdbcEnvironmentProperties.java#L30). 
However, these queries need to be modified to follow MySQL requirement and implemented in [bootstrap.yml][4]:

```yaml
spring:
  cloud:
    config:
      server:
        jdbc:
          sql: SELECT `KEY`, `VALUE` from PROPERTIES where APPLICATION=? and PROFILE=? and LABEL=?
          sql-without-profile: SELECT `KEY`, `VALUE` from PROPERTIES where APPLICATION=? and PROFILE='default' and LABEL=?
```

We will have [GreetResource][12] which will retrieve the value of `app.greet.name` from [GreetProperties][14].

```java
@RestController
class GreetResource {

    private final GreetProperties properties;

    GreetResource(GreetProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam String greeting) {
        return String.format("%s, my name is %s", greeting, properties.name());
    }

}
```

Next we will have [CloudJdbcEnvRepoApplicationTests][13] class that verifies that the value for `app.greet.name` is **Demo** and not **Default**:

```java
@Testcontainers
@SpringBootTest(properties = "spring.datasource.url=jdbc:tc:mysql:8:///test?TC_INITSCRIPT=init-script.sql", webEnvironment = RANDOM_PORT)
class CloudJdbcEnvRepoApplicationTests {

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8");

    @Autowired
    private TestRestTemplate restClient;

    @Test
    @DisplayName("Given app.greet.name is configured to Demo in the database When I call greet Then I should get Hello, my name is Demo")
    void greet() {
        var response = restClient.getForEntity("/greet?greeting={0}", String.class, "Hello");

        assertThat(response.getBody()).isEqualTo("Hello, my name is Demo");
    }

}
```

By executing `greet()` we verify that the returned response is **Hello, my name is Demo** and not **Hello, my name is Default**.

[1]: https://cloud.spring.io/spring-cloud-config/single/spring-cloud-config.html#_jdbc_backend
[2]: https://cloud.spring.io/spring-cloud-config/single/spring-cloud-config.html#_spring_cloud_config_server
[3]: https://cloud.spring.io/spring-cloud-config/single/spring-cloud-config.html#_environment_repository
[4]: src/main/resources/bootstrap.yml
[6]: src/test/resources/init-script.sql
[8]: src/main/resources/META-INF/spring.factories
[9]: https://github.com/spring-cloud/spring-cloud-config/blob/master/spring-cloud-config-server/src/main/java/org/springframework/cloud/config/server/environment/JdbcEnvironmentRepository.java
[10]: https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html
[11]: src/main/resources/application.yml
[12]: src/main/java/zin/rashidi/boot/cloud/jdbcenvrepo/greet/GreetResource.java
[13]: src/test/java/zin/rashidi/boot/cloud/jdbcenvrepo/CloudJdbcEnvRepoApplicationTests.java
[14]: src/main/java/zin/rashidi/boot/cloud/jdbcenvrepo/greet/GreetProperties.java
