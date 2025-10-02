package net.alex.aspectsofminecraft.effect;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.effect.custom.HaggedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Aspects.MOD_ID);

    public static final RegistryObject<MobEffect> HAGGED =
            MOB_EFFECTS.register("hagged", HaggedEffect::new);

    public static void register(IEventBus eventBus) {MOB_EFFECTS.register(eventBus);}
}
