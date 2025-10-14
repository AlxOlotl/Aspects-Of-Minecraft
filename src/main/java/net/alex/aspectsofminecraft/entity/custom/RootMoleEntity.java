package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Set;

public class RootMoleEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private boolean underground = false;
    private static final Set<Block> DIGGABLE_BLOCKS = Set.of(
            Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT, Blocks.MUD,
            Blocks.GRASS_BLOCK, Blocks.SAND, Blocks.RED_SAND, Blocks.GRAVEL
    );

    public RootMoleEntity(EntityType<? extends RootMoleEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.FOLLOW_RANGE, 12.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    // ---------- Underground detection ----------
    private boolean isInDiggableBlock() {
        BlockPos pos = this.blockPosition();
        Block block = this.level().getBlockState(pos).getBlock();
        return DIGGABLE_BLOCKS.contains(block);
    }

    @Override
    public void tick() {
        super.tick();

        boolean wasUnderground = underground;
        underground = isInDiggableBlock();

        if (underground) {
            Vec3 dir = this.getDeltaMovement();
            double speed = 0.04D;

            if (this.random.nextInt(10) == 0) {
                dir = new Vec3(
                        (this.random.nextDouble() - 0.5) * 0.5,
                        (this.random.nextDouble() - 0.5) * 0.2,
                        (this.random.nextDouble() - 0.5) * 0.5
                );
            }

            this.setDeltaMovement(dir.add(0, 0, 0).normalize().scale(speed));
            this.noPhysics = true;
            this.setAirSupply(300);
        } else {
            this.noPhysics = false;
        }

        if (wasUnderground && !underground) {
            this.setDeltaMovement(Vec3.ZERO);
        }

        if (underground && level() instanceof ServerLevel server) {

            if (this.tickCount % 4 == 0) {
                var state = level().getBlockState(blockPosition());
                var dir = this.getDeltaMovement().normalize();

                double px = getX() - dir.x * 0.3;
                double py = getY() + 0.2;
                double pz = getZ() - dir.z * 0.3;

                double spread = 0.2;

                for (int i = 0; i < 6; i++) {
                    double vx = -dir.x * 0.15 + (random.nextDouble() - 0.5) * spread;
                    double vy = (random.nextDouble() - 0.5) * 0.1;
                    double vz = -dir.z * 0.15 + (random.nextDouble() - 0.5) * spread;

                    server.sendParticles(
                            new net.minecraft.core.particles.BlockParticleOption(
                                    net.minecraft.core.particles.ParticleTypes.BLOCK, state),
                            px, py, pz,
                            1,
                            0, 0, 0,
                            0.0
                    );
                }
            }
            if (this.tickCount % 8 == 0) {
                BlockPos above = blockPosition().above();
                BlockState aboveState = level().getBlockState(above);

                if (aboveState.isAir()) {
                    double sx = getX();
                    double sy = above.getY() + 0.05;
                    double sz = getZ();

                    server.sendParticles(
                            new net.minecraft.core.particles.BlockParticleOption(
                                    net.minecraft.core.particles.ParticleTypes.FALLING_DUST,
                                    level().getBlockState(blockPosition())
                            ),
                            sx, sy, sz,
                            5,
                            0.25, 0.05, 0.25,
                            0.02
                    );
                }
            }
        }
    }

    // ---------- Animation logic ----------
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        if (underground) {
            state.getController().setAnimation(
                    RawAnimation.begin().then("animation.root_mole.dig", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(
                    RawAnimation.begin().then("animation.root_mole.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(
                RawAnimation.begin().then("animation.root_mole.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (!this.level().isClientSide) {
            double dropChance = 0.05 + (looting * 0.02);
            if (this.random.nextDouble() < dropChance) {
                this.spawnAtLocation(net.alex.aspectsofminecraft.item.ModItems.STARRY_SCHNOZ.get());
            }
        }
    }

}
