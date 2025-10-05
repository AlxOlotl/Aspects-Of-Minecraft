package net.alex.aspectsofminecraft.item.custom;

import com.google.common.collect.ImmutableMap;
import net.alex.aspectsofminecraft.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ModArmorItem extends ArmorItem {
//    private static final Map<ArmorMaterial, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
//            (new ImmutableMap.Builder<ArmorMaterial, MobEffectInstance>();
    //.put(ModArmorMaterials. /Material goes here/, new MobEffectInstance(MobEffects. /Effect goes here/, /Time/, /Level/))
    //.build();

    public ModArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

//    @Override
//    public void onArmorTick(ItemStack stack, Level level, Player player) {
//        if(!level.isClientSide() && hasFullArmorSet(player)){
//            evaluateArmorEffects(player);
//        }
//    }

//    private void evaluateArmorEffects(Player player) {
//        for (Map.Entry<ArmorMaterial, MobEffectInstance> entry : MARERIAL_TO_EFFECT_MAP.entrySet()){
//            ArmorMaterial mapArmorMaterial = entry.getKey();
//            MobEffectInstance mapEffect = entry.getValue();
//
//            if (hasPlayerCorrectArmorOn(mapArmorMaterial, player)){
//                  addEfectToPlayer(player, mapEffect);
//                }
//        }
//    }

    private void addEfectToPlayer(Player player, MobEffectInstance mapEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapEffect.getEffect());

        if (!hasPlayerEffect){
            player.addEffect(new MobEffectInstance(mapEffect.getEffect(),
                    mapEffect.getDuration(), mapEffect.getAmplifier()));
        }
    }


    private boolean hasPlayerCorrectArmorOn(ArmorMaterial mapArmorMaterial, Player player) {
        for (ItemStack armorStack : player.getArmorSlots()){
            if (!(armorStack.getItem() instanceof ArmorItem)){
                return false;
            }
        }
        ArmorItem helmet = ((ArmorItem) player.getInventory().getArmor(0).getItem());
        ArmorItem chestplate = ((ArmorItem) player.getInventory().getArmor(0).getItem());
        ArmorItem leggings = ((ArmorItem) player.getInventory().getArmor(0).getItem());
        ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());

        return helmet.getMaterial() ==mapArmorMaterial && chestplate.getMaterial() ==mapArmorMaterial
                && leggings.getMaterial() ==mapArmorMaterial && boots.getMaterial() ==mapArmorMaterial;
    }

    private boolean hasFullArmorSet(Player player) {
        ItemStack helmet = player.getInventory().getArmor(0);
        ItemStack chestplate = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(0);
        ItemStack boots = player.getInventory().getArmor(0);
        return !boots.isEmpty() &&!leggings.isEmpty() &&!chestplate.isEmpty() &&!helmet.isEmpty();
    }
}
