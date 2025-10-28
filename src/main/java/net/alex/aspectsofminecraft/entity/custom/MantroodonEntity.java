package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class MantroodonEntity extends TamableAnimal implements GeoEntity, Shearable {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final EntityDataAccessor<Boolean> SHEARED =
            SynchedEntityData.defineId(MantroodonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RESTING =
            SynchedEntityData.defineId(MantroodonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ANGRY =
            SynchedEntityData.defineId(MantroodonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLLAR_COLOR =
            SynchedEntityData.defineId(MantroodonEntity.class, EntityDataSerializers.INT);

    private static final int MAX_REGROW_TIME = 20 * 60 * 3;
    private int furRegrowTimer = 0;

    public MantroodonEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 14.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHEARED, false);
        this.entityData.define(RESTING, false);
        this.entityData.define(ANGRY, false);
        this.entityData.define(COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 5.0F, 2.0F, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !MantroodonEntity.this.isResting();
            }
            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !MantroodonEntity.this.isResting();
            }
        });
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new TemptGoal(this, 0.8D, Ingredient.of(Items.CACTUS), false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !MantroodonEntity.this.isResting();
            }
        });
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.8D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !MantroodonEntity.this.isResting();
            }
        });
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, player -> this.isAngry() && !this.isTame()));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof ShearsItem && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            if (!this.isTame()) {
                this.setAngry(true);
                this.setTarget(player);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!this.isTame() && stack.is(ModItems.SPECKLEREY.get())) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && stack.getItem() instanceof DyeItem dye) {
            this.setCollarColor(dye.getDyeColor().getId());
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && stack.isEmpty() && !player.isShiftKeyDown()) {
            this.setResting(!this.isResting());
            this.getNavigation().stop();
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean readyForShearing() {
        return !this.isSheared() && this.isAlive();
    }

    @Override
    public void shear(SoundSource source) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.gameEvent(GameEvent.SHEAR, this);
        this.setSheared(true);
        this.furRegrowTimer = MAX_REGROW_TIME;

        if (!this.level().isClientSide) {
            int dropCount = 1 + this.random.nextInt(2);
            this.spawnAtLocation(ModItems.SCORCHED_MANE.get(), dropCount);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isSheared()) {
            if (furRegrowTimer > 0) {
                furRegrowTimer--;
            } else {
                this.setSheared(false);
                this.setAngry(false);
            }
        }

        if (this.isAngry() && this.getTarget() == null && this.tickCount % 100 == 0) {
            this.setAngry(false);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        var controller = state.getController();

        if (this.isResting()) {
            controller.setAnimation(RawAnimation.begin().then("animation.mantroodon.rest", Animation.LoopType.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() && this.isSheared()) {
            controller.setAnimation(RawAnimation.begin().then("animation.mantroodon.angry_sheared", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isAngry()) {
            controller.setAnimation(RawAnimation.begin().then("animation.mantroodon.angry", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isSheared()) {
            controller.setAnimation(RawAnimation.begin().then("animation.mantroodon.sheared", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (state.isMoving()) {
            controller.setAnimation(RawAnimation.begin().then("animation.mantroodon.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        controller.setAnimation(RawAnimation.begin().then("animation.mantroodon.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isResting() { return this.entityData.get(RESTING); }
    public void setResting(boolean resting) {
        this.entityData.set(RESTING, resting);
        this.setOrderedToSit(resting);
        this.getNavigation().stop();
        if (resting) this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
    }

    public boolean isSheared() { return this.entityData.get(SHEARED); }
    public void setSheared(boolean sheared) { this.entityData.set(SHEARED, sheared); }

    public boolean isAngry() { return this.entityData.get(ANGRY); }
    public void setAngry(boolean angry) { this.entityData.set(ANGRY, angry); }

    public int getCollarColor() { return this.entityData.get(COLLAR_COLOR); }
    public void setCollarColor(int color) { this.entityData.set(COLLAR_COLOR, color); }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Sheared", this.isSheared());
        tag.putBoolean("Resting", this.isResting());
        tag.putBoolean("Angry", this.isAngry());
        tag.putInt("CollarColor", this.getCollarColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setSheared(tag.getBoolean("Sheared"));
        this.setResting(tag.getBoolean("Resting"));
        this.setAngry(tag.getBoolean("Angry"));
        this.setCollarColor(tag.getInt("CollarColor"));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean success = super.doHurtTarget(target);
        if (success && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            this.level().playSound(null, this.blockPosition(), SoundEvents.BEE_STING, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
        return success;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (!this.isSheared()) this.spawnAtLocation(ModItems.SCORCHED_MANE.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.MANTROODON.get().create(level);
    }
}