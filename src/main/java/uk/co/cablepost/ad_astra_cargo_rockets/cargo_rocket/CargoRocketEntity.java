package uk.co.cablepost.ad_astra_cargo_rockets.cargo_rocket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import uk.co.cablepost.ad_astra_cargo_rockets.AdAstraCargoRockets;

import java.util.List;
import java.util.Objects;

public class CargoRocketEntity extends Entity implements InventoryOwner {
    public String targetPlanet = "";
    private final SimpleInventory inventory = new SimpleInventory(9);

    private static final TrackedData<Integer> TRACKED_TIER = DataTracker.registerData(CargoRocketEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRACKED_LAUNCH_TICKS = DataTracker.registerData(CargoRocketEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private boolean hasPlayedLandingSound = false;

    public CargoRocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(TRACKED_TIER, 0);
        this.dataTracker.startTracking(TRACKED_LAUNCH_TICKS, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        dataTracker.set(TRACKED_TIER, nbt.getInt("Tier"));
        dataTracker.set(TRACKED_LAUNCH_TICKS, nbt.getInt("LaunchTicks"));

        targetPlanet = nbt.getString("TargetPlanet");
        this.readInventory(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Tier", dataTracker.get(TRACKED_TIER));
        nbt.putInt("LaunchTicks", dataTracker.get(TRACKED_LAUNCH_TICKS));
        nbt.putString("TargetPlanet", targetPlanet);
        this.writeInventory(nbt);
    }

    public void setTier(int tier){
        dataTracker.set(TRACKED_TIER, tier);
    }

    public int getTier(){
        return dataTracker.get(TRACKED_TIER);
    }

    public void setLaunchTicks(int ticks) {
        dataTracker.set(TRACKED_LAUNCH_TICKS, ticks);
    }

    public int getLaunchTicks() {
        return dataTracker.get(TRACKED_LAUNCH_TICKS);
    }

    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean canUsePortals(){
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public boolean collidesWith(Entity other) {
        return canCollide(this, other);
    }

    public static boolean canCollide(Entity entity, Entity other) {
        return other.isCollidable() && !entity.isConnectedThroughVehicle(other);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if(player.getWorld().isClient() || hand == Hand.OFF_HAND){
            return ActionResult.PASS;
        }

        player.sendMessage(Text.of(" ====== Ship inventory ======"));// TODO - open inventory GUI

        for(int i = 0; i < getInventory().size(); i++){
            var stack = getInventory().getStack(i);
            if(stack.isEmpty()){
                continue;
            }

            player.sendMessage(Text.of("Slot " + i + ": " + stack.getCount() + "x ").copy().append(stack.getItem().getName()));
        }

        player.sendMessage(Text.of(" =========================="));

        return ActionResult.SUCCESS;
    }

    private void dropInventory(){
        if(getWorld().isClient()){
            return;
        }

        ItemScatterer.spawn(getWorld(), this, getInventory());
    }

    private void dropSelf(){
        if(getWorld().isClient()){
            return;
        }

        int tier = getTier();

        if(tier == 1) {
            this.dropItem(AdAstraCargoRockets.CARGO_ROCKET_TIER_1_ITEM);
            return;
        }if(tier == 2) {
            this.dropItem(AdAstraCargoRockets.CARGO_ROCKET_TIER_2_ITEM);
            return;
        }if(tier == 3) {
            this.dropItem(AdAstraCargoRockets.CARGO_ROCKET_TIER_3_ITEM);
            return;
        }if(tier == 4) {
            this.dropItem(AdAstraCargoRockets.CARGO_ROCKET_TIER_4_ITEM);
            return;
        }

        this.dropItem(Items.DIRT);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.getWorld().isClient) {
            return true;
        } else {
            dropInventory();
            dropSelf();

            this.kill();

            return true;
        }
    }

    public void killRocket() {
        this.damage(this.getDamageSources().genericKill(), Float.MAX_VALUE);
    }

    @Override
    public void tick(){
        super.tick();

        this.setPosition(this.getX(), this.getY() + this.getVelocity().y, this.getZ());
        this.checkBlockCollision();

        if(getWorld().isClient()){
            if(this.getVelocity().y < -0.1){
                boolean groundNearby = false;
                for(int i = 1; i < 30; i++){
                    BlockPos checkPos = getBlockPos().add(0, -i, 0);
                    if(!getWorld().getBlockState(checkPos).getCollisionShape(getWorld(), checkPos).isEmpty()){
                        groundNearby = true;
                        break;
                    }
                }

                if(groundNearby){
                    for(int i = 0; i < 3; i++) {
                        var flame = Registries.PARTICLE_TYPE.get(new Identifier("ad_astra", "large_flame"));
                        if (flame instanceof ParticleEffect particleEffect) {
                            getWorld().addParticle(particleEffect, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                        } else {
                            getWorld().addParticle(ParticleTypes.FLAME, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                        }

                        var smoke = Registries.PARTICLE_TYPE.get(new Identifier("ad_astra", "large_smoke"));
                        if (smoke instanceof ParticleEffect particleEffect) {
                            getWorld().addParticle(particleEffect, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                        } else {
                            getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                        }
                    }

                    if(!hasPlayedLandingSound){
                        hasPlayedLandingSound = true;
                        // Use Ad Astra sound event
                        var sound = Registries.SOUND_EVENT.get(new Identifier("ad_astra", "rocket"));
                        if(sound != null) {
                            getWorld().playSound(getX(), getY(), getZ(), sound, SoundCategory.AMBIENT, 2f, 0.5f, false);
                        } else {
                            // Fallback if not found (though user asked for 'rocket', usually it's rocket_fly or similar in ad_astra)
                             getWorld().playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 2f, 0.5f, false);
                        }
                    }
                } else {
                    hasPlayedLandingSound = false;
                }
            } else if (getLaunchTicks() > 0) {
                // Launch particles
                 for(int i = 0; i < 3; i++) {
                    var flame = Registries.PARTICLE_TYPE.get(new Identifier("ad_astra", "large_flame"));
                    if (flame instanceof ParticleEffect particleEffect) {
                        getWorld().addParticle(particleEffect, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                    } else {
                        getWorld().addParticle(ParticleTypes.FLAME, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                    }

                    var smoke = Registries.PARTICLE_TYPE.get(new Identifier("ad_astra", "large_smoke"));
                    if (smoke instanceof ParticleEffect particleEffect) {
                        getWorld().addParticle(particleEffect, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                    } else {
                        getWorld().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, getX() + (random.nextDouble() - 0.5), getY(), getZ() + (random.nextDouble() - 0.5), 0, -0.2, 0);
                    }
                }

                int ticks = getLaunchTicks();
                if (ticks == 1 || ticks == 40) {
                     var sound = Registries.SOUND_EVENT.get(new Identifier("ad_astra", "rocket"));
                     if(sound != null) {
                         getWorld().playSound(getX(), getY(), getZ(), sound, SoundCategory.AMBIENT, 2f, 0.5f, false);
                     } else {
                         getWorld().playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 2f, 0.5f, false);
                     }
                }

                hasPlayedLandingSound = false;
            } else {
                hasPlayedLandingSound = false;
            }
            return;
        }

        {
            List<CargoRocketEntity> allCargoRocketEntitiesIntersecting = getWorld().getEntitiesByClass(CargoRocketEntity.class, new Box(getBlockPos()).expand(2), CargoRocketEntity::isAlive).stream().filter(x -> x.getId() != getId()).toList();

            if(!allCargoRocketEntitiesIntersecting.isEmpty()){
                getWorld().createExplosion(
                    this,
                    getX(),
                    getY() + 2,
                    getZ(),
                    5,
                    World.ExplosionSourceType.MOB
                );

                dropInventory();
                dropSelf();

                this.kill();
            }
        }

        if(targetPlanet.isEmpty()){
            setLaunchTicks(0);
            
            // Descent Logic
            List<CargoRocketEntity> allCargoRocketEntitiesBelow = getWorld().getEntitiesByClass(CargoRocketEntity.class, new Box(getBlockPos().add(0, -3, 0)).expand(2), CargoRocketEntity::isAlive).stream().filter(x -> x.getId() != getId()).toList();

            if(!allCargoRocketEntitiesBelow.isEmpty()){
                getWorld().createExplosion(
                    this,
                    getX(),
                    getY() - 0.5f,
                    getZ(),
                    5,
                    World.ExplosionSourceType.MOB
                );

                dropInventory();
                dropSelf();

                this.kill();
                return;
            }

            // Find ground level
            Integer highestBlockY = null;
            int currentBlockY = getBlockPos().getY();
            // Scan down to find the highest block in 3x3 area
            for (int y = currentBlockY; y > currentBlockY - 30; y--) {
                boolean isBlocked = false;
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                         BlockPos checkPos = new BlockPos(getBlockPos().getX() + x, y, getBlockPos().getZ() + z);
                         if (!getWorld().getBlockState(checkPos).getCollisionShape(getWorld(), checkPos).isEmpty()) {
                             isBlocked = true;
                             break;
                         }
                    }
                    if(isBlocked) break;
                }

                if (isBlocked) {
                    highestBlockY = y;
                    break;
                }
            }

            if (highestBlockY != null) {
                double targetY = highestBlockY + 1.0d;
                double currentY = getY();
                double dist = currentY - targetY;

                if (dist <= 0.1) {
                    // Landed
                    this.setVelocity(0f, 0f, 0f);
                    this.setPosition(getX(), targetY, getZ());
                } else {
                    // Smooth descent
                    // Slow down as we get closer. 
                    // Max speed -1.0, Min speed -0.1
                    double speed = -Math.min(1.0, Math.max(0.1, dist * 0.1));
                    this.setVelocity(0f, speed, 0f);
                }
            } else {
                // No ground nearby, fall fast
                this.setVelocity(0f, -1f, 0f);
            }
        }
        else{
            // Ascent Logic
            int launchTicks = getLaunchTicks();
            setLaunchTicks(launchTicks + 1);
            
            List<CargoRocketEntity> allCargoRocketEntitiesAbove = getWorld().getEntitiesByClass(CargoRocketEntity.class, new Box(getBlockPos().add(0, +4, 0)).expand(2), CargoRocketEntity::isAlive).stream().filter(x -> x.getId() != getId()).toList();

            if(!allCargoRocketEntitiesAbove.isEmpty()){
                getWorld().createExplosion(
                    this,
                    getX(),
                    getY() - 4f,
                    getZ(),
                    5,
                    World.ExplosionSourceType.MOB
                );

                dropInventory();
                dropSelf();

                this.kill();
            }

            if(
                getWorld().getBlockState(getBlockPos().add(-1, 4, -1)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(-1, 4, 0)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(-1, 4, 1)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(0, 4, -1)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(0, 4, 0)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(0, 4, 1)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(1, 4, -1)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(1, 4, 0)).isAir() &&
                getWorld().getBlockState(getBlockPos().add(1, 4, 1)).isAir()
            ) {
                if (launchTicks < 40) {
                    // Pre-launch
                    this.setVelocity((random.nextDouble() - 0.5) * 0.05, 0f, (random.nextDouble() - 0.5) * 0.05);
                } else {
                    // Accelerate
                    double speed = Math.min(1.0, (launchTicks - 40) * 0.01);
                    this.setVelocity(0f, speed, 0f);
                }
            }
            else{
                this.setVelocity(0f, 0f, 0f);
            }

            if(getPos().y > getWorld().getTopY() + 400){
                ServerWorld targetWorld = null;

                var worlds = Objects.requireNonNull(getWorld().getServer()).getWorlds();
                for(var otherWorld : worlds){
                    if(otherWorld.getRegistryKey().getValue().toString().equals(targetPlanet)){
                        targetWorld = otherWorld;
                        break;
                    }
                }

                targetPlanet = "";

                if(targetWorld == null || targetWorld.equals(getWorld())){
                    return;
                }

                detach();

                Entity entity = this.getType().create(targetWorld);
                if (entity != null) {
                    entity.copyFrom(this);
                    entity.refreshPositionAndAngles(getX(), targetWorld.getTopY() + 200, getZ(), 0, 0);
                    entity.setVelocity(new Vec3d(0, 0, 0));
                    targetWorld.onDimensionChanged(entity);
                }

                removeFromDimension();

                ((ServerWorld)this.getWorld()).resetIdleTimeout();
                targetWorld.resetIdleTimeout();
            }
        }
    }
}
