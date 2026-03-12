package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import dan200.computercraft.api.detail.VanillaDetailRegistries;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket.CargoRocketEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchPadBlockPeripheral implements IPeripheral {

    private final LaunchPadBlockEntity blockEntity;

    public LaunchPadBlockPeripheral(BlockEntity blockEntity, Direction direction) {
        this.blockEntity = (LaunchPadBlockEntity) blockEntity;
    }

    @Override
    public void attach(IComputerAccess computer) {
        blockEntity.addComputer(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        blockEntity.removeComputer(computer);
    }

    @Override
    public String getType() {
        return "cargo_rocket_launch_pad";
    }

    @Override
    public boolean equals(IPeripheral iPeripheral) {
        return iPeripheral instanceof LaunchPadBlockPeripheral;
    }

    @LuaFunction(mainThread = true)
    public final void launch(String planet) throws LuaException {
        @Nullable CargoRocketEntity rocket = blockEntity.getRocket();
        @Nullable LaunchFailReason launchFailReason = blockEntity.launch(planet);

        if(launchFailReason == null){
            if (rocket != null) {
                // Ad Astra の音の ID を作成
                Identifier adAstraRocketSound = new Identifier("ad_astra", "rocket");

                // その ID に対応する SoundEvent を取得して再生
                rocket.getWorld().playSound(
                        null,
                        rocket.getBlockPos(),
                        net.minecraft.registry.Registries.SOUND_EVENT.get(adAstraRocketSound),
                        SoundCategory.NEUTRAL,
                        1.0f,
                        1.0f
                );
            }
            return;
        }

        switch (launchFailReason){
            case NO_ROCKET -> throw new LuaException("No rocket found");
            case INVALID_PLANET -> throw new LuaException(planet + " is not a valid planet");
            case NOT_ENOUGH_ENERGY -> throw new LuaException("Not enough energy to launch");
            case NOT_ENOUGH_FUEL -> throw new LuaException("Not enough fuel to launch.");
            case ROCKET_TIER_TOO_LOW -> {
                int difficulty = blockEntity.calculateDifficulty(planet);
                throw new LuaException(planet + " requires a Tier " + difficulty + " rocket");}
        }
    }

    @LuaFunction(mainThread = true)
    public final void moveItemsFromRocketToLaunchPad(int rocketSlot, int launchPadSlot) throws LuaException {
        @Nullable ItemMoveFailReason itemMoveFailReason = blockEntity.moveStackFromRocketToLaunchPad(rocketSlot, launchPadSlot);

        if(itemMoveFailReason == null){
            return;
        }

        switch (itemMoveFailReason){
            case NO_ROCKET -> throw new LuaException("No rocket found");
            case TARGET_FULL -> throw new LuaException("Destination full");
            case INVALID_SLOT -> throw new LuaException("Invalid slot");
        }
    }

    @LuaFunction(mainThread = true)
    public final void moveItemsFromLaunchPadToRocket(int launchPadSlot, int rocketSlot) throws LuaException {
        @Nullable ItemMoveFailReason itemMoveFailReason = blockEntity.moveStackFromLaunchPadToRocket(launchPadSlot, rocketSlot);

        if(itemMoveFailReason == null){
            return;
        }

        switch (itemMoveFailReason){
            case NO_ROCKET -> throw new LuaException("No rocket found");
            case TARGET_FULL -> throw new LuaException("Destination full");
            case INVALID_SLOT -> throw new LuaException("Invalid slot");
        }
    }

    @LuaFunction(mainThread = true)
    public final int getEnergyRequiredForLaunch() {
        return blockEntity.getEnergyRequiredForLaunch();
    }

    @LuaFunction(mainThread = true)
    public final int getFuelRequiredForLaunch() {
        return blockEntity.getFuelRequiredForLaunch()/81;
    }

    @LuaFunction(mainThread = true)
    public final long getEnergy() {
        return blockEntity.getEnergy();
    }

    @LuaFunction(mainThread = true)
    public final long getMaxEnergy() {
        return blockEntity.getMaxEnergy();
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Integer> getValidDestinations() {
        return blockEntity.getValidDestinations();
    }

    @LuaFunction(mainThread = true)
    public Map<Integer, Map<String, ?>> listLaunchPadInventory() {
        Map<Integer, Map<String, ?>> result = new HashMap<>();
        var size = blockEntity.size();
        for (var i = 0; i < size; i++) {
            var stack = blockEntity.getStack(i);
            if (!stack.isEmpty()) result.put(i + 1, Map.ofEntries(
                Map.entry("name", stack.getItem().getName().asTruncatedString(128)),
                Map.entry("id", Registries.ITEM.getId(stack.getItem()).toString()),
                Map.entry("count", stack.getCount()),
                Map.entry("max_count", stack.getMaxCount())
            ));
        }

        return result;
    }

    @LuaFunction
    public List<Integer> listLaunchPadInputSlotIndexes() {
        List<Integer> result = new ArrayList<>();
        for(int s : blockEntity._inputSlots) {
            result.add(s + 1);
        }
        return result;
    }

    @LuaFunction
    public List<Integer> listLaunchPadOutputSlotIndexes() {
        List<Integer> result = new ArrayList<>();
        for(int s : blockEntity._outputSlots) {
            result.add(s + 1);
        }
        return result;
    }

    @LuaFunction(mainThread = true)
    public Boolean isRocketPresent(){
        return blockEntity.getRocket() != null;
    }

    @LuaFunction(mainThread = true)
    public Map<Integer, @Nullable Map<String, ?>> listRocketInventory() {

        @Nullable CargoRocketEntity cargoRocketEntity = blockEntity.getRocket();

        if(cargoRocketEntity == null){
            return null;
        }

        Map<Integer, Map<String, ?>> result = new HashMap<>();
        var size = cargoRocketEntity.getInventory().size();
        for (var i = 0; i < size; i++) {
            var stack = cargoRocketEntity.getInventory().getStack(i);
            if (!stack.isEmpty()) result.put(i + 1, Map.ofEntries(
                Map.entry("name", stack.getItem().getName().asTruncatedString(128)),
                Map.entry("id", Registries.ITEM.getId(stack.getItem()).toString()),
                Map.entry("count", stack.getCount()),
                Map.entry("max_count", stack.getMaxCount())
            ));
        }

        return result;
    }

    @LuaFunction(mainThread = true)
    public void destroyRocket(){
        blockEntity.destroyRocket();
    }
}
