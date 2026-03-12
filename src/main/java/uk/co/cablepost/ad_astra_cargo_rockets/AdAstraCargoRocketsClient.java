package uk.co.cablepost.ad_astra_cargo_rockets;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket.T1CargoRocketEntityModel;
import uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket.CargoRocketEntityRenderer;

public class AdAstraCargoRocketsClient implements ClientModInitializer {

    public static final EntityModelLayer MODEL_CARGO_ROCKET_LAYER = new EntityModelLayer(new Identifier(AdAstraCargoRockets.MOD_ID, "cargo_rocket"), "main");;

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(MODEL_CARGO_ROCKET_LAYER, T1CargoRocketEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(AdAstraCargoRockets.CARGO_ROCKET_ENTITY, CargoRocketEntityRenderer::new);

        AdAstraCargoRockets.LAUNCH_PAD.onInitializeClient();
    }
}
