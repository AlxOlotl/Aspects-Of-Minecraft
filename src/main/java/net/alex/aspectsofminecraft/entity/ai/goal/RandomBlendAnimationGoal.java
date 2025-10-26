package net.alex.aspectsofminecraft.entity.ai.goal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;

import java.io.InputStreamReader;
import java.util.*;

public class RandomBlendAnimationGoal<T extends Mob & GeoEntity> extends Goal {
    private final T entity;
    private final Random random = new Random();

    private static final Map<Class<?>, List<String>> BLEND_CACHE = new HashMap<>();
    private static final Map<Class<?>, Map<String, Float>> LENGTH_CACHE = new HashMap<>();

    private int cooldownTicks = 0;
    private boolean playing = false;
    private String currentAnim = null;

    public RandomBlendAnimationGoal(T entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (entity.level().isClientSide) return false;
        if (playing) return false;
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return false;
        }

        if (entity.getTarget() != null && entity.getTarget().isAlive()) return false;

        boolean inWater = entity.isInWaterOrBubble();
        boolean onGround = entity.onGround();
        boolean flying = !onGround && !inWater && entity.getDeltaMovement().y > 0.05D;

        String statePrefix = flying ? "air_blend" : inWater ? "water_blend" : "land_blend";

        double moveSq = entity.getDeltaMovement().lengthSqr();
        if (moveSq > 0.5D) return false;

        List<String> blends = loadBlendAnimations(entity, statePrefix);
        return !blends.isEmpty() && random.nextInt(200) == 0;
    }

    @Override
    public void start() {
        String statePrefix = getCurrentStatePrefix();
        List<String> blends = loadBlendAnimations(entity, statePrefix);
        if (blends.isEmpty()) return;

        String chosen = blends.get(random.nextInt(blends.size()));
        this.currentAnim = chosen;
        this.playing = true;

        float length = LENGTH_CACHE
                .getOrDefault(entity.getClass(), Map.of())
                .getOrDefault(chosen, 3.0f);

        this.cooldownTicks = (int) (length * 20) + 40 + random.nextInt(60);
        entity.triggerAnim("controller", chosen);
    }

    @Override
    public boolean canContinueToUse() {
        return playing;
    }

    @Override
    public void tick() {
        if (cooldownTicks-- <= 0) {
            playing = false;
        }
    }

    private static List<String> loadBlendAnimations(GeoEntity geoEntity, String prefix) {
        Entity base = (Entity) geoEntity;
        ResourceLocation entityId = Objects.requireNonNull(
                ForgeRegistries.ENTITY_TYPES.getKey(base.getType())
        );

        return BLEND_CACHE.computeIfAbsent(geoEntity.getClass(), c -> {
                    List<String> found = new ArrayList<>();
                    Map<String, Float> lengths = new HashMap<>();

                    ResourceLocation animRes = new ResourceLocation(
                            entityId.getNamespace(),
                            "animations/" + entityId.getPath() + ".animation.json"
                    );

                    try (InputStreamReader reader = new InputStreamReader(
                            Objects.requireNonNull(geoEntity.getClass().getResourceAsStream(
                                    "/assets/" + animRes.getNamespace() + "/" + animRes.getPath()
                            ))
                    )) {
                        JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                        JsonObject animations = root.getAsJsonObject("animations");

                        for (String key : animations.keySet()) {
                            if (key.endsWith("_blend") && !key.contains("attack_blend")) {
                                String animName = "animation." + key;
                                found.add(animName);

                                JsonObject anim = animations.getAsJsonObject(key);
                                if (anim.has("animation_length")) {
                                    lengths.put(animName, anim.get("animation_length").getAsFloat());
                                }
                            }
                        }

                        LENGTH_CACHE.put(geoEntity.getClass(), lengths);
                        System.out.println("[RandomBlendAnimationGoal] Loaded "
                                + found.size() + " blend animations for " + entityId);

                    } catch (Exception e) {
                        System.err.println("[RandomBlendAnimationGoal] Failed to load animations for "
                                + animRes + ": " + e.getMessage());
                    }

                    return found;
                }).stream()
                .filter(name -> name.contains(prefix))
                .toList();
    }

    private String getCurrentStatePrefix() {
        boolean inWater = entity.isInWaterOrBubble();
        boolean onGround = entity.onGround();
        boolean flying = !onGround && !inWater && entity.getDeltaMovement().y > 0.05D;

        return flying ? "air_blend" : inWater ? "water_blend" : "land_blend";
    }
}