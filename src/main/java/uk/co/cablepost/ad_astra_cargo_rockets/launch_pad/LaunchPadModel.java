package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value= EnvType.CLIENT)
public class LaunchPadModel extends Model {
    private final ModelPart base;
    private final ModelPart mainBody;
    private final ModelPart topPlatform;

    public LaunchPadModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.base = root.getChild("base");
        this.mainBody = this.base.getChild("main_body");
        this.topPlatform = this.base.getChild("top_platform");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        // Main body element (from: [-16, 0, -16], to: [32, 13, 32])
        // Entity model layout: Right(48x13) Front(48x13) Left(48x13) Back(48x13) Top(48x48) Bottom(48x48)
        base.addChild("main_body", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-16.0F, -24.0F, -16.0F, 48.0F, 13.0F, 48.0F, new Dilation(0.0F)), 
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        // Top platform element (from: [-15, 13, -15], to: [31, 15, 31])
        // Entity model layout: Right(46x2) Front(46x2) Left(46x2) Back(46x2) Top(46x46) Bottom(46x46)
        base.addChild("top_platform", ModelPartBuilder.create()
                .uv(0, 61).cuboid(-15.0F, -11.0F, -15.0F, 46.0F, 2.0F, 46.0F, new Dilation(0.0F)), 
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
