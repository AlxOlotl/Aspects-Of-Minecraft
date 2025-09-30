package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.block.custom.HagGooLayerBlock;
import net.alex.aspectsofminecraft.effect.ModEffects;
import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HagGooProjectileEntity extends ThrowableItemProjectile {
    private int bounceCount = 0;

    public HagGooProjectileEntity(EntityType<? extends HagGooProjectileEntity> type, Level level) {
        super(type, level);
    }

    public HagGooProjectileEntity(Level level, LivingEntity thrower) {
        super(ModEntities.HAG_GOO_PROJECTILE.get(), thrower, level);
    }

    public HagGooProjectileEntity(Level level, double x, double y, double z) {
        super(ModEntities.HAG_GOO_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.HAG_GOO.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) return;
        if (result instanceof BlockHitResult bhr) {
            BlockPos hitPos = bhr.getBlockPos().relative(bhr.getDirection());
            if (this.isInWaterOrBubble()) {
                placeGoo(hitPos);
                this.discard();
                return;
            }
            if (bounceCount == 0) {
                placeGoo(hitPos);
                bounce(bhr.getDirection());
                bounceCount++;
            } else if (bounceCount == 1) {
                placeGoo(hitPos);
                placeGoo(hitPos.north());
                placeGoo(hitPos.south());
                placeGoo(hitPos.east());
                placeGoo(hitPos.west());
                this.discard();}}
    }



    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity target = result.getEntity();
        if (!this.level().isClientSide && target instanceof LivingEntity living) {
            living.hurt(this.damageSources().thrown(this, this.getOwner()), 1.0F);
            living.addEffect(new MobEffectInstance(ModEffects.HAGGED.get(), 200));
        }
        this.discard();
    }


    private void placeGoo(BlockPos pos) {
        BlockState gooLayer = ModBlocks.HAG_GOO_LAYER.get()
                .defaultBlockState()
                .setValue(HagGooLayerBlock.PROJECTILE_PLACED, true);
        if (level().getBlockState(pos).isAir()) {
            level().setBlockAndUpdate(pos, gooLayer);
        }
    }

    private void bounce(Direction hitDir) {
        Vec3 motion = this.getDeltaMovement();
        if (hitDir == Direction.UP || hitDir == Direction.DOWN) {
            motion = new Vec3(motion.x, -motion.y * 0.7, motion.z);
        } else if (hitDir == Direction.EAST || hitDir == Direction.WEST) {
            motion = new Vec3(-motion.x * 0.7, motion.y, motion.z);
        } else if (hitDir == Direction.NORTH || hitDir == Direction.SOUTH) {
            motion = new Vec3(motion.x, motion.y, -motion.z * 0.7);
        }
        this.setDeltaMovement(motion);
        this.setPos(
                this.getX() + hitDir.getStepX() * 0.1,
                this.getY() + hitDir.getStepY() * 0.1,
                this.getZ() + hitDir.getStepZ() * 0.1
        );
    }


}
