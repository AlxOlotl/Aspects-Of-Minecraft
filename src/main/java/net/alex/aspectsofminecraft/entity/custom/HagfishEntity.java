package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.effect.ModEffects;
import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HagfishEntity extends WaterAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int outOfWaterTicks = 0;
    private int idleTicks = 0;

    public HagfishEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide) {
            Entity attacker = source.getEntity();

            if (attacker instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(ModEffects.HAGGED.get(), 200, 1));
            }
            this.spawnAtLocation(ModItems.HAG_GOO.get());
            this.getNavigation().moveTo(
                    this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D,
                    this.getY(),
                    this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D,
                    1.6D
            );
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean addEffect(MobEffectInstance effect, Entity source) {
        // Immune to Hagged effect
        if (effect.getEffect() == ModEffects.HAGGED.get()) {
            return false;
        }
        return super.addEffect(effect, source);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (!this.isInWaterOrBubble()) {
            outOfWaterTicks++;
            if (outOfWaterTicks > 3600) { // 3 minutes
                this.hurt(this.damageSources().drown(), 1.0F);
            }
        } else {
            outOfWaterTicks = 0;
        }

        // Idle tracking
        if (this.getDeltaMovement().lengthSqr() < 0.001) {
            idleTicks++;
        } else {
            idleTicks = 0;
        }
    }

    public boolean isIdleTooLong() {
        return idleTicks > 200; // 10 seconds idle = curl up
    }

    @Override
    protected void registerGoals() {
        // Swim randomly
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));

        // Avoid predators (run away)
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Dolphin.class, 8.0F, 1.0D, 1.3D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Guardian.class, 10.0F, 1.0D, 1.4D));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Axolotl.class, 8.0F, 1.0D, 1.2D));

        // Feed on drowned (follow them and nibble without aggro)
        this.goalSelector.addGoal(6, new FollowMobGoal(this, 1.0D, 5.0F, 2.0F) {
            @Override
            public boolean canUse() {
                return HagfishEntity.this.level().getNearestEntity(
                        HagfishEntity.this.level().getEntitiesOfClass(Drowned.class,
                                HagfishEntity.this.getBoundingBox().inflate(8.0D)),
                        TargetingConditions.forNonCombat(),
                        HagfishEntity.this,
                        HagfishEntity.this.getX(),
                        HagfishEntity.this.getY(),
                        HagfishEntity.this.getZ()
                ) != null;
            }

            @Override
            public void tick() {
                super.tick();
                Drowned drowned = HagfishEntity.this.level().getNearestEntity(
                        HagfishEntity.this.level().getEntitiesOfClass(Drowned.class,
                                HagfishEntity.this.getBoundingBox().inflate(2.0D)),
                        TargetingConditions.forNonCombat(),
                        HagfishEntity.this,
                        HagfishEntity.this.getX(),
                        HagfishEntity.this.getY(),
                        HagfishEntity.this.getZ()
                );
                if (drowned != null && HagfishEntity.this.tickCount % 40 == 0) { // every 2s
                    drowned.hurt(HagfishEntity.this.damageSources().mobAttack(HagfishEntity.this), 1.0F);
                }
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, state -> {
            if (!this.isInWaterOrBubble()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("HAGFISH_CURL"));
            } else if (this.isSwimming()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("HAGFISH_SWIM"));
            } else if (this.isIdleTooLong()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("HAGFISH_CURL"));
            } else {
                return state.setAndContinue(RawAnimation.begin().thenLoop("HAGFISH_IDLE"));
            }
        }));
    }

    public static void registerEntitySpawns() {
        SpawnPlacements.register(ModEntities.HAGFISH.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
