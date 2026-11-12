package lycanitestweaks.util.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class BaseProjectileDamageSource extends EntityDamageSourceIndirect {

    public BaseProjectileDamageSource(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
        super(damageTypeIn, source, indirectEntityIn);
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
        Entity killer = this.getTrueSource();
        Entity indirectKiller = this.getImmediateSource();

        ITextComponent killerDisplayName;
        ItemStack itemstack;

        if(indirectKiller != null && killer == null) {
            killerDisplayName = indirectKiller.getDisplayName();
            itemstack = indirectKiller instanceof EntityLivingBase
                    ? ((EntityLivingBase)indirectKiller).getHeldItemMainhand()
                    : ItemStack.EMPTY;
        }
        else if(killer != null) {
            killerDisplayName = killer.getDisplayName();
            itemstack = killer instanceof EntityLivingBase
                    ? ((EntityLivingBase)killer).getHeldItemMainhand()
                    : ItemStack.EMPTY;
        }
        else {
            killerDisplayName = new TextComponentTranslation("entity.generic.name");
            itemstack = ItemStack.EMPTY;
        }

        // Append item to entity display name
        if(!itemstack.isEmpty() && itemstack.hasDisplayName()) {
            killerDisplayName.appendText(" [").appendSibling(itemstack.getTextComponent()).appendText("]");
        }

        String langKey = "death.attack.";
        if(indirectKiller instanceof EntityLivingBase) {
            langKey += this.damageType;
        }
        else {
            langKey += "thrown";
        }
        String withItemLangKey = langKey + ".item"; // Reused to show indirect entity

        if(I18n.canTranslate(withItemLangKey) && indirectKiller != null) {
            return new TextComponentTranslation(withItemLangKey, entityLivingBaseIn.getDisplayName(), killerDisplayName, indirectKiller.getDisplayName());
        }
        else {
            return new TextComponentTranslation(langKey, entityLivingBaseIn.getDisplayName(), killerDisplayName);
        }
    }
}
