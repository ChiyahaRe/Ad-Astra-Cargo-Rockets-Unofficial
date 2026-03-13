package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;

public class LaunchPadPeripheralFabricCompat {
    public static void regPer() {
        // ComputerCraftのAPIを使用して周辺機器プロバイダを登録します。
        PeripheralLookup.get().registerForBlockEntity(
            (blockEntity, direction) -> new LaunchPadBlockPeripheral(blockEntity, direction),
            AdAstraCargoRockets.LAUNCH_PAD.getBlockEntity()
        );
    }
}
