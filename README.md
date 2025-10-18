# Locator Bar Configuration

A small paper plugin that allows players to change their locator bar settings without needing to use commands.

Supported Minecraft Versions: `1.21.8 - 1.21.10`

Inspired by RedSyven's Datapack [Locator Bar Options](https://modrinth.com/datapack/locator-bar-options).

You can access the menu from the pause screen (either through `Locator Bar Configuration` or `Custom Options` and then `Locator Bar Configuration`)

![usage-gif](/media/usage.gif)

## Disclaimer
This plugin is more a proof-of-concept than actual plugin. It uses Paper's **experimental** [Dialog API](https://docs.papermc.io/paper/dev/dialogs/) and should therefore also be seen as experimental.
Since paper-api doesn't have support for getting and setting a player's waypoint colour [yet](https://github.com/PaperMC/Paper/issues/12961), this is also the first time i had to work with NMS.

tl;dr: The code isn't pretty and the APIs it uses are experimental, but it seems to work.

### Folia
**Folia doesn't support waypoints yet**. I marked this compatible as it should be fully compatible once Folia supports waypoints/the locator bar.

## Features
Each player can
- toggle their locator bar on and off 
- customize their transmit and receive range
- customize the colour their waypoint appears to others.

## Why?
While RedSyven's datapack worked fine for our purposes, some players ran into undiagnosable issues with their options not being updated.  
Since the server i used the datapack on might switch to Folia soon and datapacks aren't enabled in Folia, I wanted to see
what Paper's **experimental** support for the Dialog API can do.

## How does it work?
The plugin registers a generic dialog (the first menu you see) in the [registry](https://docs.papermc.io/paper/dev/command-api/arguments/registry/#_top) using papers bootstrap process.
This dialog cannot be customised per player as it needs to be fully built before the server even fully starts.

The plugin uses an internal datapack that is marked for loading during the bootstrap process to register this dialog (`lbc:menu`) as part of the `pause_screen_additions` [dialog tag](https://minecraft.wiki/w/Dialog_tag_(Java_Edition)) to add it to the pause menu.
This does not seem possible via the provided paper api route at the moment. Since the datapack only handles this registration, it works fine on folia.

The first menu then opens further sub-menus that are created for the viewing player individually.