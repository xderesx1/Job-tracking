# Job Recommender System

Система рекомендаций вакансий на основе навыков и опыта пользователя. Приложение помогает соискателям находить наиболее релевантные предложения о работе, а работодателям — оценивать соответствие кандидатов требованиям.

![Java](https://img.shields.io/badge/Java-17/21-blue.svg?download=true&timestamp=1785396467425)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg?download=true&timestamp=1785396937709)
![Postgesql](https://img.shields.io/badge/PostgreSQL-15-blue.svg?download=true&timestamp=1785402412539)
![Gradle](https://img.shields.io/badge/Gradle-8.x-orange.svg?download=true&timestamp=1785402533269)

🎯 **Основные возможности**

✅ CLI-интерфейс — управление через консольные команды
✅ REST API — HTTP-эндпоинты для интеграции с фронтендом
✅ Алгоритм мэтчинга — расчёт релевантности вакансии на основе навыков и опыта
✅ Асинхронные уведомления — фоновый поиск лучших предложений каждые 60 секунд
✅ История команд — сохранение и загрузка команд из файла
✅ Статистика — топ навыков, вакансии по опыту, пользователи с мэтчами
✅ Тесты — юнит-тесты (Mockito) и интеграционные (Testcontainers)

🛠 **Технологический стек**
 
Язык Java 17/21
Фреймворк Spring Boot 3.2
База данных PostgreSQL 15
Сборка Gradle 8.x
Тестирование JUnit 5, Mockito, Testcontainers
Контейнеризация Docker, Testcontainers
Дополнительно Stream API, ExecutorService, Scheduled Tasks

**Принципы проектирования**

Tell Don't Ask — логика мэтчинга инкапсулирована в Job.calculateScore()
Separation of Concerns — контроллеры → сервисы → репозитории
Dependency Injection — аннотации Spring @Autowired, @Service, @Repository
SOLID — каждый класс отвечает за одну задачу
🚀 Быстрый старт

**Требования**

Java 17 или выше
Gradle 8.x
PostgreSQL (для продакшена)
Docker (для интеграционных тестов)

**Установка**

# Клонировать репозиторийgit clone https://github.com/xderesx1/Job-tracking.gitcd job-recommender# Собрать проект./gradlew build# Запустить CLI-версию./gradlew run# Запустить Spring Boot API./gradlew bootRun
📖 Использование

CLI-интерфейс

# Создать пользователяuser alice --skills=java,spring,linux --exp=2user bob --skills=python,django,ml --exp=5
# Создать вакансиюjob Backend_Dev --company=VK --tags=java,spring,linux --exp=1job ML_Engineer --company=Google --tags=ml,python,tensorflow --exp=3
# Получить рекомендации (топ-2) suggest alice
# Список пользователей/вакансийm user-listjob-list
# Статистика stat               
# вакансии с опытом >= 2  --exp 2           
# пользователи с >= 1 мэтчем stat --match     
# топ-3 навыков stat --top-skills 3  
# История команд history 
# Выход exit

**REST API**

Метод Эндпоинт Описание
POST /api/users Создать пользователя
GET /api/users Список пользователей
POST /api/jobs Создать вакансию
GET /api/jobs Список вакансий
GET /api/suggest/{username} Рекомендации для пользователя
GET /api/stat/exp/{n} Вакансии с опытом >= n
GET /api/stat/match/{n} Пользователи с >= n мэтчами
GET /api/stat/top-skills/{n} Топ-n навыков

**Примеры запросов**

# Создать пользователя curl -X POST http://localhost:8080/api/users \  -H "Content-Type: application/json" \  -d '{"name":"alice","skills":["java","spring"],"exp":2}'
# Создать вакансию curl -X POST http://localhost:8080/api/jobs \  -H "Content-Type: application/json" \  -d '{"title":"Backend_Dev","company":"VK","tags":["java","spring"],"exp":1}'
# Получить рекомендации curl http://localhost:8080/api/suggest/alice# Статистика curl http://localhost:8080/api/stat/exp/2 curl http://localhost:8080/api/stat/top-skills/3


🧪 **Тестирование**
# Запустить все тесты./gradlew test# Только юнит-тесты./gradlew test --tests SuggestServiceUnitTest# Только интеграционные тесты./gradlew test --tests SuggestServiceIntegrationTest# С отчётом о покрытии./gradlew test jacocoTestReport

Покрытие тестами

✅ Юнит-тесты: 4 сценария (обычный, пустые вакансии, одна вакансия, пользователь не найден)
✅ Интеграционные тесты: реальная БД через Testcontainers + PostgreSQL
⚙️ Конфигурация

application.properties

properties


123456789101112
# База данныхspring.datasource.url=jdbc:postgresql://localhost:5432/job_recommenderspring.datasource.username=postgresspring.datasource.password=your_passwordspring.jpa.hibernate.ddl-auto=updatespring.jpa.show-sql=true# Порт сервераserver.port=8080# Логированиеlogging.level.ru.vk.education=DEBUG
**Переменные окружения (для Docker)**

export DB_HOST=localhostexport DB_PORT=5432export DB_NAME=job_recommenderexport DB_USER=postgresexport DB_PASSWORD=your_password

**Алгоритм мэтчинга**

java

public double calculateScore(User user) {    // 1. Считаем совпадения навыков пользователя и тегов вакансии    long matches =         user.getSkills().stream()        .filter(this.tags::contains)        .count();        double score = (double) matches;        // 2. Штраф 50% если опыт пользователя меньше требуемого    if (user.getExp() < this.exp) {        score /= 2.0;    }        return score;}
🎯 **Демонстрация работы**

CLI сессия

$ ./gradlew run> user alice --skills=java,spring --exp=2> job Backend_Dev --company=VK --tags=java,spring --exp=1> suggest aliceBackend_Dev at VK> stat --top-skills 3javaspring> exit
Фоновые уведомления

Каждую минуту в консоли появляется:


12
alice, лучшее предложение — Backend_Dev в VKbob, лучшее предложение — ML_Engineer в Google
📝 Чему я научился

Архитектура Spring Boot — от монолита к слоистой архитектуре
Работа с БД — JPA, Hibernate, миграции схемы
Асинхронность — @Scheduled, ExecutorService, потокобезопасность
Тестирование — пирамида тестов, моки, интеграционные тесты
Отладка — чтение логов, диагностика Docker-проблем, анализ stack trace
