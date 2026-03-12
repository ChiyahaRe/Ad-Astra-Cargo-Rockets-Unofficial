package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;
import uk.co.cablepost.f_tech.machines.abstract_machine.AbstractMachineInit;

public class LaunchPadInit extends AbstractMachineInit<LaunchPadBlock, LaunchPadBlockEntity, LaunchPadScreenHandler, LaunchPadScreen> {

    public static EntityModelLayer MODEL_LAYER;

    public LaunchPadInit(){
        super(AdAstraCargoRockets.MOD_ID, "launch_pad"
        );

    }

    @Override
    public void onInitialize() {
        super.onInitialize();

//        System.out.println("MODS FOUND BY FabricLoader");
//        for(var s : FabricLoader.getInstance().getAllMods()){
//            System.out.println(s.getMetadata().getId());
//        }

        if(FabricLoader.getInstance().getModContainer("connectormod").isPresent()) {
            LaunchPadPeripheralForgeCompat.regPer();
        }
        else{
//            PeripheralLookup.get().registerForBlockEntity(
//                LaunchPadBlockPeripheral::new,
//                getBlockEntity()
//            );

            LaunchPadPeripheralFabricCompat.regPer();
        }
    }

    @Override
    protected LaunchPadBlock createBlock(FabricBlockSettings blockSettings) {
        return new LaunchPadBlock(blockSettings);
    }

    @Override
    protected FabricBlockEntityTypeBuilder.Factory<LaunchPadBlockEntity> createBlockEntityFactory() {
        return LaunchPadBlockEntity::new;
    }

    @Override
    protected ExtendedScreenHandlerType<LaunchPadScreenHandler> createScreenHandler() {
        return new ExtendedScreenHandlerType<>(LaunchPadScreenHandler::new);
    }

    @Override
    protected void registerScreen(ExtendedScreenHandlerType<LaunchPadScreenHandler> screenHandler) {
        HandledScreens.register(screenHandler, LaunchPadScreen::new);
    }

    @Override
    public void onInitializeClient(){
        MODEL_LAYER = new EntityModelLayer(new Identifier(AdAstraCargoRockets.MOD_ID, "launch_pad"), "main");

        super.onInitializeClient();
        BlockEntityRendererFactories.register(blockEntity, LaunchPadBlockEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_LAYER, LaunchPadModel::getTexturedModelData);
    }
}
