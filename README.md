# WGSpleef
## RU
Плагин на сплиф для майнкрафта, который работает с WorldGuard. Заточен на сервера с выживанием.
### Игровой процесс
1. Игроки заходят на арену
2. Игра начинается при достижении минимального для старта количества игроков
3. Игроки могут ломать определённые блоки, чтобы нанести поражение соперникам
4. Игрок, прикоснувшийся к лаве или огню или вышедший за пределы арены, проигрывает и исключается из арены
5. Последний оставшийся на арене игрок выигрывает и получает денежный приз [опционально]
### Постройка арены
Плагин специально разрабатывался гибким, чтобы позволить создателям серверов проивить свои творческие способности. Именно поэтому существуют некоторые нюансы, которые необходимо учесть при постройке арены.
- Арена должна быть замкнутой и полностью находиться на территории защищённого региона WorldGuard: игроки, которые каким-либо образом вышли за пределы региона, будут исключены из арены незамедлительно (это может использоваться в креативных целях: например, на арене можно расставить различные ловушки, которые выбрасывают игроков за пределы арены)
- Игрок проигрывает при соприкосновении с лавой или огнём (следует напомнить, что лава имеет свойство растекаться, что также может добавить изюминку в вашу игру)
- Игроки могут ломать только тот тип блока, который вы указали при настройке арены. 
#### Сохранение атрибутов игрока
1. Следующие атрибуты игрока сохраняются перед игрой и восстанавливаются после игры:
- Позиция
- Игровой режим
- Количество опыта
- Очки здоровья
- Очки истощения
- Очки сытости
- Очки насыщения
- Возможность полёта
- Скорость ходьбы
- Эффекты
- **Инвентарь**
2. Восстановление сломанных блоков после игры (блоки, которые можно разрушить, указываются в [настройках арены](#configyml))
### Настройки
#### config.yml
```yaml
lang: ru  # Язык локализации. Конфиг с таким именем должен находиться в директории lang
arenas:
  spleef:  # Название защищённого региона WorldGuard. Количество арен неограничено
    world: world  # Название мира, в котором находится арена
    minPlayers: 3  # Минимальное количество игроков для начала игры (не менее 2)
    maxPlayers: 10  # Максимальное количество игроков на арене
    startCoords:  # Координаты блока, на который телепортируются игроки при перемещении на арену
      x: 1294.5
      y: 97
      z: -377.5
    blockToBreak: SNOW_BLOCK  # Тип блока, который могут разрушать игроки
    startItem: DIAMOND_SPADE  # Тип блока/предмета, который получает игрок при попадании на арену
    startCd: 10  # Обратной отсчёт после перед стартом игры (в секундах)
    startCdReset: false  # Сбрасывать ли обратный отсчёт при подключении новых игроков
    winAmount: 50.0  # Сумма денежного приза для победителя
```
*Примечание*: типы блоков и предметов могут быть найдены [в официальной документации spigot](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html).

**Будьте внимательны: тип должен соответствовать либо блокам, либо предметам. Не забывайте, что в вашей версии MineCraft некоторые блоки/предметы из списка могут отсутствовать!**
#### lang.yml
В данном примере используется русский язык. В названии конфига следует указывать используемый язык (в конкретном случае файл надо назвать **ru.yml**). Конфиг должен располагаться в директории lang.
```yaml
help:
  admin: |-
    &c&l-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-
    &cКоманды&8:
    &e  /spleef join [arena] &8-&e участвовать в сплифе
    &e  /spleef leave &8-&e не участвовать в сплифе
    &e  /spleef players <arena> &8-&e количество участников сплифа
    &e  /spleef (list/arenas) &8-&e доступные арены
    &c&l-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-
    &cДля Админа&8:
    &e  /spleef reload &8-&e перезагрузить конфиги
    &e  /spleef add <arena> [params] &8-&e создать арену (в разработке)
    &e  /spleef remove <arena> &8-&e удалить арену (в разработке)
    &c&l-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-
  user: |-
    &c&l-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-
    &cКоманды&8:
    &e  /spleef join [arena] &8-&e участвовать в сплифе
    &e  /spleef leave &8-&e не участвовать в сплифе
    &e  /spleef players <arena> &8-&e количество участников сплифа
    &e  /spleef (list/arenas) &8-&e доступные арены
    &c&l-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-
join:
  alreadyIn: "&8[&cSpleef&8] &eВы уже на арене &c$arena&e!"
  success: "&8[&cSpleef&8] &eВы присоединились к арене &c$arena&e. Игроков&8: &c$players&8/&c$max"
leave:
  notIn: "&8[&cSpleef&8] &eВы не участвуете в сплифе!"
  success: "&8[&cSpleef&8] &eВы изгнаны из сплиф-кружка"
arenas:
  notExist: "&8[&cSpleef&8] &eАрены &c$arena &eне существует!"
  notEnough: "&8[&cSpleef&8] &eВсе существующие арены заполнены!"
  full: "&8[&cSpleef&8] &eНа арене &c$arena &eмаксимальное количество игроков &8(&cplayers/&cmax&8)"
  header: "&8[&cSpleef&8] &eАрены &8(&c$freeArenasCount&8/&c$arenasCount&8):\n"
  arena: "&8- &c$arena &8(&c$players&8/&c$max&8)"
  freeArena: "&8- &e$arena &8(&a$players&8/&a$max&8)"
  separator: "\n"
end:
  lose: |-
    &8[&cSpleef&8] &eВы проиграли!
    &eПопытаете удачу снова?
    &c/spleef join
  win: "&8[&cSpleef&8] &eВы победили! На ваш баланс зачислено &c$amount$"
players:
  notEnough: "&8[&cSpleef&8] &eНедостаточно игроков для начала игры!"
  header: "&8[&cSpleef&8] &eИгроки на арене &c$arena &8(&c$players&8/&c$max&8):\n"
  player: "&e- &c$player"
  separator: "\n"
wait: "&8[&cSpleef&8] &c$remain&e..."
start: "&8[&cSpleef&8] &eИгра началась!"
reload: "&8[&cSpleef&8] &eКонфиги успешно перезагружены!"
illegalCommand: |-
  &8[&cSpleef&8] &eВы не можете использовать команды во время игры!
  &eЧтобы выйти из игры, введитесегодня
  &c/spleef leave
```
В качестве спецсимвола для цветовых кодов используется амперсанд (&) или параграф (§). Может использоваться как цветовое, так и текстовое форматирование.

![Цветовые коды](/images/colorCodes.png)
![Коды для форматирования текста](/images/formatCodes.png)
## EN
Spleef minigame plugin for minecraft that works with WorldGuard  // TODO
