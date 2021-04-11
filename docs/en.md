# WGSpleed (English version)

## Gameplay
1. Players enter the arena
2. The game starts when the minimum number of players to start is reached
3. Players can break certain blocks to defeat opponents
4. A player who has touched a lava or a fire or who has stepped outside the arena loses and is excluded from the arena
5. The last remaining player wins and receives a cash prize [optional]

## Arena building
The plugin was specifically designed to be flexible to allow server creators to demonstrate their creativity. That is why there are certain nuances to be taken into account in the construction of the arena.

- Players who have moved beyond the protected WorldGuard region will be excluded from the arena immediately (this can be used for creative purposes: for example, in the arena you can place various traps that throw players out of the arena)
- The player loses when he contacts with a lava or fire (it should be recalled that the lava has the property to spread, which can also add an element to your game)
- Players can only break the block type that you specified when setting up the arena.

## Aditional information
#### Saving player attributes
The following attributes are saved before the game and restored after the game:
- Position
- Game mode
- Amount of level (EXP)
- Health points
- Hunger points
- Satiety points
- Ability to fly
- Walking speed
- Effects
- Inventory

#### Restoring broken blocks after a game
Blocks that can be destroyed are specified in the [arena settings](https://github.com/markelovstyle/WGSpleef#configyml)

## Settings
No section should be skipped, otherwise the plugin will be turned off when the server starts.

#### config.yml
TODO (code markup)

*Note*: Block and item types can be found in the [official spigot documentation](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html).<br>
<br>**Будьте внимательны: тип должен соответствовать либо блокам, либо предметам. Не забывайте, что в вашей версии MineCraft некоторые блоки/предметы из списка могут отсутствовать!**

#### lang.yml
The Russian language is used in this example. The name of the config should indicate the language used (in a specific case, the file should be called en.yml). The config must be located in the **lang** directory.
<br><br>TODO (code markup)

#### Template variables
In localization configs you can use template variables. They always start with a dollar symbol ($).

##### Arena's variables (universal)
| Name      | Value |
| ----------- | ----------- |
| players      | number of players    |
| min   | minimum number of players   |
| max   | maximum number of players   |
| remain | number of seconds to start |
| arena/region | WorldGuard protected area name |
| amount | amount of money for the last remaining player |

##### Universal variables
| Name | Parametr | Value |
| ----------- | ----------- | ----------- |
| player | players.player | player's nickname |
| arenasCount | arenas.header | number of arenas |
| freeArenasCount | arenas.header | number of free arenas |

### Color codes
*Note*: The special symbol for color codes is ampersand (&) or paragraph (>). Both color and text formatting can be used.
<br><br> Images
