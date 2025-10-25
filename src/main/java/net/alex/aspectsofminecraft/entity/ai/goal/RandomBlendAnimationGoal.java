package net.alex.aspectsofminecraft.entity.ai.goal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RandomBlendAnimationGoal<T extends Mob & GeoEntity> extends Goal {

    private static final Map<Class<?>, List<String>> CACHE = new ConcurrentHashMap<>();

    private final T entity;
    private final Random random = new Random();
    private List<String> blendAnimations = Collections.emptyList();
    private String currentAnim = null;
    private int cooldown = 0;
    private boolean isPlaying = false;

    public RandomBlendAnimationGoal(T entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (blendAnimations.isEmpty()) {
            blendAnimations = loadBlendAnimations(entity);
        }

        boolean hasTarget = entity.getTarget() != null;
        boolean hasLivingOwner = false;

        if (entity instanceof TamableAnimal tamable &&
                tamable.getOwner() != null &&
                tamable.getOwner().isAlive()) {
            hasLivingOwner = true;
        }

        return !blendAnimations.isEmpty()
                && !hasTarget
                && !hasLivingOwner
                && !entity.isAggressive()
                && cooldown-- <= 0
                && !isPlaying;
    }

    @Override
    public void start() {
        if (blendAnimations.isEmpty()) return;
        currentAnim = blendAnimations.get(random.nextInt(blendAnimations.size()));
        isPlaying = true;
        entity.triggerAnim("controller", currentAnim.substring("animation.".length()));
        cooldown = 200 + random.nextInt(200);
    }

    @Override
    public boolean canContinueToUse() {
        return isPlaying;
    }

    @Override
    public void tick() {
        if (--cooldown <= 0) {
            isPlaying = false;
        }
    }

    @Override
    public void stop() {
        isPlaying = false;
        currentAnim = null;
    }

    private static List<String> loadBlendAnimations(GeoEntity entity) {
        Entity baseEntity = (Entity) entity;
        ResourceLocation entityId = Objects.requireNonNull(
                net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(baseEntity.getType())
        );

        ResourceLocation animRes = new ResourceLocation(
                entityId.getNamespace(),
                "animations/" + entityId.getPath() + ".animation.json"
        );

        return CACHE.computeIfAbsent(entity.getClass(), c -> {
            List<String> found = new ArrayList<>();

            try (InputStreamReader reader = new InputStreamReader(
                    Objects.requireNonNull(entity.getClass().getResourceAsStream(
                            "/assets/" + animRes.getNamespace() + "/" + animRes.getPath()
                    ))
            )) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                JsonObject animations = root.getAsJsonObject("animations");

                for (String key : animations.keySet()) {
                    if (key.endsWith("_blend") && !key.contains("attack_blend")) {
                        found.add("animation." + key);
                    }
                }

                System.out.println("[RandomBlendAnimationGoal] Found " + found.size()
                        + " blend animations for " + entityId);

            } catch (Exception e) {
                System.err.println("[RandomBlendAnimationGoal] Failed to load animations for "
                        + animRes + ": " + e.getMessage());
            }
            return found;
        });
    }
}