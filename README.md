This is my first time mod making.
This mod may cause many bug, and i can't treat almost bugs. sorry

# Rocket Launchpad Lua API

This API allows you to control a rocket launchpad from a CC:Tweaked computer. It provides functions to launch rockets, manage inventories, and check energy levels.

---

## Connecting
You must connect the computer to the central block of the launch pad to access the below methods.
Connecting to the outer blocks will allow you to access the generic inventory methods.

---

## 📦 Inventory Slot Indexing
- All inventory slot indexes in Lua start at **1**, matching CC:Tweaked's conventions.

---

## 🧨 `launch(planet)`
Attempts to launch a rocket to the specified planet. (See `getValidDestinations`)

### Parameters
- `planet` (string): The name of the destination planet.

### Errors
- `"No rocket found"` – No rocket is on the launchpad.
- `"<planet> is not a valid planet"` – The specified planet name is invalid.
- `"Not enough energy to launch"` – The launchpad lacks sufficient energy.
- `"Not enough fuel to launch"` – The launchpad lacks sufficient fuel.
- `"<planet> is require Tier<requiredtier> rocket."` – The rocket tier is too low for the destination.

---

## 📥 `moveItemsFromRocketToLaunchPad(rocketSlot, launchPadSlot)`
Moves an item from the rocket's inventory to the launchpad's inventory.

### Parameters
- `rocketSlot` (int): Slot in the rocket's inventory.
- `launchPadSlot` (int): Slot in the launchpad's inventory.

### Errors
- `"No rocket found"`
- `"Destination full"`
- `"Invalid slot"`

---

## 📤 `moveItemsFromLaunchPadToRocket(launchPadSlot, rocketSlot)`
Moves an item from the launchpad's inventory to the rocket's inventory.

### Parameters
- `launchPadSlot` (int): Slot in the launchpad's inventory.
- `rocketSlot` (int): Slot in the rocket's inventory.

### Errors
- `"No rocket found"`
- `"Destination full"`
- `"Invalid slot"`

---

## ⚡ `getEnergyRequiredForLaunch()`
Returns the amount of energy required to launch the rocket.

### Returns
- `int`: Energy required.

---

## 🔋 `getEnergy()`
Returns the current stored energy in the launchpad.

### Returns
- `long`: Current energy.

---

## 🔋 `getMaxEnergy()`
Returns the maximum energy capacity of the launchpad.

### Returns
- `long`: Maximum energy.

---

## 🌍 `getValidDestinations()`
Returns a table of valid destination planet names, with the key being the planet and the value being the required rocket tier to reach it.

### Returns
- `table<string, int>`: Table with the key being the planet and the value being the required rocket tier to reach it.

---

## 📦 `listLaunchPadInventory()`
Returns the current non-empty inventory of the launchpad.

### Returns
- `table<int, table>`: A table mapping slot indexes to item tables with:
    - `name` (string): Display name.
    - `id` (string): Registry ID.
    - `count` (int): Stack size.
    - `max_count` (int): Maximum stack size.

---

## 📥 `listLaunchPadInputSlotIndexes()`
Lists which slot indexes are considered input slots.
These are the slots hoppers and other item transportation mods can insert into.

### Returns
- `int[]`: List of input slot indexes (1-based).

---

## 📤 `listLaunchPadOutputSlotIndexes()`
Lists which slot indexes are considered output slots.
These are the slots hoppers and other item transportation mods can extract from.

### Returns
- `int[]`: List of output slot indexes (1-based).

---

## 🚀 `isRocketPresent()`
Checks whether a rocket is present on the launchpad.

### Returns
- `boolean`: `true` if a rocket is present, `false` otherwise.

---

## 🚀 `listRocketInventory()`
Returns the current non-empty inventory of the rocket.

### Returns
- `table<int, table>|nil`: Table mapping slot indexes to item data if rocket is present, or `nil` if no rocket is found. Item tables contain:
    - `name` (string): Display name.
    - `id` (string): Registry ID.
    - `count` (int): Stack size.
    - `max_count` (int): Maximum stack size.

---
