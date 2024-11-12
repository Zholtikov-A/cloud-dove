
# Cloud dove

Программа позволяет загружать и скачивать ранее загруженные изображения,
а также получать список загруженных изображений по указанным параметрам.
В программе реализованы хранение в YandexCloud, авторизация и аутентификация через Spring Security + PostgreSQL DB,
а также отсылаются отбивки на почту пользователя о его действиях.

<div>
<img width="1024" alt="Dove-postman takes image to cloud" src="landing/cloud-dove-welcome.png">
</div>

## API (Swagger)

Опубликовано по адресу: 

https://app.swaggerhub.com/apis-docs/lessera/cloud-dove/v0#/

## Make it work! (инструкция по запуску) ▶️

### Docker

Необходимое для работы программы окружение (база данных PostgreSQL и брокер сообщений Apache Kafka) настроено в файле docker-compose.yml
После заполнения данных почты и объектного хранилища для запуска программы следует перейти в директорию приложения 
и ввести в терминале команду docker compose up -d

Порт по-умолчанию 8081

### Почта

Для пересылки писем используется SMTP gmail.
Вам потребуется соответствующий ящик с двухэтапной аутентификацией.
Включить ее можно в настройках, вкладка "Безопасность".
По следующей ссылке нужно создать пароль для приложений:
https://myaccount.google.com/apppasswords

В файл application.properties модуля mail необходимо ввести
почтовый адрес и пароль для приложения в соответствующие поля.
* spring.mail.username=
* spring.mail.password=

### Объектное хранилище

Для записи файлов в облачное хранилище потребуется:
Хранилище S3, в котором необходимо создать сервисный аккаунт.

В файл application.properties модуля core необходимо ввести
* aws.access.key
* aws.secret.key
* Также в зависимости от выбранного сервиса заполнить поля(по умолчанию заполнены для yandex cloud):
* aws.service.endpoint=
* aws.signing.region=
* Также следует указать имя bucket'а в вашем хранилище:
* aws.bucket.name=


### Аутентификация и авторизация:

По умолчанию в программе создан пользователь moderator с паролем moderator и правами доступа ко всем эндпоинтам.
Для создания нового пользователя аутентификация не требуется. Пользователь по-умолчанию получает роль USER и может 
загружать файлы, просматривать и скачивать свои файлы.

## Tech & Tools 🛠

<div>
      <img src="https://github.com/Salaia/icons/blob/main/green/Java.png?raw=true" title="Java" alt="Java" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/SPRING%20boot.png?raw=true" title="Spring Boot" alt="Spring Boot" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/Maven.png?raw=true" title="Apache Maven" alt="Apache Maven" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Rest%20API.png?raw=true" title="Rest API" alt="Rest API" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/Microservice.png?raw=true" title="Microservice" alt="Microservice" height="40"/>    
      <img src="https://github.com/Salaia/icons/blob/main/green/Spring Security.png?raw=true" title="Spring Security" alt="Spring Security" height="40"/>    
<img src="https://github.com/Salaia/icons/blob/main/green/JDBC.png?raw=true" title="JDBC" alt="JDBC" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/JPA.png?raw=true" title="JPA" alt="JPA" height="40"/>
 <img src="https://github.com/Salaia/icons/blob/main/green/Hibernate.png?raw=true" title="Hibernate" alt="Hibernate" height="40"/>
       <img src="https://github.com/Salaia/icons/blob/main/green/Lombok.png?raw=true" title="Lombok" alt="Lombok" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/PostgreSQL.png?raw=true" title="PostgreSQL" alt="PostgreSQL" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Amazon S3.png?raw=true" title="AWS S3" alt="Amazon Web Services S3" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Kafka-3.png?raw=true" title="Apache Kafka" alt="Apache Kafka" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/SPRING Java Mail.png?raw=true" title="Spring Java Mail" alt="Spring Java Mail" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Docker.png?raw=true" title="Docker" alt="Docker" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Postman.png?raw=true" title="Postman" alt="Postman" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Swagger.png?raw=true" title="Swagger" alt="Swagger" height="40"/>
</div>


## Testing
### Postman image list upload

POST - Body, form-data, поле "Key" должно быть одинаково для всех файлов, тогда в API придет List>MultipartFile>
(не забыть выбрать тип загрузки "File" и присоединить по файлу на ключ)
Тесты находятся в директории postman.