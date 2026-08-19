package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import lycanitestweaks.handlers.features.entity.AttributesHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_UseUnusedAttributesMixin extends EntityLiving {

    @Shadow(remap = false)
    public CreatureStats creatureStats;

    public BaseCreatureEntity_UseUnusedAttributesMixin(World world) {
        super(world);
    }

    @Inject(
            method = "applyEntityAttributes",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;applyDynamicAttributes()V", remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_applyEntityAttributesPierce(CallbackInfo ci){
        this.getAttributeMap().registerAttribute(AttributesHandler.PIERCE);
    }

    @Inject(
            method = "applyDynamicAttributes",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_applyDynamicAttributesPierce(CallbackInfo ci){
        this.getEntityAttribute(AttributesHandler.PIERCE).setBaseValue(this.creatureStats.getPierce());
    }

    @ModifyExpressionValue(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/CreatureStats;getPierce()D"),
            remap = false
    )
    private double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageWithPierceAttribute(double original){
        return this.getEntityAttribute(AttributesHandler.PIERCE).getAttributeValue();
    }

    @ModifyExpressionValue(
            method = "attackEntityAsMob",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/CreatureStats;getPierce()D"),
            remap = false
    )
    private double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityAsMobWithPierceAttribute(double original){
        return this.getEntityAttribute(AttributesHandler.PIERCE).getAttributeValue();
    }

    @ModifyExpressionValue(
            method = "getDamageAfterDefense",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/CreatureStats;getDefense()D"),
            remap = false
    )
    private double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getDamageAfterDefenseWithDefenseAttribute(double original){
        return this.getEntityAttribute(BaseCreatureEntity.DEFENSE).getAttributeValue();
    }
}
