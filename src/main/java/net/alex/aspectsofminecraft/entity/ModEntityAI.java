package net.alex.aspectsofminecraft.entity;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aspects.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntityAI {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Dolphin dolphin) {
            dolphin.targetSelector.addGoal(1,
                    new NearestAttackableTargetGoal<>(dolphin, HagfishEntity.class, true));
        }
        if (event.getEntity() instanceof Guardian guardian) {
            guardian.targetSelector.addGoal(1,
                    new NearestAttackableTargetGoal<>(guardian, HagfishEntity.class, true));
        }
        if (event.getEntity() instanceof Axolotl axolotl) {
            axolotl.targetSelector.addGoal(1,
                    new NearestAttackableTargetGoal<>(axolotl, HagfishEntity.class, true));
        }
    }
}
