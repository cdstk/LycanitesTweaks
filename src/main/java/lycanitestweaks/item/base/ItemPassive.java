package lycanitestweaks.item.base;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.ModLoadedUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = ModLoadedUtil.BAUBLES_MODID, striprefs = true)
public abstract class ItemPassive extends ItemBase implements IBauble {

    public ItemPassive(String modid, String name) {
        super(modid, name);
    }

    public ItemPassive(String name) {
        this(LycanitesTweaks.MODID, name);
    }

    public boolean isToggleable() {
        return false;
    }

    public void tickAbility(ItemStack itemstack, EntityLivingBase entity) {
        this.tickAbility(entity);
    }

    public void tickAbility(EntityLivingBase entity) {

    }

    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.TRINKET;
    }

    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemStack, EntityLivingBase player) {
        this.tickAbility(itemStack, player);
    }
}
