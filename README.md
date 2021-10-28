# Reactor Netty / R2DBC Pool Connection Leak
This demo shows how connection leaks can be provoked, when using spring boot webflux on reactor netty with spring boot data r2dbc + postgres driver.

## Prerequisites
- Install postgres: https://hub.docker.com/_/postgres
- Create Database: test (or change the connection string in application.properties)
- Create Schema: test (or change the connection string in application.properties)
- Create Table: 
```sql
create extension if not exists "uuid-ossp";

create table if not exists test_entity
(
    id uuid default public.uuid_generate_v4(),
    name text not null,
    primary key (id)
);
```
- Configure postgres credentials in application.properties

## Results
When running the application now with `./gradlew bootRun -Dio.netty.leakDetectionLevel=paranoid`, it will start a webserver and call the single existing endpoint 5 x 1000 times in parallel.
There will be no connection leak be found by netty. You can enable the debug logs via application.properties, to double-check.

The database pool is configured to have a single short-living connection, which will then be idle, waiting for the client to fetch the payload. This connection is then also not available to the connection pool. You can see a connection waiting vor the client in the postgres statistics:
```sql
select *
from pg_stat_activity
where query != '<IDLE>'
  and query not ilike '%pg_stat_activity%'
  and usename = 'postgres'
order by pid desc;
```

Also the application will not answer (or timeout) when you call it via
```shell
curl --location --request GET 'http://localhost:8080/'
```

This behaviour can be avoided by using undertow instead of netty on the server side.
If you add a timeout to the dao method call in `Router.kt`, the behaviour reappears.

```kotlin
entityDao.findById(UUID.randomUUID())
    .timeout(Duration.ofMillis(100))
```
