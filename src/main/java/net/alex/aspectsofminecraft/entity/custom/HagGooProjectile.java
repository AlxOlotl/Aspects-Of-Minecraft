package net.alex.aspectsofminecraft.entity.custom;

import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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
        return ModItems.HAG_GOO.get(); // Or your custom "hag goo" item if needed
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {

        Vec3 motion = this.getDeltaMovement();

        Direction face = result.getDirection();
        double bounceFactor = 0.25;

        double x = motion.x;
        double y = motion.y;
        double z = motion.z;

        if (face.getAxis() == Direction.Axis.X) {
            x = -x * bounceFactor;
            y = y * bounceFactor;
            z = z * bounceFactor;
        } else if (face.getAxis() == Direction.Axis.Y) {
            x = x * bounceFactor;
            y = -y * bounceFactor;
            z = z * bounceFactor;
        } else if (face.getAxis() == Direction.Axis.Z) {
            x = x * bounceFactor;
            y = y * bounceFactor;
            z = -z * bounceFactor;
        }

        this.setDeltaMovement(new Vec3(x, y, z));

        bounceCount++;

//        Block gooBlock = ModBlocks.HAG_GOO_LAYER.get();
        Level level = this.level();
        BlockPos pos = result.getBlockPos().above();

        if (bounceCount == 1) {
            // First bounce: place a single goo layer
            if (level.isEmptyBlock(pos)) {
                level.setBlock(pos, gooBlock.defaultBlockState(), 3);
            }
        } else if (bounceCount >= 2) {
            // Second bounce: place plus pattern
            BlockPos[] positions = new BlockPos[]{
                    pos, pos.north(), pos.south(), pos.east(), pos.west()
            };

            for (BlockPos p : positions) {
                if (level.isEmptyBlock(p)) {
                    level.setBlock(p, gooBlock.defaultBlockState(), 3);
                }
            }

            // Remove projectile after final bounce
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
