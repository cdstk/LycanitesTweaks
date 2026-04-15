package lycanitestweaks.mixin.somanyenchantments;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.info.CreatureManager;
import com.shultrea.rin.enchantments.weapon.damage.EnchantmentButchering;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentButchering.class)
public abstract class EnchantmentButchering_LycanitesMixin {

    @Definition(id = "victim", local = @Local(type = EntityLivingBase.class, name = "victim"))
    @Definition(id = "EntityAnimal", type = EntityAnimal.class)
    @Expression("victim instanceof EntityAnimal")
    @ModifyExpressionValue(
            method = "onLivingHurtEvent",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_smeEnchantmentButchering_onLivingHurtEventLycanites(boolean isVanillaAnimal, @Local(name = "victim") EntityLivingBase victim){
        return isVanillaAnimal || CreatureManager.getInstance().creatureGroups.get("animal").hasEntity(victim);
    }

    @Definition(id = "EntityAnimal", type = EntityAnimal.class)
    @Expression("? instanceof EntityAnimal")
    @ModifyExpressionValue(
            method = "onLootingLevelEvent",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_smeEnchantmentButchering_onLootingLevelEventLycanites(boolean isVanillaAnimal, @Local(argsOnly = true) LootingLevelEvent event){
        return isVanillaAnimal || CreatureManager.getInstance().creatureGroups.get("animal").hasEntity(event.getEntityLiving());
    }
}
