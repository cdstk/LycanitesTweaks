package lycanitestweaks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.LycanitesTweaks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

/*

    *** Modeled after looting_enchant

    *** "count" - Required, specifies minimum OR minimum AND maximum.
    *** "scale" - Optional, multiplies with creature's levels
    *** "limit" - Optional, maximum items

    "functions": [
        {
            "function": "lycanitestweaks:scale_with_mob_levels",
            "count": {
                "min": 0,
                "max": 1
            },
            "limit": 10
        }
    ]

 */

public class ScaleWithMobLevels extends LootFunction {

    private final float scale;
    private final int limit;

    public ScaleWithMobLevels(LootCondition[] conditionsIn, float scale, int limit) {
        super(conditionsIn);
        this.scale = scale;
        this.limit = limit;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (context.getLootedEntity() instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity)context.getLootedEntity();

            int newCount = (int) (stack.getCount() * this.scale * creature.getLevel());
            if(this.limit > 0) newCount = Math.min(this.limit, newCount); //max set to limit, otherwise set to currCount x scale x moblvl

            stack.setCount(newCount);
        }

        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<ScaleWithMobLevels> {

        public Serializer() {
            super(new ResourceLocation(LycanitesTweaks.MODID + ":" + "scale_with_mob_levels"), ScaleWithMobLevels.class);
        }

        public void serialize(JsonObject object, ScaleWithMobLevels functionClazz, JsonSerializationContext serializationContext) {
            object.add("scale", serializationContext.serialize(functionClazz.scale));
        }

        public ScaleWithMobLevels deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            float scale = JsonUtils.getFloat(object, "scale", 1.0F);
            int limit = JsonUtils.getInt(object, "limit", 0);
            return new ScaleWithMobLevels(conditionsIn, scale, limit);
        }
    }
}
