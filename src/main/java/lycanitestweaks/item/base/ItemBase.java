package lycanitestweaks.item.base;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.client.renderer.RenderContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemBase extends Item {

    public ItemBase(String modid, String name) {
        super();
        this.setRegistryName(modid, name);
        this.setTranslationKey(modid + "." + name);

        this.setMaxStackSize(1);

        this.addPropertyOverride(new ResourceLocation("dummyrender"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack itemStack, World world, EntityLivingBase entity) {
                RenderContext.currentRenderEntity = entity;
                RenderContext.currentRenderStack = itemStack;
                return 0.0F;
            }
        });
    }


    public ItemBase(String name) {
        this(LycanitesTweaks.MODID, name);
    }

    /**
     * @return if the item is enabled in the config
     */
    public abstract boolean isEnabled();

    /**
     * @return if the item class has an event subscriber
     */
    public boolean hasSubscriber() {
        return false;
    }

    public NBTTagCompound getTagCompound(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            return itemStack.getTagCompound();
        }
        return new NBTTagCompound();
    }

    public boolean getTagBoolean(ItemStack itemStack, String key) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(key)) return nbt.getBoolean(key);
        return false;
    }

    public int getTagInt(ItemStack itemStack, String key) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(key)) return nbt.getInteger(key);
        return 0;
    }

    public String getTagString(ItemStack itemStack, String key) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(key)) return nbt.getString(key);
        return "";
    }

    public void setTagBoolean(ItemStack itemStack, String key, boolean value){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setBoolean(key, value);
        itemStack.setTagCompound(nbt);
    }

    public void setTagInt(ItemStack itemStack, String key, int value){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(key, value);
        itemStack.setTagCompound(nbt);
    }

    public void setTagString(ItemStack itemStack, String key, String value){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(key, value);
        itemStack.setTagCompound(nbt);
    }
}
