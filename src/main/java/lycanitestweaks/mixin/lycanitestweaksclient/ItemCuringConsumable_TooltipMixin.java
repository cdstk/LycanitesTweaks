package lycanitestweaks.mixin.lycanitestweaksclient;

import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.consumable.ItemCleansingCrystal;
import com.lycanitesmobs.core.item.consumable.ItemImmunizer;
import lycanitestweaks.handlers.ForgeConfigProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(value = {
        ItemCleansingCrystal.class,
        ItemImmunizer.class
})
public abstract class ItemCuringConsumable_TooltipMixin extends ItemBase {

    @Unique
    @Override
    public String getDescription(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String langKey = "item.mixin." + this.itemName + ".description";
        if(I18n.hasKey(langKey)){
            Set<ResourceLocation> resourceLocations;
            switch (this.itemName){
                case "cleansingcrystal": resourceLocations = ForgeConfigProvider.getCleansedCureEffects(); break;
                case "immunizer": resourceLocations = ForgeConfigProvider.getImmunizationCureEffects(); break;
                default: resourceLocations = new HashSet<>();
            }
            StringBuilder stringBuilder = new StringBuilder(I18n.format(langKey));
            stringBuilder.append(" ");
            for(ResourceLocation resourceLocation : resourceLocations){
                Potion curable = Potion.REGISTRY.getObject(resourceLocation);
                if(curable != null) {
                    stringBuilder.append(I18n.format(curable.getName()));
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.setCharAt(stringBuilder.lastIndexOf(","), '.');
            return stringBuilder.toString();
        }
        return super.getDescription(stack, worldIn, tooltip, flagIn);
    }
}
