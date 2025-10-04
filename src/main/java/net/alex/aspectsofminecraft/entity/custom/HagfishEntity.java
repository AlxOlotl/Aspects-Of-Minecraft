package net.alex.aspectsofminecraft.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;

public class HagfishEntity extends WaterAnimal {
    public HagfishEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_SPEED, 0.75f)
                .add(Attributes.ATTACK_DAMAGE, 1.0f);
    }


}
