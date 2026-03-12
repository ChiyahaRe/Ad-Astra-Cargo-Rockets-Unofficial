package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import uk.co.cablepost.f_tech.machines.abstract_machine.AbstractMachineScreen;
import uk.co.cablepost.f_tech.machines.abstract_machine.AbstractMachineScreenHandler;

public class LaunchPadScreen extends AbstractMachineScreen {
    public LaunchPadScreen(AbstractMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected boolean showProgress() {
        return false;
    }
}
