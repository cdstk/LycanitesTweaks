package lycanitestweaks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.Variant;
import lycanitestweaks.LycanitesTweaks;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

public class RandomChanceWithVariantDropScale implements LootCondition {

    private final float chance;
    private final float lootingMultiplier;
    private final boolean applyRareToNBTBosses;

    public RandomChanceWithVariantDropScale(float chanceIn, float lootingMultiplierIn, boolean applyRareToNBTBosses) {
        this.chance = chanceIn;
        this.lootingMultiplier = lootingMultiplierIn;
        this.applyRareToNBTBosses = applyRareToNBTBosses;
    }

    public boolean testCondition(Random rand, LootContext context) {
        int lootingBonus = context.getLootingModifier();
        int variantBonus = 1;

        if (context.getLootedEntity() instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity)context.getLootedEntity();

            if(creature.isRareVariant()) variantBonus = Variant.RARE_DROP_SCALE;
            else if(creature.spawnedAsBoss && this.applyRareToNBTBosses) variantBonus = Variant.RARE_DROP_SCALE;
            else if(creature.getVariantIndex() != 0) variantBonus = Variant.UNCOMMON_DROP_SCALE;
        }

        return rand.nextFloat() < (this.chance + (float)lootingBonus * this.lootingMultiplier) * variantBonus;
    }

    public static class Serializer extends LootCondition.Serializer<RandomChanceWithVariantDropScale>
    {
        public Serializer() {
            super(new ResourceLocation(LycanitesTweaks.MODID + ":" + "random_chance_With_Variant_Drop_Scale"), RandomChanceWithVariantDropScale.class);
        }

        public void serialize(JsonObject json, RandomChanceWithVariantDropScale value, JsonSerializationContext context) {
            json.addProperty("chance", value.chance);
            json.addProperty("looting_multiplier", value.lootingMultiplier);
            json.addProperty("applyRareToNBTBosses", value.applyRareToNBTBosses);
        }

        public RandomChanceWithVariantDropScale deserialize(JsonObject json, JsonDeserializationContext context) {
            return new RandomChanceWithVariantDropScale(
                    JsonUtils.getFloat(json, "chance", 1F),
                    JsonUtils.getFloat(json, "looting_multiplier", 0F),
                    JsonUtils.getBoolean(json, "applyRareToNBTBosses", false)
            );
        }
    }
}
