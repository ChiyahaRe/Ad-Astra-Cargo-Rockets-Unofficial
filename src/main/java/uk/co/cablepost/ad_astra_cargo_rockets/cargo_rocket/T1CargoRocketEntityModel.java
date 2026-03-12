package uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class T1CargoRocketEntityModel<T extends CargoRocketEntity> extends SinglePartEntityModel<T> {

    private final ModelPart rocketbody;
    private final ModelPart rockethead;
    private final ModelPart rings3;
    private final ModelPart rings;
    private final ModelPart rings2;
    private final ModelPart bb_main;

    public T1CargoRocketEntityModel(ModelPart root) {
        this.rocketbody = root.getChild("rocketbody");
        this.rockethead = root.getChild("rockethead");
        this.rings3 = this.rockethead.getChild("rings3");
        this.rings = root.getChild("rings");
        this.rings2 = root.getChild("rings2");
        this.bb_main = root.getChild("bb_main");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        //matrices.scale(2f, 1.333f, 2f);
        matrices.translate(0f, -1.5f, 0f);

        rocketbody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        rockethead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        rings.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        rings2.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertices, light, overlay, red, green, blue, alpha);

        matrices.pop();
    }

    @Override
    public ModelPart getPart() {
        return bb_main;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData rocketbody = modelPartData.addChild("rocketbody", ModelPartBuilder.create().uv(99, 32).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(99, 0).cuboid(-3.0F, 0.0F, 20.0F, 6.0F, 9.0F, 4.0F, new Dilation(0.0F))
            .uv(99, 13).cuboid(-3.0F, 0.0F, -2.0F, 6.0F, 9.0F, 4.0F, new Dilation(0.0F))
            .uv(99, 26).cuboid(-1.0F, -3.0F, 20.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(0, 0).cuboid(-9.0F, -34.0F, 2.0F, 18.0F, 44.0F, 18.0F, new Dilation(0.0F))
            .uv(0, 83).cuboid(-6.0F, 10.0F, 5.0F, 12.0F, 3.0F, 12.0F, new Dilation(0.0F))
            .uv(1, 63).cuboid(-8.0F, 13.0F, 3.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 7.0F, -11.0F));

        ModelPartData cube_r1 = rocketbody.addChild("cube_r1", ModelPartBuilder.create().uv(82, 40).mirrored().cuboid(-6.0F, -3.5F, -1.0F, 16.0F, 9.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(9.0503F, 3.5F, 20.0502F, -0.3295F, -0.7268F, 0.4754F));

        ModelPartData cube_r2 = rocketbody.addChild("cube_r2", ModelPartBuilder.create().uv(82, 40).mirrored().cuboid(-8.0F, -4.5F, -1.0F, 16.0F, 9.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(10.1373F, 5.1237F, 1.1373F, 0.3295F, 0.7268F, 0.4754F));

        ModelPartData cube_r3 = rocketbody.addChild("cube_r3", ModelPartBuilder.create().uv(82, 40).cuboid(-8.0F, -4.5F, -1.0F, 16.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-10.1373F, 5.1237F, 1.1373F, 0.3295F, -0.7268F, -0.4754F));

        ModelPartData cube_r4 = rocketbody.addChild("cube_r4", ModelPartBuilder.create().uv(82, 40).cuboid(-10.0F, -3.5F, -1.0F, 16.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-9.0503F, 3.5F, 20.0502F, -0.3295F, 0.7268F, -0.4754F));

        ModelPartData cube_r5 = rocketbody.addChild("cube_r5", ModelPartBuilder.create().uv(83, 0).mirrored().cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-16.0F, 9.0F, -4.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r6 = rocketbody.addChild("cube_r6", ModelPartBuilder.create().uv(83, 0).mirrored().cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-16.0F, 9.0F, 27.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r7 = rocketbody.addChild("cube_r7", ModelPartBuilder.create().uv(83, 0).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(16.0F, 9.0F, 27.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r8 = rocketbody.addChild("cube_r8", ModelPartBuilder.create().uv(83, 0).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(16.0F, 9.0F, -4.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData rockethead = modelPartData.addChild("rockethead", ModelPartBuilder.create().uv(104, 114).cuboid(-12.0F, -24.0F, -3.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F))
            .uv(88, 122).cuboid(-11.0F, -26.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
            .uv(80, 113).cuboid(-10.0F, -37.0F, -1.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F))
            .uv(88, 114).cuboid(-11.0F, -40.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(9.0F, -27.0F, 0.0F));

        ModelPartData cube_r9 = rockethead.addChild("cube_r9", ModelPartBuilder.create().uv(0, 102).mirrored().cuboid(-1.0F, -24.0F, -1.0F, 2.0F, 24.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-18.0F, 0.0F, -9.0F, -0.48F, 0.7854F, 0.0F));

        ModelPartData cube_r10 = rockethead.addChild("cube_r10", ModelPartBuilder.create().uv(0, 102).mirrored().cuboid(-1.0F, -24.0F, -1.0F, 2.0F, 24.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-18.0F, 0.0F, 9.0F, 0.4326F, 0.678F, 0.6346F));

        ModelPartData cube_r11 = rockethead.addChild("cube_r11", ModelPartBuilder.create().uv(0, 102).cuboid(-1.0F, -24.0F, -1.0F, 2.0F, 24.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 9.0F, 0.4326F, -0.678F, -0.6346F));

        ModelPartData cube_r12 = rockethead.addChild("cube_r12", ModelPartBuilder.create().uv(0, 102).cuboid(-1.0F, -24.0F, -1.0F, 2.0F, 24.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -9.0F, -0.48F, -0.7854F, 0.0F));

        ModelPartData cube_r13 = rockethead.addChild("cube_r13", ModelPartBuilder.create().uv(83, 68).cuboid(-8.0F, -24.0F, 0.0F, 16.0F, 24.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.0F, 0.0F, 9.0F, 2.7925F, 0.0F, -3.1416F));

        ModelPartData cube_r14 = rockethead.addChild("cube_r14", ModelPartBuilder.create().uv(83, 68).cuboid(-8.0F, -24.0F, 0.0F, 16.0F, 24.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.0F, 0.0F, -9.0F, 2.7925F, 3.1416F, 3.1416F));

        ModelPartData cube_r15 = rockethead.addChild("cube_r15", ModelPartBuilder.create().uv(83, 68).cuboid(-8.0F, -24.0F, 0.0F, 16.0F, 24.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-18.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.3491F));

        ModelPartData cube_r16 = rockethead.addChild("cube_r16", ModelPartBuilder.create().uv(83, 68).cuboid(-8.0F, -24.0F, 0.0F, 16.0F, 24.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.3491F));

        ModelPartData rings3 = rockethead.addChild("rings3", ModelPartBuilder.create(), ModelTransform.pivot(0.5F, 0.0F, 0.0F));

        ModelPartData cube_r17 = rings3.addChild("cube_r17", ModelPartBuilder.create().uv(19, 121).cuboid(-10.5F, -1.0F, 0.0F, 21.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.499F, 0.0F, -10.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r18 = rings3.addChild("cube_r18", ModelPartBuilder.create().uv(19, 121).cuboid(-10.5F, -1.0F, 0.0F, 21.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.499F, 0.0F, 10.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r19 = rings3.addChild("cube_r19", ModelPartBuilder.create().uv(19, 121).cuboid(-10.5F, -1.0F, 0.0F, 21.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-20.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r20 = rings3.addChild("cube_r20", ModelPartBuilder.create().uv(19, 121).cuboid(-10.5F, -1.0F, 0.0F, 21.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData rings = modelPartData.addChild("rings", ModelPartBuilder.create(), ModelTransform.pivot(9.5F, 13.0F, 0.0F));

        ModelPartData cube_r21 = rings.addChild("cube_r21", ModelPartBuilder.create().uv(21, 115).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.499F, 0.0F, -9.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r22 = rings.addChild("cube_r22", ModelPartBuilder.create().uv(21, 115).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.499F, 0.0F, 9.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r23 = rings.addChild("cube_r23", ModelPartBuilder.create().uv(21, 115).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-19.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r24 = rings.addChild("cube_r24", ModelPartBuilder.create().uv(21, 115).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData rings2 = modelPartData.addChild("rings2", ModelPartBuilder.create(), ModelTransform.pivot(9.5F, 10.0F, 0.0F));

        ModelPartData cube_r25 = rings2.addChild("cube_r25", ModelPartBuilder.create().uv(22, 118).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.499F, 0.0F, -9.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r26 = rings2.addChild("cube_r26", ModelPartBuilder.create().uv(22, 118).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-9.499F, 0.0F, 9.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r27 = rings2.addChild("cube_r27", ModelPartBuilder.create().uv(22, 118).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-19.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r28 = rings2.addChild("cube_r28", ModelPartBuilder.create().uv(22, 118).cuboid(-9.5F, -1.0F, 0.0F, 19.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(100, 52).cuboid(-4.0F, -40.0F, -10.0F, 8.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }
}
