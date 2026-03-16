package uk.co.cablepost.ad_astra_cargo_rockets;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import uk.co.cablepost.f_tech.machines.abstract_machine.AbstractMachineBlockEntity;

public abstract class AbstractFluidMachineBlockEntity extends AbstractMachineBlockEntity {

    public final SingleVariantStorage<FluidVariant> fluidTank = new SingleVariantStorage<>() {


        @Override
        public long insert(FluidVariant variant, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {

            if (!isResourceAllowed(variant)) {
                return 0;
            }
            return super.insert(variant, maxAmount, transaction);
        }


        private boolean isResourceAllowed(FluidVariant variant) {
            String fluidId = Registries.FLUID.getId(variant.getFluid()).toString();
            return ModConfig.INSTANCE.fuels.containsKey(fluidId);
        }

        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return 32 * FluidConstants.BUCKET;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    public AbstractFluidMachineBlockEntity(
            BlockEntityType<?> type,
            BlockPos pos,
            BlockState state,
            int[] inputSlots,
            int[] outputSlots,
            int energyStorageCapacity,
            int energyStorageMaxInsert,
            int energyStorageMaxExtract,
            boolean doProcessing
    ) {
        super(type, pos, state,
                inputSlots,
                outputSlots,
                energyStorageCapacity,
                energyStorageMaxInsert,
                energyStorageMaxExtract,
                doProcessing);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putLong("FluidAmount", fluidTank.amount);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        fluidTank.amount = tag.getLong("FluidAmount");
    }
}
