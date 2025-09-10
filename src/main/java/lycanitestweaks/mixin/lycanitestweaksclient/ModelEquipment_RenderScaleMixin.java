package lycanitestweaks.mixin.lycanitestweaksclient;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.model.ModelEquipment;
import com.lycanitesmobs.client.model.ModelItemBase;
import com.lycanitesmobs.client.model.ModelObjPart;
import com.lycanitesmobs.client.renderer.EquipmentRenderer;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.ForgeConfigProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelEquipment.class)
public abstract class ModelEquipment_RenderScaleMixin {

    // com.lycanitesmobs.client.renderer.EquipmentPartRenderer
    // Too lazy to pass the hand where it should go
    @Unique
    public boolean lycanitesTweaks$isOffhand = false;

    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", ordinal = 1)
    )
    public void lycanitesTweaks_lycanitesMobsModelEquipment_renderOffhand(ItemStack itemStack, EnumHand hand, EquipmentRenderer renderer, float loop, CallbackInfo ci){
        lycanitesTweaks$isOffhand = Minecraft.getMinecraft().player.getHeldItemOffhand() == itemStack;
    }

    @Inject(
            method = "renderPart",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/client/AssetManager;getItemModel(Ljava/lang/String;)Lcom/lycanitesmobs/client/model/ModelItemBase;"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsModelEquipment_renderPartSkip(ItemStack partStack, EnumHand hand, EquipmentRenderer renderer, ModelObjPart offsetPart, float loop, CallbackInfoReturnable<ModelItemBase> cir, @Local ItemEquipmentPart itemEquipmentPart){
        if(ForgeConfigProvider.getEquipmentPartRendersToSkip().contains(itemEquipmentPart.slotType)) cir.setReturnValue(null);
    }

    @Inject(
            method = "renderPart",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/client/model/ModelItemBase;render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/EnumHand;Lcom/lycanitesmobs/client/renderer/IItemModelRenderer;Lcom/lycanitesmobs/client/model/ModelObjPart;Lcom/lycanitesmobs/client/renderer/layer/LayerItem;FZ)V", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsModelEquipment_renderPartModify(ItemStack partStack, EnumHand hand, EquipmentRenderer renderer, ModelObjPart offsetPart, float loop, CallbackInfoReturnable<ModelItemBase> cir, @Local ModelItemBase modelItemBase){
        float scale = ForgeConfigHandler.clientFeaturesMixinConfig.equipmentRenderScale;
        modelItemBase.scale(scale, scale, scale);
        if(offsetPart == null) {
            if(lycanitesTweaks$isOffhand){
                // com.lycanitesmobs.client.modelModelItemBase
                // invert the doAngle and doTranslate called in line 198 of render()
                // too lazy for a separate mixin
                modelItemBase.doAngle(ModelItemBase.modelXRotOffset, 0, 0, 1);
                modelItemBase.doTranslate(0, ModelItemBase.modelYPosOffset * 2, 0);
            }
        }
    }
}
