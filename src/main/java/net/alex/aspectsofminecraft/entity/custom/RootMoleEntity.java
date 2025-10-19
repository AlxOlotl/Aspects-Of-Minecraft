package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.entity.ai.goal.RootMoleDigGoal;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class RootMoleEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private boolean diggingDown = false;
    private boolean diggingUp = false;
    private boolean underground = false;

    public RootMoleEntity(EntityType<? extends RootMoleEntity> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 12.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new RootMoleDigGoal(this)); // Digging logic
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.1D, Ingredient.of(Items.POTATO), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.ROOT_MOLE.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.POTATO);
    }

    public boolean isDiggingDown() {
        return diggingDown;
    }

    public void setDiggingDown(boolean diggingDown) {
        this.diggingDown = diggingDown;
    }

    public boolean isDiggingUp() {
        return diggingUp;
    }

    public void setDiggingUp(boolean diggingUp) {
        this.diggingUp = diggingUp;
    }

    public boolean isUnderground() {
        return underground;
    }

    public void setUnderground(boolean underground) {
        this.underground = underground;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        var controller = state.getController();

        if (this.isDiggingDown()) {
            controller.setAnimation(
                    RawAnimation.begin().then("animation.root_mole.dig_down", Animation.LoopType.PLAY_ONCE)
            );
            return PlayState.CONTINUE;
        }

        if (this.isUnderground()) {
            controller.setAnimation(
                    RawAnimation.begin().then("animation.root_mole.dig", Animation.LoopType.LOOP)
            );
            return PlayState.CONTINUE;
        }

        if (this.isDiggingUp()) {
            controller.setAnimation(
                    RawAnimation.begin().then("animation.root_mole.dig_up", Animation.LoopType.PLAY_ONCE)
            );
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            controller.setAnimation(
                    RawAnimation.begin().then("animation.root_mole.walk", Animation.LoopType.LOOP)
            );
        } else {
            controller.setAnimation(
                    RawAnimation.begin().then("animation.root_mole.idle", Animation.LoopType.LOOP)
            );
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void checkInsideBlocks() {
        if (this.isUnderground() || this.isDiggingDown() || this.isDiggingUp()) return;
        super.checkInsideBlocks();
    }

    @Override
    protected int decreaseAirSupply(int airSupply) {
        if (this.isUnderground() || this.isDiggingDown() || this.isDiggingUp()) return airSupply;
        return super.decreaseAirSupply(airSupply);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if ((this.isUnderground() || this.isDiggingDown() || this.isDiggingUp())
                && source == this.damageSources().inWall()) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    //  Drops & Sounds
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

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.RABBIT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }
}
