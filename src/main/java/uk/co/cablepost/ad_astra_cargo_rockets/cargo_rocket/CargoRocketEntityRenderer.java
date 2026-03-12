package uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRocketsClient;

public class CargoRocketEntityRenderer extends EntityRenderer<CargoRocketEntity> {

    private final T1CargoRocketEntityModel<CargoRocketEntity> t1model;

    public CargoRocketEntityRenderer(EntityRendererFactory.Context context) {
        super(context);

        ModelPart modelPart = context.getPart(AdAstraCargoRocketsClient.MODEL_CARGO_ROCKET_LAYER);
        t1model = new T1CargoRocketEntityModel<>(modelPart);
    }

    @Override
    public Identifier getTexture(CargoRocketEntity entity) {
        int tier = entity.getTier();

        if(tier == 1) {
            return new Identifier(AdAstraCargoRockets.MOD_ID, "textures/entity/cargo_rocket/t1.png");
        }if(tier == 2) {
            return new Identifier(AdAstraCargoRockets.MOD_ID, "textures/entity/cargo_rocket/t2.png");
        }if(tier == 3) {
            return new Identifier(AdAstraCargoRockets.MOD_ID, "textures/entity/cargo_rocket/t3.png");
        }if(tier == 4) {
            return new Identifier(AdAstraCargoRockets.MOD_ID, "textures/entity/cargo_rocket/t4.png");
        }
        return new Identifier(AdAstraCargoRockets.MOD_ID, "textures/entity/cargo_rocket/t0.png");
    }

    @Override
    public void render(CargoRocketEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i){
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(getTexture(entity)));

        int tier = entity.getTier();

        if(tier == 2) {
            // TODO - if statements for all tiers, fall back to tier 1
        }

        t1model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
