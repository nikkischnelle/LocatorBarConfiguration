# Locator Bar Configuration
A small paper plugin that allows players to change their locator bar settings without needing to use commands.  
Inspired by RedSyven's Datapack [Locator Bar Options](https://modrinth.com/datapack/locator-bar-options).

You can access the menu from the pause screen (either through `Locator Bar Configuration` or `Custom Options` and then `Locator Bar Configuration`)

![usage-gif](https://raw.githubusercontent.com/nikkischnelle/LocatorBarConfiguration/refs/heads/main/media/usage.gif)

## Features
Supported Minecraft Versions: `1.21.8 - 1.21.10`

Each player can
- toggle their locator bar on and off 
- customize their transmit and receive range
- customize the colour their waypoint appears to others.

Administrators can
- configure which range values and colors players can choose
- disable player customization of range or color entirely (or both but what's the point of this plugin then)
- seamlessly migrate data from RedSyven's Datapack

## Configuration
The default configuration is automatically created inside the `LocatorBarConfiguration` directory inside your plugin directory (so usually inside `plugins/LocatorBarConfiguration`).
You can add or remove options for transmit range, receive range and the icon color.

If the receiveRanges option is not set, players will have the same options as for transmitting.
If you set an option to an empty list (`iconColors: []`) the option will be locked for players.

> You can reload the config using `/lbc reload`. This requires the `lbc.reload` permission.

## Migration from Locator Bar Options
To migrate from Locator Bar Options, simply install the plugin and disable the LocatorBarOptions datapack.
User data will automatically be migrated once they (re-)join. They will be notified about this migration so they can ensure the migration worked correctly.

**More notes about migration can be found [in the wiki](https://github.com/nikkischnelle/LocatorBarConfiguration/wiki/LocatorBarOptions-migration).**

## Folia
**Folia doesn't support waypoints yet**. Folia forks that implement waypoint support will likely work already, though. CanvasMC is one such fork and officially supported.

## Why?
While RedSyven's datapack worked fine for our purposes, some players ran into undiagnosable issues with their options not being updated.  
Since the server I used the datapack on might switch to Folia soon and datapacks aren't enabled in Folia, I wanted to see
what Paper's **experimental** support for the Dialog API can do.

It now offers some features datapacks cannot offer, like configuring available options for player.

## How does it work?
The plugin registers a generic dialog (the first menu you see) in the [registry](https://docs.papermc.io/paper/dev/command-api/arguments/registry/#_top) using papers bootstrap process.
This dialog cannot be customised per player as it needs to be fully built before the server even fully starts.

The plugin uses an internal datapack that is marked for loading during the bootstrap process to register this dialog (`lbc:menu`) as part of the `pause_screen_additions` [dialog tag](https://minecraft.wiki/w/Dialog_tag_(Java_Edition)) to add it to the pause menu.
This does not seem possible via the provided paper api route at the moment. Since the datapack only handles this registration, it works fine on folia.

The first menu then opens further sub-menus that are created for the viewing player individually.

Since paper-api doesn't have support for getting and setting a player's waypoint colour [yet](https://github.com/PaperMC/Paper/issues/12961), this is also the first time I had to work with NMS.  

The transmit and receive ranges are simply set using the players' attributes.
Disabling the locator bar entirely enables a modifier on both attributes that effectively sets their values to -1.
