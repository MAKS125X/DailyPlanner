# Daily Planner - ежедневник (ToDo App)

## О приложении
Приложение предоставляет следующий набор функций:
- создание собственных дел (включая планирование на определённую дату с указываемой продолжительностью);
- просмотр списка дел на определённую дату с группировкой и сортировкой по времени начала выполнения;
- обновление и удаление добавленных ранее дел.

## Технологии
- MVI, ScreenState, ScreenEvents;
- Jetpack Compose, Material Design 3 components;
- Room для локального сохранения дел;
- Coroutines для использования Flow и извлечения из него сущностей БД (а также возможной интеграции API и работы с сетью в будущем);
- Manual Dependency Injection (вследствие создания небольшого проекта отсутствовала необходимость использовать сторонние DI-библиотеки).

## Дополнительная информация
Релизная версия apk данного приложения расположена по пути: DailyPlaner/app/release/app-release.apk

Разработчик - Щёлоков Максим. 
Для Mobile-практикума от IT-компании SimbirSoft.