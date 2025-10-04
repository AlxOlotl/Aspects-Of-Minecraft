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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public class HagfishEntity extends WaterAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private int outOfWaterTicks = 0;
    private int idleTicks = 0;
    private boolean isIdle = false;
    public float prevRenderPitch = 0.0F;
    public float renderPitch = 0.0F;

    // Curling state
    private boolean curled = false;
    public int curlTicks = 0;

    public HagfishEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new HagfishMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_SPEED, 0.75f)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.FOLLOW_RANGE, 32.0f);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Normal movement
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(1, new CurlOnLandGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        // Avoid predators
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Dolphin.class, 12.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Axolotl.class, 12.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Guardian.class, 12.0F, 1.2D, 1.5D));
        // Attack drowned (follow the dead)
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Drowned.class, true));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));

        // Curling behavior
        this.goalSelector.addGoal(5, new CurlAtBottomGoal(this));
    }

    // === Damage & Loot ===
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
            target.hurt(damageSources().mobAttack(this), 1.0F);
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

    // === Tick Logic ===
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
            if (idleTicks > 1200) { // 1 min
                isIdle = true;
            }
        } else {
            idleTicks = 0;
            isIdle = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater()) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.lengthSqr() > 0.0001) {
                float horizontalSpeed = (float)Math.sqrt(motion.x * motion.x + motion.z * motion.z);
                this.setXRot((float)(-(Mth.atan2(motion.y, horizontalSpeed) * (180F / Math.PI))));
            }
        }
    }


    // === Curl State ===
    public boolean isIdle() {
        return this.isIdle;
    }

    public boolean isCurled() {
        return this.curled;
    }

    public void setCurled(boolean curled) {
        this.curled = curled;
        this.curlTicks = 0;
    }

    // === GeckoLib Animations ===
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if (this.isCurled()) {
            state.getController().setAnimation(
                    RawAnimation.begin().then("animation.hagfish.curl", Animation.LoopType.LOOP)
            );
            return PlayState.CONTINUE;
        }


        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.hagfish.swim", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        state.getController().setAnimation(RawAnimation.begin().then("animation.hagfish.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static class HagfishMoveControl extends MoveControl {
        private final HagfishEntity hagfish;

        public HagfishMoveControl(HagfishEntity hagfish) {
            super(hagfish);
            this.hagfish = hagfish;
        }

        @Override
        public void tick() {
            if (hagfish.isInWater() && !hagfish.isCurled()) {
                Vec3 motion = hagfish.getDeltaMovement();

                // Neutral buoyancy control: stop floating endlessly upward
                if (motion.y < -0.01D) {
                    // Rising too slowly or falling - add slight buoyancy
                    hagfish.setDeltaMovement(motion.add(0.0D, 0.002D, 0.0D));
                } else if (motion.y > 0.01D) {
                    // Rising too fast - dampen upward motion
                    hagfish.setDeltaMovement(motion.add(0.0D, -0.0025D, 0.0D));
                }

                // Handle navigation and turning
                if (this.operation == Operation.MOVE_TO && !hagfish.getNavigation().isDone()) {
                    double dx = this.wantedX - hagfish.getX();
                    double dy = this.wantedY - hagfish.getY();
                    double dz = this.wantedZ - hagfish.getZ();
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    if (dist > 1e-4) {
                        dy /= dist;
                        float speed = (float)(this.speedModifier * hagfish.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        hagfish.setDeltaMovement(hagfish.getDeltaMovement().add(
                                dx / dist * speed * 0.05D,
                                dy * speed * 0.05D,
                                dz / dist * speed * 0.05D
                        ));
                        hagfish.setYRot(-((float)Math.atan2(hagfish.getDeltaMovement().x, hagfish.getDeltaMovement().z)) * (180F / (float)Math.PI));
                        hagfish.yBodyRot = hagfish.getYRot();
                    }
                }

                // Smooth pitch tilting like dolphin
                motion = hagfish.getDeltaMovement();
                if (motion.lengthSqr() > 0.0001D) {
                    float horizontalSpeed = (float)Math.sqrt(motion.x * motion.x + motion.z * motion.z);
                    float targetPitch = (float)(-(Mth.atan2(motion.y, horizontalSpeed) * (180F / Math.PI)));
                    hagfish.setXRot(Mth.lerp(0.1F, hagfish.getXRot(), targetPitch));
                }

            } else {
                super.tick();
            }
        }
    }

    // Curl goal
    static class CurlAtBottomGoal extends Goal {
        private final HagfishEntity hagfish;
        private int duration;

        public CurlAtBottomGoal(HagfishEntity hagfish) {
            this.hagfish = hagfish;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return hagfish.isInWater() && !hagfish.isCurled() && hagfish.getRandom().nextInt(2000) == 0;
        }

        @Override
        public void start() {
            hagfish.getNavigation().stop();
            hagfish.setCurled(true);
            duration = 20 * (180 + hagfish.getRandom().nextInt(120)); // 3–5 minutes
        }

        @Override
        public boolean canContinueToUse() {
            return hagfish.isCurled() && hagfish.curlTicks < duration;
        }

        @Override
        public void tick() {
            hagfish.setDeltaMovement(0, -0.01D, 0); // hug bottom
            hagfish.curlTicks++;
        }

        @Override
        public void stop() {
            hagfish.setCurled(false);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

}
