package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

@SuppressWarnings("all")
public class LaunchPadPeripheralFabricCompat {
    public static void regPer() {
        try {
            // Load PeripheralLookup and get BlockApiLookup instance
            Class<?> peripheralLookupClass = Class.forName("dan200.computercraft.api.peripheral.PeripheralLookup");
            Object blockApiLookupInstance = peripheralLookupClass.getMethod("get").invoke(null);

            // Load BlockApiLookup class to find method
            Class<?> blockApiLookupClass = Class.forName("net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup");

            // Find the registerForBlockEntity method with (BiFunction, BlockEntityType)
            java.lang.reflect.Method registerMethod = null;
            for (java.lang.reflect.Method m : blockApiLookupClass.getMethods()) {
                if (m.getName().equals("registerForBlockEntity")) {
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length == 2
                        && java.util.function.BiFunction.class.isAssignableFrom(params[0])
                        && params[1].getName().equals("net.minecraft.block.entity.BlockEntityType")) {
                        registerMethod = m;
                        break;
                    }
                }
            }
            if (registerMethod == null) {
                throw new NoSuchMethodException("registerForBlockEntity method not found");
            }

            // Create factory BiFunction<BlockEntity, Direction, IPeripheral>
            java.util.function.BiFunction<Object, Object, Object> factory = (blockEntity, direction) ->
                new LaunchPadBlockPeripheral((BlockEntity) blockEntity, (Direction) direction);

            Class<?> adAstraClass = Class.forName("uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets");
            Object launchPadInitInstance = adAstraClass.getField("LAUNCH_PAD").get(null);
            Class<?> launchPadInitClass = launchPadInitInstance.getClass();
            Object blockEntityTypeInstance = launchPadInitClass.getMethod("getBlockEntity").invoke(launchPadInitInstance);

            // Sanity check
            if (!registerMethod.getParameterTypes()[1].isInstance(blockEntityTypeInstance)) {
                System.err.println("BlockEntityType instance mismatch");
                System.err.println("Expected: " + registerMethod.getParameterTypes()[1].getName());
                System.err.println("Got: " + blockEntityTypeInstance.getClass().getName());
                return;
            }

            // Invoke registerForBlockEntity
            registerMethod.invoke(blockApiLookupInstance, factory, blockEntityTypeInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
