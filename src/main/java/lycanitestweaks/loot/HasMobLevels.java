package lycanitestweaks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.LycanitesTweaks;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

/*

    *** Checks Mob Levels if BaseCreatureEntity
    *** Else returns true otherwise

    *** Minimum Mob Levels ***
    "conditions": [
        {
            "condition": "lycanitestweaks:has_mob_levels",
            "min": 10
        }
    ]

    *** Range of Mob Levels ***
    "conditions": [
        {
            "condition": "lycanitestweaks:has_mob_levels",
            "min": 50,
            "max": 100
        }
    ]
*/
public class HasMobLevels implements LootCondition {

    private final int min;
    private final int max;

    public HasMobLevels(int min) {
        this(min, -1);
    }

    public HasMobLevels(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        if(context.getLootedEntity() instanceof BaseCreatureEntity){
            BaseCreatureEntity creature = (BaseCreatureEntity)context.getLootedEntity();

            if(creature.isMinion()) return false;
            if(this.min > 0 && creature.getLevel() < this.min) return false;
            if(this.max > 0 && creature.getLevel() > this.max) return false;
            return true;
        }
        return true;
    }

    public static class Serializer extends LootCondition.Serializer<HasMobLevels>{

        public Serializer() {
            super(new ResourceLocation(LycanitesTweaks.MODID + ":" + "has_mob_levels"), HasMobLevels.class);
        }

        public void serialize(JsonObject json, HasMobLevels value, JsonSerializationContext context) {
            if(value.min > 0) json.add("min", context.serialize(value.min));
            if(value.max > 0) json.add("max", context.serialize(value.max));
        }

        public HasMobLevels deserialize(JsonObject json, JsonDeserializationContext context) {
            return new HasMobLevels(JsonUtils.getInt(json, "min", -1), JsonUtils.getInt(json, "max", -1));
        }
    }
}
