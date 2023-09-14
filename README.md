# java-explore-with-me
## Сервис для тех, кто хочет поделиться информацией о предстоящих интересных событиях
### Позволяет регистрировать пользователей, создавать и публиковать информацию о событиях, создавать и редактировать категории, запросы на участие и подборки событий

## Модули приложения
- Основной модуль приложения
- Модуль статистики
  - Сервис статистики
  - Вспомогательный модуль с DTO
  - Клиент статистики

## Эндпойнты приложения
- Публичные
  - /compilations GET - получение информации о подборках
  - /compilations/{compId} GET - получение информации о подборке
  - /categories GET - получение информации о категориях
  - /categories/{catId} GET - получение информации о категории
  - /events GET - получение информации о событиях
  - /events/{eventId} GET - получение информации о событии
    
 - Для зарегистрированных пользователей
   - /users/{userId}/events GET, POST - создание и получение информации о событиях
   - /users/{userId}/events/{eventId} GET, PATCH - редактирование и получение информации о событии
   - /users/{userId}/events/{eventId}/requests GET, PATCH - редактирование и получение запросов на участие в событии
   - /users/{userId}/requests GET, POST - создание и получение запросов на участие в событии
   - /users/{userId}/requests/{requestId}/cancel PATCH - отмена запроса на участие в событии
  
 - Для администраторов
   - /admin/categories POST - создание категории
   - /admin/categories/{catId} DELETE, PATCH - редактирование и удаление категории
   - /admin/events GET - получение информации о событиях
   - /admin/events/{eventId} PATCH -редактирование информации о событии
   - /admin/users GET, POST - создание и получение пользователя
   - /admin/users/{userId} DELETE - удаление пользователя
   - /admin/compilations POST - создание подборки
   - /admin/compilations/{compId} DELETE, PATCH - редактирование и удаление подборки
  
## Дополнительная функциональность - модерация событий перед публикацией, допольнительные комментарии
## Эндпойнты

 - Для зарегистрированных пользователей
   - /users/{userId}/events/{eventId}/comments POST - создание комментариев
   - /users/{userId}/events/{eventId}/comments/{commentId} PATCH - редактирование комментария
   - /users/events/{eventId}/comments GET - получение комментариев

 - Для администраторов
   - /admin/{userId}/events/{eventId}/comments GET, POST - создание и получение комментариев
   - /admin/{userId}/events/{eventId}/comments/{commentId} PATCH - редактирование комментария

