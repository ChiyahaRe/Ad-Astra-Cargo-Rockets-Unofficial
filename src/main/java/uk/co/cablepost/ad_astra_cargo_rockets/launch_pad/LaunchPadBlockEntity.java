package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import dan200.computercraft.api.peripheral.IComputerAccess;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import uk.co.cablepost.ad_astra_cargo_rockets.AbstractFluidMachineBlockEntity;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;
import uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket.CargoRocketEntity;
import uk.co.cablepost.f_tech.config.FTechConfig;
import uk.co.cablepost.f_tech.machines.abstract_machine.AbstractMachineBlockEntity;

import java.util.*;

public class LaunchPadBlockEntity extends AbstractFluidMachineBlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory {

    private final Set<IComputerAccess> computers = new HashSet<>();
    public static final TagKey<net.minecraft.item.Item> DENIED_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(AdAstraCargoRockets.MOD_ID, "denied_in_launch_pad"));

    public LaunchPadBlockEntity(BlockPos pos, BlockState state) {
        super(
            AdAstraCargoRockets.LAUNCH_PAD.getBlockEntity(),
            pos,
            state,
            new int[]{ 0,  1,  2,  3,  4,  5,  6,  7,  8 },
            new int[]{ 9, 10, 11, 12, 13, 14, 15, 16, 17 },
            50000,
            1000,
            0,
            false
        );


    }

    //region BlockEntity Stuff
    @Override
    public int getMaxProcessProgress() {
        return 0;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (tag.contains("FluidContent")) {
            fluidTank.variant = FluidVariant.fromNbt(tag.getCompound("FluidContent").getCompound("variant"));
            fluidTank.amount = tag.getCompound("FluidContent").getLong("amount");
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        NbtCompound fluidTag = new NbtCompound();
        fluidTag.put("variant", fluidTank.variant.toNbt());
        fluidTag.putLong("amount", fluidTank.amount);
        nbt.put("FluidContent", fluidTag);

        super.writeNbt(nbt);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        this.writeNbt(nbtCompound);
        return nbtCompound;
    }

    @Override
    public int processEnergyConsumption() {
        return 1 * FTechConfig.CONFIG.getOrDefault("f_tech_core.energy_mul", 1);
    }

    @Override
    public Text getDisplayName() {

        return Text.translatable("container.ad_astra_cargo_rockets.launch_pad");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new LaunchPadScreenHandler(syncId, playerInventory, this, _propertyDelegate);
    }




    @Override
    public void serverTickExtra(){
    }
    //endregion

    //region Peripheral Stuff
    public void addComputer(IComputerAccess computer) {
        computers.add(computer);
    }

    public void removeComputer(IComputerAccess computer) {
        computers.remove(computer);
    }
    //endregion

    public @Nullable ItemMoveFailReason moveStackFromRocketToLaunchPad(int rocketSlotIndex, int launchPadSlotIndex) {
        @Nullable CargoRocketEntity rocket = getRocket();

        if(rocket == null){
            return ItemMoveFailReason.NO_ROCKET;
        }

        ItemStack rocketStack;
        ItemStack launchPadStack;
        try {
            rocketStack = rocket.getInventory().getStack(rocketSlotIndex - 1);
            launchPadStack = _inventory.get(launchPadSlotIndex - 1);
        }catch (Exception ignored){
            return ItemMoveFailReason.INVALID_SLOT;
        }

        if (rocketStack.isIn(DENIED_ITEMS)) {
            return ItemMoveFailReason.TARGET_FULL; // Or a new fail reason like "ITEM_DENIED" but TARGET_FULL works for stopping move
        }

        if(!launchPadStack.isEmpty() && (!rocketStack.getItem().equals(launchPadStack.getItem()) || launchPadStack.getCount() >= launchPadStack.getMaxCount())) {
            return ItemMoveFailReason.TARGET_FULL;
        }

        int maxCanMove = Math.min(
            launchPadStack.getMaxCount() - launchPadStack.getCount(),
            rocketStack.getCount()
        );

        if(maxCanMove == 0){
            return null;
        }

        if(launchPadStack.isEmpty()){
            _inventory.set(launchPadSlotIndex - 1, rocketStack.copy());
            _inventory.get(launchPadSlotIndex - 1).setCount(maxCanMove);
            rocketStack.decrement(maxCanMove);
        }
        else{
            launchPadStack.increment(maxCanMove);
            rocketStack.decrement(maxCanMove);
        }

        return null;
    }

    public @Nullable ItemMoveFailReason moveStackFromLaunchPadToRocket(int launchPadSlotIndex, int rocketSlotIndex) {
        @Nullable CargoRocketEntity rocket = getRocket();

        if(rocket == null){
            return ItemMoveFailReason.NO_ROCKET;
        }

        ItemStack rocketStack;
        ItemStack launchPadStack;
        try {
            rocketStack = rocket.getInventory().getStack(rocketSlotIndex - 1);
            launchPadStack = _inventory.get(launchPadSlotIndex - 1);
        }catch (Exception ignored){
            return ItemMoveFailReason.INVALID_SLOT;
        }

        // Check if item is denied (even when moving to rocket? Usually constraints are on LaunchPad inventory, but moving OUT is fine.
        // Wait, the request is "prevent storage items from being moved INTO LaunchPad".
        // moveStackFromRocketToLaunchPad is Rocket -> LaunchPad. So we checked there.
        // External pipes insert into LaunchPad via SidedInventory.

        if(!rocketStack.isEmpty() && (!launchPadStack.getItem().equals(rocketStack.getItem()) || rocketStack.getCount() >= rocketStack.getMaxCount())) {
            return ItemMoveFailReason.TARGET_FULL;
        }

        int maxCanMove = Math.min(
            rocketStack.getMaxCount() - rocketStack.getCount(),
            launchPadStack.getCount()
        );

        if(maxCanMove == 0){
            return null;
        }

        if(rocketStack.isEmpty()){
            rocket.getInventory().setStack(rocketSlotIndex - 1, launchPadStack.copy());
            rocket.getInventory().getStack(rocketSlotIndex - 1).setCount(maxCanMove);
            launchPadStack.decrement(maxCanMove);
        }
        else{
            rocketStack.increment(maxCanMove);
            launchPadStack.decrement(maxCanMove);
        }

        markDirty();

        return null;
    }

    public @Nullable CargoRocketEntity getRocket(){
        List<CargoRocketEntity> allCargoRocketEntitiesNearby = world.getEntitiesByClass(CargoRocketEntity.class, new Box(pos).expand(2), CargoRocketEntity::isAlive);

        allCargoRocketEntitiesNearby = allCargoRocketEntitiesNearby.stream().filter(x -> x.getPos().distanceTo(pos.toCenterPos().add(0, 0.5f, 0)) < 1f).toList();

        if(allCargoRocketEntitiesNearby.size() != 1){
            return null;
        }

        return allCargoRocketEntitiesNearby.get(0);
    }

    public int calculateDifficulty(String planet) {
        Map<String, Integer> validDestinations = getValidDestinations();

        // Get tiers
        String currentDim = world.getRegistryKey().getValue().toString();
        int currentTier = validDestinations.getOrDefault(currentDim, 1);
        int targetTier = validDestinations.getOrDefault(planet, 1);

        // Calculate difficulty: Abs(Target - Current), min 1
        return Math.max(1, Math.abs(targetTier - currentTier));
    }

    public @Nullable LaunchFailReason launch(String planet) {
        @Nullable CargoRocketEntity rocket = getRocket();

        if(rocket == null){
            return LaunchFailReason.NO_ROCKET;
        }

        Map<String, Integer> validDestinations = getValidDestinations();

        if(!validDestinations.containsKey(planet)){
            return LaunchFailReason.INVALID_PLANET;
        }

        int difficulty = calculateDifficulty(planet);

        if(difficulty > rocket.getTier()){
            return LaunchFailReason.ROCKET_TIER_TOO_LOW;
        }
        if(getEnergyRequiredForLaunch() * difficulty > _energyStorage.amount){
            return LaunchFailReason.NOT_ENOUGH_ENERGY;
        }


        if (fluidTank.amount < getFuelRequiredForLaunch() * difficulty) {
            return LaunchFailReason.NOT_ENOUGH_FUEL;
        }


        try (Transaction transaction = Transaction.openOuter()) {

            long extracted = fluidTank.extract(fluidTank.variant, (getFuelRequiredForLaunch()* difficulty), transaction);

            if (extracted == (getFuelRequiredForLaunch() * difficulty)) {

                _energyStorage.amount -= (getEnergyRequiredForLaunch() * difficulty);
                rocket.targetPlanet = planet;


                transaction.commit();
                markDirty();
                return null;
            } else {


                return LaunchFailReason.NOT_ENOUGH_FUEL;
            }
        }
    }

    public int getEnergyRequiredForLaunch() {
        return 5000;
    }
    public int getFuelRequiredForLaunch() {
        return 600000;
    }

    public long getEnergy(){
        return _energyStorage.amount;
    }
    public long getFuel(){
        return fluidTank.amount;
    }

    public long getMaxEnergy(){
        return _energyStorage.capacity;
    }

    public Map<String, Integer> getValidDestinations(){
        assert world != null;
        var worlds = Objects.requireNonNull(world.getServer()).getWorlds();

        Map<String, Integer> tierIndex = new HashMap<>();
        tierIndex.put("minecraft:overworld", 0);
        tierIndex.put("ad_astra:earth_orbit", 1);
        tierIndex.put("ad_astra:moon", 1);
        tierIndex.put("ad_astra:moon_orbit", 1);
        tierIndex.put("ad_astra:mars", 2);
        tierIndex.put("ad_astra:mars_orbit", 2);
        tierIndex.put("ad_astra:mercury", 3);
        tierIndex.put("ad_astra:mercury_orbit", 3);
        tierIndex.put("ad_astra:venus", 3);
        tierIndex.put("ad_astra:venus_orbit", 3);
        tierIndex.put("ad_astra:glacio", 4);
        tierIndex.put("ad_astra:glacio_orbit", 4);

        Map<String, Integer> validDestinations = new HashMap<>();

        for(var otherWorld : worlds){
            String dimId = otherWorld.getRegistryKey().getValue().toString();

            if(tierIndex.containsKey(dimId)){
                validDestinations.put(dimId, tierIndex.get(dimId));
            }
        }

        return validDestinations;
    }

    public void destroyRocket(){
        var rocket = getRocket();
        if(rocket == null){
            return;
        }

        rocket.killRocket();
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (stack.isIn(DENIED_ITEMS)) {
            return false;
        }
        return super.isValid(slot, stack);
    }
}
