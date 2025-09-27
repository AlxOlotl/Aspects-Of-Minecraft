package net.alex.aspectsofminecraft.effect;

import net.alex.aspectsofminecraft.Aspects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Aspects.MODID);

    public static final RegistryObject<MobEffect> HAGGED =
            MOB_EFFECTS.register("hagged", HaggedEffect::new);
}
