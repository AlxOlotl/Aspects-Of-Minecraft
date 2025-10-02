package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.effect.ModEffects;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class HagfishEntity extends WaterAnimal implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public HagfishEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private int outOfWaterTicks = 0;
    private int idleTicks = 0;
    private boolean isIdle = false;

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_SPEED, 0.75f)
                .add(Attributes.ATTACK_DAMAGE, 1.0f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Normal movement
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        // Avoid predators
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Dolphin.class, 12.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Axolotl.class, 12.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Guardian.class, 12.0F, 1.2D, 1.5D));
        // Attack drowned (follow the dead)
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Drowned.class, true));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount)) {
            if (source.getEntity() instanceof LivingEntity attacker) {
                attacker.addEffect(new MobEffectInstance(ModEffects.HAGGED.get(), 200, 1));
                this.goalSelector.addGoal(0, new PanicGoal(this, 1.5D));
                this.spawnAtLocation(ModItems.HAG_GOO.get(), 1);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        this.spawnAtLocation(ModItems.HAG_GOO.get(), 2);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof Drowned) {
            target.hurt(damageSources().mobAttack(this), 1.0F); // 1 damage
            return true;
        }
        return super.doHurtTarget(target);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity instanceof Drowned) {
            return true;
        }
        return super.isAlliedTo(entity);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.isInWaterRainOrBubble()) {
            outOfWaterTicks++;
            if (outOfWaterTicks > 3600) {
                this.hurt(damageSources().drown(), 1.0F);
            }
        } else {
            outOfWaterTicks = 0;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isInWater() && this.onGround()) {
            idleTicks++;
            if (idleTicks > 1200) { // 1 minute
                isIdle = true;
            }
        } else {
            idleTicks = 0;
            isIdle = false;
        }
    }

    public boolean isIdle() {
        return this.isIdle;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller",0,this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hagfish.swim", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.hagfish.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
