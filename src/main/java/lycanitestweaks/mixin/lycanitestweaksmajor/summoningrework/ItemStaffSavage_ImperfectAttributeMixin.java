package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.item.temp.ItemStaffSavage;
import lycanitestweaks.LycanitesTweaks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemStaffSavage.class)
public abstract class ItemStaffSavage_ImperfectAttributeMixin {

    @Unique
    private static final UUID lycanitesTweaks$SAVAGE_UUID = UUID.fromString("7e991afb-7df2-410f-bbd5-467dfd8f090c");
    @Unique
    private static final String lycanitesTweaks$SAVAGE_ID = LycanitesTweaks.MODID + ":savageHealth";

    @Inject(
            method = "applyMinionEffects",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemStaffSavage_applyMinionEffectsAttributes(BaseCreatureEntity minion, CallbackInfo ci){
        minion.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(lycanitesTweaks$SAVAGE_UUID, lycanitesTweaks$SAVAGE_ID, -0.5D,2));
    }
}
