# CrptApi

Итак, нужно сделать клиента для работы с API, на java. требуется:

- Разработать класс для работы с Api
- Класс должен быть thread-safe и поддерживать ограничение на количество запросов к API
- Ограничение указывается в конструкторе в виде количества запросов в определенный интервал времени – public CrptApi(TimeUnit timeUnit, int requestLimit). 
  timeUnit – указывает промежуток времени – секунда, минута и пр.
  requestLimit – положительное значение, которое определяет максимальное количество запросов в этом промежутке времени.
- В любой ситуации превышать лимит на количество запросов запрещено
- Решение должно быть оформлено в виде одного файла CrptApi.java
- Все дополнительные классы, которые используются должны быть внутренними
- При превышении лимита запрос вызов должен блокироваться, чтобы не превысить максимальное количество запросов к API и продолжить выполнение, без выбрасывания исключения
- Реализовать нужно единственный метод – Создание документа. Документ и подпись должны передаваться в метод в виде Java объекта и строки соответственно
- В задании необходимо просто сделать вызов указанного метода. Вызывается по HTTPS метод POST следующий URL: https://example.ru/api/v3/lk/documents/create
- В теле запроса передается в формате JSON документ
- Реализация должна быть максимально удобной для последующего расширения функционала.


Итог: самотест выполнен (см /log_self_test.txt)
