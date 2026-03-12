package uk.co.cablepost.ad_astra_cargo_rockets;

import net.minecraft.item.Item;

public class CargoRocketItem extends Item {
    public final int tier;
    public CargoRocketItem(Settings settings, int tier) {
        super(settings);
        this.tier = tier;
    }
}
