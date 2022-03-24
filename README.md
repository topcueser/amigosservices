# Microservices

![Microservices](https://github.com/topcueser/amigosservices/blob/master/microservices.png?raw=true)

*** | URL |
| --- | --- |
| Config Server         | http://localhost:8888/ |
| Eureka Server         | http://localhost:8761/ |
| Customer Service      | http://localhost:8080/ |
| Fraud Service         | http://localhost:8081/ |
| Notification Service  | http://localhost:8082/ |
| RabbitMQ Management   | http://localhost:15672/ |
| Postgresql - pgAdmin  | http://localhost:5050/ |

# Linkler

- [microservice-app-config-server](#microservice-app-config-server)

- [config-server](#config-server) (Spring Clound Config Server)

- [eureka-server](#eureka-server) (Discovery Service)

- [feign-client](#feign-client) (Spring Cloud OpenFeign)

<br>

# microservice-app-config-server

Projemizdeki development, production ortamlarına özel konfigürasyon(properties) dosyaları içeren repodur. 

```
https://github.com/topcueser/microservices-app-config-server
```
# config-server

- Microservislerin tüm konfigürasyon bilgilerini aldığı projedir. 
Projeye 8888 portundan erişilmektedir.

 `microservice-app-config-server` projesinde tuttuğumuz bilgilere ulaşmamızı sağlayan proje `config-server` dır.
 
 Tüm projeler `config-server` projesine istekte bulunarak son konfigürasyonlar ile çalışırlar.
 
 Projemizin son konfigürasyonlara ulaşmasını ve bu rolü üstlenmesini sağlamak için, 

 -  ` @EnableConfigServer ` ile projeyi çalıştıran ana sınıf işaretlenmelidir. Bağımlılık olarak ` spring-cloud-config-server ` eklenmelidir.

 <b>ConfigServerApplication.java</b>
```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApplication.class, args);
  }
}
```
<b>pom.xml</b>
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
</dependencies>
```
 
 -  ` application ` veya ` bootstrap `  isimli dosyada konfigürasyonu alacağı projenin bilgisi şu şekilde eklemelidir.

```properties  
server.port: 8888
spring.application.name: config-server
spring.cloud.config.server.git.uri: https://github.com/topcueser/microservices-app-config-server  
```

ve hangi klasörlerde arama yapılabileceği, ulaşılabileceğini aşağıdaki şekilde belirtiyoruz.

```properties
spring.cloud.config.server.git.searchPaths: client-project-config 
```
    
Istenilen profillere göre konfigürasyonlara ulaşabildiğimizi kontrol edelim.

` / proje / port / properties veya yml dosyasının adı / profil  `

şeklindeki hiyerarşi ile isteğimizi gerçekleştirelim. Burada hangi klasöre gideceğimiz daha önceden belirtildiğinden
tekrar belirmemize gerek kalmadan doğrudan dosyalara ulaşabiliyoruz.

` customer.yml ` veya ` customer-development.yml ` veya ` customer-production.yml `

```
  curl http://localhost:8888/customer/development

  curl http://localhost:8888/customer/production
```
- `config-server` projesine istekte bulunarak konfigürasyonları almak isteyen proje için bağımlılık olarak `spring-cloud-starter-config` eklenmelidir.

<b>pom.xml</b>
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
</dependencies>
```
- `config-server` örnek olarak `customer` service tarafından kullanılsın ve `customer` servisin `application.yml` içerisinde aşağıdaki gibi tanım yapılmalıdır.
```yml
spring:
  application:
    name: customer
  config:
    import: optional:configserver:http://localhost:8888
```
# eureka-server

- Tüm ulaşılabilir projelerin bilgilerinin tutulduğu projedir. Projeye 8761 portundan erişilmektedir.
- Spring Cloud Netflix Eureka, servislerin makina adı ve bağlantı noktalarına ihtiyaç duymaksızın birbiri ile iletişim kurmasını sağlar.
- Bir servis, ihtiyacı olan bir diğer servise ulaşmak istediği zaman, bilgileri Eureka Server üzerinden alabiliyor ve böylelikle uygulamamız içerisinde diğer servislerin IP, Port vs. gibi bilgilerini tutmak zorunda kalmıyoruz. 

<b>Projemizin bu rolü üstlenmesini sağlamak için, </b>

 -  ` @EnableEurekaServer ` ile projeyi çalıştıran ana sınıf işaretlenmelidir. Bağımlılık olarak ` spring-cloud-netflix-eureka-server ` eklenmelidir.

  <b>EurekaServerApplication.java</b>
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(EurekaServerApplication.class, args);
  }
}
```
<b>pom.xml</b>
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```
- `eureka-server` için oluşturulacak `application.yml` içerisinde kendi kendine kayıt olmaya çalışmasını engellemek ve  client olarak çalışırken alması gereken kayıt defteri(eureka) bilgilerini almaması için aşağıdaki bilgileri eklemeliyiz.

```yml
eureka:
  client:
    fetch-registry: false
    register-with-eureka: false
```

- Kayıt olacak olan projelerin kendilerini çalıştıran ana sınıf `@EnableEurekaClient` veya  `@EnableDiscoveryClient` ile işaretlenmelidir. Bağımlılık olarak `spring-cloud-starter-netflix-eureka-client` eklenmelidir.

<b>CustomerApplication.java (Eureka Server a kayıt olan client)</b>
```java
@SpringBootApplication
@EnableEurekaClient
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
```
<b>pom.xml</b>
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

`@EnableDiscoveryClient` ile işaretlenirse

     Eureka    ( https://netflix.github.io )
     Consul    ( https://www.consul.io )
     Zookeeper ( https://zookeeper.apache.org )  
 
desteklemek için genel bir alt yapı sağlanırken `@EnableEurekaClient` sadece Eureka yı desteklemektedir. Bu projede Eureka kullanılmaktadır.

Eureka Server a kayıt olacak client için `application.yml` içerisinde aşağıdaki tanımlamalar yapılmalıdır. 

```yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
    fetch-registry: true # eureka-server projesinin kayıt defteri bilgilerni al
    register-with-eureka: true # kendini eureka-server projesine kayıt et
```
# feign-client

<b> 
- Spring Boot uygulamalarında bir servisten diğer servise rest isteklerini nasil atabiliriz?
<br></br>
</b>

```
1-) RestTemplate

2-) Feign Client
```

_<h2>RestTemplate Kullanmak</h2>_

- `Customer` servis üzerinden `Fraud` servise istekte bulunalım.

_<b>Eureka Server olmadan</b>_

```java
private final RestTemplate restTemplate;
FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
        "http://localhost:8081/api/v1/fraud-check/{customerId}",
        FraudCheckResponse.class,
        customer.getId()
);
```

_<b>Eureka Server varken : 
Eureka Server da servis bilgileri kayıtlı olduğu için base-url, port gibi bilgileri bilmeden direk application-name üzerinden istekte bulunabiliriz.
</b>_

```java
private final RestTemplate restTemplate;
FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
        "http://FRAUD/api/v1/fraud-check/{customerId}",
        FraudCheckResponse.class,
        customer.getId()
);
```

_<h2>Feign Client Kullanmak</h2>_