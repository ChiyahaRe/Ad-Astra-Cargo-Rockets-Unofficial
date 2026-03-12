package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;

@Environment(EnvType.CLIENT)
public class LaunchPadBlockEntityRenderer implements BlockEntityRenderer<LaunchPadBlockEntity> {
    final LaunchPadModel launchPadModel;

    public LaunchPadBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        launchPadModel = new LaunchPadModel(ctx.getLayerModelPart(LaunchPadInit.MODEL_LAYER));
    }

    @Override
    public void render(LaunchPadBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5f, 2f, 0.5f);
        matrices.translate(-0.5, -2, -0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));

        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) Math.PI));
        launchPadModel.render(matrices,
            vertexConsumers.getBuffer(RenderLayer.getEntityCutout(new Identifier(AdAstraCargoRockets.MOD_ID, "textures/block/launch_pad.png"))),
            light,
            overlay,
            1f,
            1f,
            1f,
            1f
        );

        matrices.pop();
    }
}
