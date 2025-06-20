package lycanitestweaks.mixin.lycanitestweaksminor.smitedundead;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySpider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityLivingBase.class,
        AbstractIllager.class,
        EntityEndermite.class,
        EntitySilverfish.class,
        EntitySpider.class
})
public abstract class EntityLivingBaseSmitedUndeadMixin {
    @ModifyReturnValue(
            method = "getCreatureAttribute",
            at = @At("RETURN")
    )
    public EnumCreatureAttribute lycanitesTweaks_vanillaEntityLivingBase_getCreatureAttribute(EnumCreatureAttribute original){
        if(LycanitesMobsWrapper.hasSmitedEffect((Entity) (Object) this)) return EnumCreatureAttribute.UNDEAD;
        return original;
    }
}
