package lycanitestweaks.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.LycanitesTweaks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

/*

    *** "treasure" - Optional, default false
    *** "scale" - Optional, multiplies with creature's levels, default 1.0F

    "functions": [
        {
            "function": "lycanitestweaks:enchant_with_mob_levels",
            "base_levels": 30,
            "treasure": true,
            "scale": 0.5
        }
    ]

 */

public class EnchantWithMobLevels extends LootFunction {

    private final RandomValueRange randomBaseLevel;
    private final boolean isTreasure;
    private final float scale;

    public EnchantWithMobLevels(LootCondition[] conditionsIn, RandomValueRange randomBaseRange, boolean isTreasureIn, float scale) {
        super(conditionsIn);
        this.randomBaseLevel = randomBaseRange;
        this.isTreasure = isTreasureIn;
        this.scale = scale;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if(context.getLootedEntity() instanceof BaseCreatureEntity) {
            int levels = (int)(((BaseCreatureEntity)context.getLootedEntity()).getLevel() * this.scale) + this.randomBaseLevel.generateInt(rand);
            return this.addRandomEnchantmentUntilSuccess(rand, stack, levels, this.isTreasure);
        }
        else return stack;
    }

    // Attempts to have at least one ench without high levels resulting in the same enchants, average 1/2 level loss per loop
    public ItemStack addRandomEnchantmentUntilSuccess(Random rand, ItemStack stack, int levels, boolean isTreasure){
        ItemStack loot = EnchantmentHelper.addRandomEnchantment(rand, stack, levels, isTreasure);
        boolean emptyEnchant = (loot.getItem() == Items.ENCHANTED_BOOK) ? !loot.hasTagCompound() : loot.getEnchantmentTagList().isEmpty();

        if(emptyEnchant && levels > 0) {
            int reducedLevels = (int)(levels * MathHelper.clamp(Math.round(rand.nextFloat()), 0.25F, 0.75F ));
            return addRandomEnchantmentUntilSuccess(rand, stack, reducedLevels, isTreasure);
        }
        return loot;
    }

    public static class Serializer extends LootFunction.Serializer<EnchantWithMobLevels> {

        public Serializer() {
            super(new ResourceLocation(LycanitesTweaks.MODID + ":" + "enchant_with_mob_levels"), EnchantWithMobLevels.class);
        }

        public void serialize(JsonObject object, EnchantWithMobLevels functionClazz, JsonSerializationContext serializationContext) {
            object.add("base_levels", serializationContext.serialize(functionClazz.randomBaseLevel));
            object.add("treasure", serializationContext.serialize(functionClazz.isTreasure));
            object.add("scale", serializationContext.serialize(functionClazz.scale));
        }

        public EnchantWithMobLevels deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            RandomValueRange randomvaluerange = JsonUtils.deserializeClass(object, "base_levels", new RandomValueRange(0), deserializationContext, RandomValueRange.class);
            boolean isTreasure = JsonUtils.getBoolean(object, "treasure", false);
            float scale = JsonUtils.getFloat(object, "scale", 1.0F);
            return new EnchantWithMobLevels(conditionsIn, randomvaluerange, isTreasure, scale);
        }
    }
}
