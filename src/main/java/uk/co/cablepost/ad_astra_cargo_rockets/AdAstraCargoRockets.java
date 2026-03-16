package uk.co.cablepost.ad_astra_cargo_rockets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket.CargoRocketEntity;
import uk.co.cablepost.ad_astra_cargo_rockets.launch_pad.LaunchPadBlockEntity;
import uk.co.cablepost.ad_astra_cargo_rockets.launch_pad.LaunchPadInit;
import uk.co.cablepost.f_tech.machines.machine_shell.MachineShellBlockInit;

public class AdAstraCargoRockets implements ModInitializer {
    public static final String MOD_ID = "ad_astra_cargo_rockets";

    public static final CargoRocketItem CARGO_ROCKET_TIER_1_ITEM = new CargoRocketItem(new FabricItemSettings().maxCount(1), 1);
    public static final CargoRocketItem CARGO_ROCKET_TIER_2_ITEM = new CargoRocketItem(new FabricItemSettings().maxCount(1), 2);
    public static final CargoRocketItem CARGO_ROCKET_TIER_3_ITEM = new CargoRocketItem(new FabricItemSettings().maxCount(1), 3);
    public static final CargoRocketItem CARGO_ROCKET_TIER_4_ITEM = new CargoRocketItem(new FabricItemSettings().maxCount(1), 4);

    public static final EntityType<CargoRocketEntity> CARGO_ROCKET_ENTITY = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(MOD_ID, "cargo_rocket"),
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, CargoRocketEntity::new).dimensions(EntityDimensions.fixed(1.5f, 5.5f)).build()
    );

    public static final LaunchPadInit LAUNCH_PAD = new LaunchPadInit();

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(LAUNCH_PAD.getBlock().asItem()))
        .displayName(Text.translatable("itemGroup.ad_astra_cargo_rockets.items"))
        .entries((context, entries) -> {
            entries.add(LAUNCH_PAD.getBlock().asItem());
            entries.add(CARGO_ROCKET_TIER_1_ITEM);
            entries.add(CARGO_ROCKET_TIER_2_ITEM);
            entries.add(CARGO_ROCKET_TIER_3_ITEM);
            entries.add(CARGO_ROCKET_TIER_4_ITEM);
        })
        .build()
    ;

    @Override
    public void onInitialize() {
        ModConfig.load();

        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "main_creative_inventory_tab"), ITEM_GROUP);

        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "cargo_rocket_tier_1"), CARGO_ROCKET_TIER_1_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "cargo_rocket_tier_2"), CARGO_ROCKET_TIER_2_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "cargo_rocket_tier_3"), CARGO_ROCKET_TIER_3_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "cargo_rocket_tier_4"), CARGO_ROCKET_TIER_4_ITEM);
        LAUNCH_PAD.onInitialize();

        FluidStorage.SIDED.registerForBlocks(
                (world, pos, state, blockEntity, direction) -> {

                    if (blockEntity instanceof LaunchPadBlockEntity launchPad) {
                        return launchPad.fluidTank;
                    }


                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x == 0 && z == 0) continue;

                            BlockPos checkPos = pos.add(x, 0, z);
                            BlockEntity be = world.getBlockEntity(checkPos);

                            if (be instanceof LaunchPadBlockEntity launchPad) {
                                return launchPad.fluidTank;
                            }
                        }
                    }
                    return null;
                },
                LAUNCH_PAD.getBlock(),
                MachineShellBlockInit.MACHINE_SHELL_BLOCK
        );

        ItemStorage.SIDED.registerForBlocks(
                (world, pos, state, blockEntity, direction) -> {

                    if (blockEntity instanceof LaunchPadBlockEntity launchPad) {
                        return InventoryStorage.of(launchPad, direction);
                    }


                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x == 0 && z == 0) continue;

                            BlockPos checkPos = pos.add(x, 0, z);
                            BlockEntity be = world.getBlockEntity(checkPos);

                            if (be instanceof LaunchPadBlockEntity launchPad) {
                                return InventoryStorage.of(launchPad, direction);
                            }
                        }
                    }
                    return null;
                },
                LAUNCH_PAD.getBlock(),
                MachineShellBlockInit.MACHINE_SHELL_BLOCK
        );
    }

}
