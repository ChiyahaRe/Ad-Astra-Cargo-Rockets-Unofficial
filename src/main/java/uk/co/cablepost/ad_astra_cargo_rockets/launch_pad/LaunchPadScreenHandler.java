package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;
import uk.co.cablepost.f_tech.machines.abstract_machine.AbstractMachineScreenHandler;

public class LaunchPadScreenHandler extends AbstractMachineScreenHandler {

    public LaunchPadScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(
            syncId,
            playerInventory,
            playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
            new ArrayPropertyDelegate(LaunchPadBlockEntity.getPropertyDelegateSize())
        );
    }

    public LaunchPadScreenHandler(
        int syncId,
        PlayerInventory playerInventory,
        BlockEntity blockEntity,
        PropertyDelegate arrayPropertyDelegate
    ) {
        super(
            AdAstraCargoRockets.LAUNCH_PAD.getScreenHandler(),
            syncId,
            playerInventory,
            blockEntity,
            arrayPropertyDelegate
        );
    }
}
