package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class LaunchPadPeripheralForgeCompat {

    public static void regPer() {
        try {
            System.out.println("[LaunchPadPeripheralForgeCompat] Registering peripheral provider...");

            Class<?> providerInterface = Class.forName("dan200.computercraft.api.peripheral.IPeripheralProvider");
            Class<?> levelClass = Class.forName("net.minecraft.world.level.Level");
            Class<?> blockPosClass = Class.forName("net.minecraft.core.BlockPos");
            Class<?> directionClass = Class.forName("net.minecraft.core.Direction");

            dan200.computercraft.api.ForgeComputerCraftAPI.registerPeripheralProvider((IPeripheralProvider)
                java.lang.reflect.Proxy.newProxyInstance(
                    LaunchPadPeripheralForgeCompat.class.getClassLoader(),
                    new Class<?>[]{providerInterface},
                    (proxy, method, args) -> {
                        String name = method.getName();
                        System.out.println("[LaunchPadPeripheralForgeCompat] Proxy method called: " + name);

                        if ("getPeripheral".equals(name)) {
                            Object world = args[0];
                            Object pos = args[1];
                            Object side = args[2];

                            System.out.println("[LaunchPadPeripheralForgeCompat] getPeripheral called with world=" + world + ", pos=" + pos + ", side=" + side);

                            IPeripheral peripheral = LaunchPadPeripheralForgeCompat.getPeripheral(world, pos, side);

                            if (peripheral == null) {
                                System.out.println("[LaunchPadPeripheralForgeCompat] No peripheral found, returning empty.");
                                return callEmptyOptional();
                            } else {
                                System.out.println("[LaunchPadPeripheralForgeCompat] Peripheral found: " + peripheral);
                            }

                            try {
                                Class<?> lazyOptionalClass = Class.forName("net.minecraftforge.common.util.LazyOptional");
                                Class<?> nonNullSupplierClass = Class.forName("net.minecraftforge.common.util.NonNullSupplier");

                                Object supplierProxy = java.lang.reflect.Proxy.newProxyInstance(
                                    nonNullSupplierClass.getClassLoader(),
                                    new Class[]{nonNullSupplierClass},
                                    (p, m, a) -> {
                                        if ("get".equals(m.getName())) {
                                            System.out.println("[DEBUG] Proxy supplier get() called");
                                            return peripheral;
                                        }
                                        throw new UnsupportedOperationException("Unsupported method: " + m.getName());
                                    }
                                );

                                Object lazyOptional = lazyOptionalClass
                                    .getMethod("of", nonNullSupplierClass)
                                    .invoke(null, supplierProxy);

                                System.out.println("[LaunchPadPeripheralForgeCompat] Returning LazyOptional.of(peripheral)");
                                return lazyOptional;

                            } catch (Exception e) {
                                System.out.println("[LaunchPadPeripheralForgeCompat] Failed to create LazyOptional.of(peripheral)");
                                e.printStackTrace();
                                throw new RuntimeException("Failed to create LazyOptional.of(peripheral)", e);
                            }
                        } else if ("equals".equals(name)) {
                            return proxy == args[0];
                        } else if ("hashCode".equals(name)) {
                            return System.identityHashCode(proxy);
                        } else if ("toString".equals(name)) {
                            return "LaunchPadPeripheralForgeCompat Proxy";
                        }

                        System.out.println("[LaunchPadPeripheralForgeCompat] Unknown proxy method: " + name);
                        return null;
                    }
                )
            );

            System.out.println("[LaunchPadPeripheralForgeCompat] Peripheral provider registration completed.");
        } catch (Exception e) {
            System.out.println("[LaunchPadPeripheralForgeCompat] Failed to create Forge IPeripheralProvider via proxy");
            e.printStackTrace();
            throw new RuntimeException("Failed to create Forge IPeripheralProvider via proxy", e);
        }
    }

    public static LaunchPadBlockPeripheral getPeripheral(Object worldObj, Object posObj, Object sideObj) {
        System.out.println("[LaunchPadPeripheralForgeCompat] getPeripheral method called");
        try {
            Class<?> levelClass = Class.forName("net.minecraft.world.level.Level");
            if (!levelClass.isInstance(worldObj)) {
                System.out.println("[LaunchPadPeripheralForgeCompat] worldObj is not an instance of Level: " + worldObj);
                return null;
            } else {
                System.out.println("[LaunchPadPeripheralForgeCompat] worldObj is a Level instance");
            }

            Class<?> blockPosClass = Class.forName("net.minecraft.core.BlockPos");
            if (!blockPosClass.isInstance(posObj)) {
                System.out.println("[LaunchPadPeripheralForgeCompat] posObj is not an instance of BlockPos: " + posObj);
                return null;
            } else {
                System.out.println("[LaunchPadPeripheralForgeCompat] posObj is a BlockPos instance");
            }

//            for(var x : levelClass.getFields()){
//                System.out.println("[LaunchPadPeripheralForgeCompat] Level Field: " + x.getName());
//            }
//
//            for(var x : levelClass.getMethods()){
//                System.out.println("[LaunchPadPeripheralForgeCompat] Level Method: " + x.getName());
//            }

            Object blockEntity = levelClass.getMethod("m_7702_", blockPosClass).invoke(worldObj, posObj);
            System.out.println("[LaunchPadPeripheralForgeCompat] Got blockEntity: " + blockEntity);

            //Class<?> launchPadPeripheralClass = Class.forName("uk.co.cablepost.ad_astra_cargo_rockets.launch_pad.LaunchPadBlockPeripheral");

            if (blockEntity instanceof LaunchPadBlockEntity launchPadBlockEntity) {
                System.out.println("[LaunchPadPeripheralForgeCompat] blockEntity is an instance of LaunchPadBlockEntity");
                return new LaunchPadBlockPeripheral(launchPadBlockEntity, null);
            } else {
                System.out.println("[LaunchPadPeripheralForgeCompat] blockEntity is NOT an instance of LaunchPadBlockEntity");
            }
        } catch (Exception e) {
            System.out.println("[LaunchPadPeripheralForgeCompat] Exception in getPeripheral:");
            System.out.println("[LaunchPadPeripheralForgeCompat] " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static Object callEmptyOptional() {
        System.out.println("[LaunchPadPeripheralForgeCompat] Returning LazyOptional.empty()");
        try {
            Class<?> lazyOptionalClass = Class.forName("net.minecraftforge.common.util.LazyOptional");
            return lazyOptionalClass.getMethod("empty").invoke(null);
        } catch (Exception e) {
            System.out.println("[LaunchPadPeripheralForgeCompat] Failed to call LazyOptional.empty()");
            e.printStackTrace();
            throw new RuntimeException("Failed to call LazyOptional.empty()", e);
        }
    }
}
