package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.entity.ai.goal.RootMoleDigGoal;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class RootMoleEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public static final String CONTROLLER_NAME = "controller";
    private boolean underground = false;
    private boolean diggingDown = false;
    private boolean diggingUp = false;

    public RootMoleEntity(EntityType<? extends RootMoleEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.setPersistenceRequired();
    }

    public void clearAnimationCache() {
        var manager = this.getAnimatableInstanceCache().getManagerForId(this.getId());
        if (manager != null && manager.getAnimationControllers().containsKey(CONTROLLER_NAME)) {
            manager.getAnimationControllers().get(CONTROLLER_NAME).forceAnimationReset();
        }
    }

    // --- Attributes ---
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 12.0D);
    }

    // --- Goals ---
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RootMoleDigGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    // --- State Accessors ---
    public boolean isUnderground() { return underground; }
    public void setUnderground(boolean value) { this.underground = value; }

    public boolean isDiggingDown() { return diggingDown; }
    public void setDiggingDown(boolean value) { this.diggingDown = value; }

    public boolean isDiggingUp() { return diggingUp; }
    public void setDiggingUp(boolean value) { this.diggingUp = value; }

    // --- Physics: prevent suffocation & disable block collision underground ---
    @Override
    protected void checkInsideBlocks() {
        // Override suffocation completely — do nothing
    }

    @Override
    protected int decreaseAirSupply(int airSupply) {
        // Prevent drowning/suffocation damage
        return airSupply;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        // Immune to suffocation damage
        if (source == damageSources().inWall()) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    // --- GeckoLib Animation Setup ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<RootMoleEntity> controller =
                new AnimationController<>(this, CONTROLLER_NAME, 0, this::predicate);

        controller.setTransitionLength(0);

        controller.triggerableAnim("reset_idle",
                RawAnimation.begin().then("animation.root_mole.idle", Animation.LoopType.LOOP));
        controller.triggerableAnim("dig_down",
                RawAnimation.begin().then("animation.root_mole.dig_down", Animation.LoopType.PLAY_ONCE));
        controller.triggerableAnim("dig_up",
                RawAnimation.begin().then("animation.root_mole.dig_up", Animation.LoopType.PLAY_ONCE));

        controllerRegistrar.add(controller);
    }


    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        var controller = state.getController();

        if (controller.getCurrentAnimation() != null) {
            String name = controller.getCurrentAnimation().animation().name();
            if (name.contains("dig_up") || name.contains("dig_down")) {
                return PlayState.CONTINUE;
            }
        }

        if (isUnderground()) {
            controller.setAnimation(RawAnimation.begin().then("animation.root_mole.dig", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            controller.setAnimation(RawAnimation.begin().then("animation.root_mole.walk", Animation.LoopType.LOOP));
        } else {
            controller.setAnimation(RawAnimation.begin().then("animation.root_mole.idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // --- Safe "dig up" helper ---
    public void startDigUp() {
        var abovePos = this.blockPosition().above();
        var aboveState = this.level().getBlockState(abovePos);

        // Only dig up if there’s open space above (air or non-occluding)
        if (!aboveState.isAir() && aboveState.canOcclude()) {
            return; // solid terrain above — wait
        }

        // Move up a bit to avoid clipping
        this.setPos(this.getX(), this.getY() + 1.0D, this.getZ());
        this.triggerAnim("controller", "dig_up");
    }

    // --- Loot ---
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (!this.level().isClientSide) {
            double dropChance = 0.05 + (looting * 0.02);
            if (this.random.nextDouble() < dropChance) {
                this.spawnAtLocation(ModItems.STARRY_SCHNOZ.get());
            }
        }
    }
}
