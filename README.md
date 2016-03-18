# Android2DZ5
Приложение представляет собой реализацию домашнего задания https://geekbrains.ru/streams/441/lessons/3841 
Домашнее задание:

Нужно создать будильник. В нашей активити при помощи TimePicker выбираем время. 
Жмём на кнопку установить будильник. В выбранное время должна приходить push-нотификация 
c текстом "Пора вставать" и начинаться играть любая мелодия. Плюсом будет создание виджета 
для рабочего стола, который бы отображал на какое время установлен будильник.

Технически сделать всё это при помощи AlarmManager, PendingIntent и Service.

Важно: будильник должен сработать даже если приложение выключено и все сервисы 
пристановлены. Т.е. нужно использовать NotificationReciever для того чтобы "включить" 
наш сервис в тот момент, когда должен прозвенеть будильник, и только уже тогда начать 
проигрывать музыку. Музыку играть в течении минуты, когда музыка закончет играть, 
желательно остановить сервис.

Важно 2: для проигрывания музыки использовать именно сервис, который запустится из 
NotificationReciever. В самом NotificationReciever делать долгие асинхронные задачи 
(запрос в сеть, например) противоестественно.
