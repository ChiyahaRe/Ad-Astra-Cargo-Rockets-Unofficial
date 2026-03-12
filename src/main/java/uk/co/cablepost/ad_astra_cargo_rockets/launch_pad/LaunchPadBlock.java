package uk.co.cablepost.ad_astra_cargo_rockets.launch_pad;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;
import uk.co.cablepost.ad_astra_cargo_rockets.CargoRocketItem;
import uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket.CargoRocketEntity;
import uk.co.cablepost.f_tech.machines.abstract_machine.AbstractMachineBlock;

import java.util.List;

public class LaunchPadBlock extends AbstractMachineBlock {

    public LaunchPadBlock(Settings settings) {
        super(
            settings,
            -1,
            1,
            0,
            0,
            -1,
            1
        );
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, AdAstraCargoRockets.LAUNCH_PAD.getBlockEntity());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaunchPadBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient && !stack.isEmpty() && stack.getItem() instanceof CargoRocketItem cargoRocketItem) {
            List<CargoRocketEntity> allCargoRocketEntitiesNearby = world.getEntitiesByClass(CargoRocketEntity.class, new Box(pos).expand(3), CargoRocketEntity::isAlive);
            if(allCargoRocketEntitiesNearby.isEmpty()) {
                CargoRocketEntity cargoRocketEntity = AdAstraCargoRockets.CARGO_ROCKET_ENTITY.create(world);
                assert cargoRocketEntity != null;
                cargoRocketEntity.refreshPositionAndAngles(pos.toCenterPos().getX(), pos.getY() + 1f, pos.toCenterPos().getZ(), 0.0f, 0.0f);
                world.spawnEntity(cargoRocketEntity);
                cargoRocketEntity.setTier(cargoRocketItem.tier);

                stack.decrement(1);
                return ActionResult.CONSUME;
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}
