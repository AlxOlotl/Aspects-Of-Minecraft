package net.alex.aspectsofminecraft.entity.custom;

import net.minecraft.network.protocol.Packet;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class HagGooProjectile extends ThrowableItemProjectile {

    private int bounceCount = 0;
    private static final int MAX_BOUNCES = 2;

    public HagGooProjectile(EntityType<? extends HagGooProjectile> type, Level level) {
        super(type, level);
    }

    public HagGooProjectile(EntityType<? extends HagGooProjectile> type, Level level, net.minecraft.world.entity.LivingEntity thrower) {
        super(type, thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL; // Or your custom "hag goo" item if needed
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (result.getType() == HitResult.Type.BLOCK) {
            bounceCount++;

            if (bounceCount <= MAX_BOUNCES) {
                // Bounce logic
                BlockPos pos = ((BlockHitResult) result).getBlockPos().above();
                if (this.level().getBlockState(pos).isAir()) {
                    this.level().setBlock(pos, ModBlocks.HAG_GOO_LAYER.get().defaultBlockState(), 3);
                }
                BlockHitResult blockHit = (BlockHitResult) result;

                // Reflect motion based on impact normal
                switch (blockHit.getDirection()) {
                    case UP, DOWN -> setDeltaMovement(getDeltaMovement().x, -getDeltaMovement().y * 0.6, getDeltaMovement().z);
                    case NORTH, SOUTH -> setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y, -getDeltaMovement().z * 0.6);
                    case EAST, WEST -> setDeltaMovement(-getDeltaMovement().x * 0.6, getDeltaMovement().y, getDeltaMovement().z);
                }
            } else {
                discard(); // Remove the projectile after 2 bounces
            }
        } else if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) result;
            entityHit.getEntity().hurt(damageSources().thrown(this, this.getOwner()), 2.0F);
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Bounces", bounceCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        bounceCount = tag.getInt("Bounces");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
