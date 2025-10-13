package lycanitestweaks.mixin.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemSoulgazer.class)
public abstract class ItemSoulgazer_BaubleMixin extends ItemBase implements IBauble {

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        if(ForgeConfigHandler.integrationConfig.soulgazerBaubleCharm) return BaubleType.CHARM;
        return BaubleType.TRINKET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack repairStack) {
        if(repairStack.getItem() == Items.ENDER_PEARL && ForgeConfigHandler.integrationConfig.soulgazerBaubleRepairMaterial) return true;
        return super.getIsRepairable(itemStack, repairStack);
    }

    // Might need to move this elsewhere incase nbt is used elsewhere
    // Keep nbt when used as recipe input
    @Override
    public ItemStack getContainerItem(ItemStack itemStack){
        ItemStack copyStack = super.getContainerItem(itemStack);

        if(itemStack.hasTagCompound()) copyStack.setTagCompound(itemStack.getTagCompound().copy());

        return copyStack;
    }
}
