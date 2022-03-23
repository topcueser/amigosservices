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

- Tüm ulaşılabilir projelerin bilgilerinin tutulduğu projedir.
Projeye 8761 portundan erişilmektedir.
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
devam edecek...