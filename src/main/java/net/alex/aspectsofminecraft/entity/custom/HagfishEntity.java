package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.effect.ModEffects;
import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.entity.ai.goal.CurlOnLandGoal;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public class HagfishEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private int outOfWaterTicks = 0;
    private float curlProgress = 0.0F;
    private static final float CURL_SPEED = 0.05F;
    private boolean curled = false;
    public int curlTicks = 0;
    public float prevRenderPitch = 0.0F;
    public float renderPitch = 0.0F;
    public float prevTurnAmount = 0.0F;
    public float turnAmount = 0.0F;

    public HagfishEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new HagfishMoveControl(this);
    }

    // --- Attributes ---
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 1.5)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    // --- Goals ---
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(1, new CurlOnLandGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Dolphin.class, 10.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Axolotl.class, 10.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Guardian.class, 10.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(3, new CurlAtBottomGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity target) {
                return 1.5F + target.getBbWidth();
            }
        });
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        // Targeting
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Drowned.class, true));
    }

    // --- Tick ---
    @Override
    public void tick() {
        super.tick();

        // Turning interpolation
        float deltaYaw = Mth.wrapDegrees(this.getYRot() - this.yRotO);
        prevTurnAmount = turnAmount;
        turnAmount = Mth.clamp(deltaYaw / 45F, -1F, 1F);

        boolean inWater = this.isInWaterRainOrBubble();
        Vec3 motion = this.getDeltaMovement();

        // Curl state transitions
        if (!inWater) curlProgress = Math.min(1.0F, curlProgress + CURL_SPEED);
        else curlProgress = Math.max(0.0F, curlProgress - CURL_SPEED);

        if (!inWater) {
            this.setDeltaMovement(Vec3.ZERO);
            if (curlProgress > 0.8F) this.setCurled(true);
        } else if (inWater && curlProgress < 0.2F) {
            this.setCurled(false);
        }

        // Render pitch (axolotl-like tilt)
        this.prevRenderPitch = this.renderPitch;
        if (inWater && !this.isCurled() && motion.lengthSqr() > 0.0001) {
            float horizontalSpeed = (float)Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            float targetPitch = (float)(-(Mth.atan2(motion.y, horizontalSpeed) * (180F / Math.PI)));
            this.renderPitch = Mth.lerp(0.1F, this.renderPitch, targetPitch);
            this.setXRot(this.renderPitch);
        } else {
            this.renderPitch = Mth.lerp(0.1F, this.renderPitch, 0.0F);
        }

        // Out-of-water survival logic (slower damage)
        if (!inWater) {
            outOfWaterTicks++;
            if (outOfWaterTicks > 7200) { // 6 minutes
                this.hurt(damageSources().drown(), 1.0F);
                outOfWaterTicks = 0;
            }
        } else {
            outOfWaterTicks = 0;
        }
    }

    // --- Animation setup ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if (this.isCurled() || this.curlProgress > 0.2F) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.hagfish.curl", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.hagfish.swim", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        state.getController().setAnimation(RawAnimation.begin().then("animation.hagfish.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
    @Override public boolean isFood(ItemStack stack) { return stack.is(Items.ROTTEN_FLESH); }
    @Override public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return ModEntities.HAGFISH.get().create(level); }

    // --- Utility ---
    public boolean isCurled() { return this.curled; }
    public void setCurled(boolean curled) { this.curled = curled; this.curlTicks = 0; }

    @Override public boolean isAggressive() { return true; }
    @Override public boolean canBreatheUnderwater() { return true; }
    @Override public boolean removeWhenFarAway(double dist) { return false; }
    @Override protected boolean shouldDespawnInPeaceful() { return false; }
    @Override public int getMaxAirSupply() { return 999999; }
    @Override protected int decreaseAirSupply(int airSupply) { return airSupply; }

    // --- Move Control ---
    static class HagfishMoveControl extends MoveControl {
        private final HagfishEntity hagfish;
        public HagfishMoveControl(HagfishEntity hagfish) { super(hagfish); this.hagfish = hagfish; }

        @Override
        public void tick() {
            if (hagfish.isInWater() && !hagfish.isCurled()) {
                if (this.operation == Operation.MOVE_TO && !hagfish.getNavigation().isDone()) {
                    double dx = this.wantedX - hagfish.getX();
                    double dy = this.wantedY - hagfish.getY();
                    double dz = this.wantedZ - hagfish.getZ();
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist > 1e-4) {
                        float speed = (float)(this.speedModifier * hagfish.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        hagfish.setDeltaMovement(hagfish.getDeltaMovement().add(
                                dx / dist * speed * 0.05D,
                                dy / dist * speed * 0.05D,
                                dz / dist * speed * 0.05D
                        ));
                        hagfish.setYRot(-((float)Math.atan2(hagfish.getDeltaMovement().x, hagfish.getDeltaMovement().z)) * (180F / (float)Math.PI));
                        hagfish.yBodyRot = hagfish.getYRot();
                    }
                }
            } else {
                hagfish.setDeltaMovement(Vec3.ZERO);
                if (!hagfish.isCurled()) hagfish.setCurled(true);
                super.tick();
            }
        }
    }

    // --- Curl At Bottom Goal ---
    static class CurlAtBottomGoal extends Goal {
        private final HagfishEntity hagfish;
        private int duration;

        public CurlAtBottomGoal(HagfishEntity hagfish) {
            this.hagfish = hagfish;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override public boolean canUse() { return hagfish.isInWater() && !hagfish.isCurled() && hagfish.getRandom().nextInt(2500) == 0; }
        @Override public void start() { hagfish.getNavigation().stop(); hagfish.setCurled(true); duration = 20 * (60 + hagfish.getRandom().nextInt(60)); }
        @Override public boolean canContinueToUse() { return hagfish.isCurled() && hagfish.curlTicks < duration; }
        @Override public void tick() { hagfish.setDeltaMovement(0, -0.01D, 0); hagfish.curlTicks++; }
        @Override public void stop() { hagfish.setCurled(false); }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (!result) return false;

        if (this.level().isClientSide) return true;

        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModEffects.HAGGED.get(), 200, 1));
            if (this.random.nextInt(3) == 0) {
                this.spawnAtLocation(ModItems.HAG_GOO.get());
            }
            if (living instanceof Drowned drowned) {
                drowned.setLastHurtByMob(null);
                drowned.setTarget(null);
            }}
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (!this.level().isClientSide) {
            int count = 1 + this.random.nextInt(2 + looting);
            for (int i = 0; i < count; i++) {
                this.spawnAtLocation(ModItems.HAG_GOO.get());
            }
        }
    }
}

