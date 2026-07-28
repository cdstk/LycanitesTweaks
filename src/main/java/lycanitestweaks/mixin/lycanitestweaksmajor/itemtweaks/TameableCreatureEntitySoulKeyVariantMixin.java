package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.item.special.ItemSoulkey;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntitySoulKeyVariantMixin extends AgeableCreatureEntity {

    @Unique
    private final String COMMAND_VARIANT_TAMED = "tamed variant";

    public TameableCreatureEntitySoulKeyVariantMixin(World world) {
        super(world);
    }

    @Shadow(remap = false)
    public abstract EntityPlayer getPlayerOwner();
    @Shadow(remap = false)
    public abstract boolean isSitting();

    @Inject(
            method = "getInteractCommands",
            at = @At(value = "INVOKE", target = "Ljava/util/HashMap;putAll(Ljava/util/Map;)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesTameableCreatureEntity_getInteractCommandsSoulkey(EntityPlayer player, EnumHand hand, ItemStack itemStack, CallbackInfoReturnable<HashMap<Integer, String>> cir, @Local() HashMap<Integer, String> commands){
        if(this.isSitting() && lycanitesTweaks$canPlayerSetTame(player) && itemStack.getItem() instanceof ItemSoulkey){
            commands.put(BaseCreatureEntity.COMMAND_PIORITIES.ITEM_USE.id, COMMAND_VARIANT_TAMED);
        }
    }

    @Inject(
            method = "performCommand",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesTameableCreatureEntity_performCommandSoulkey(String command, EntityPlayer player, EnumHand hand, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
        if(COMMAND_VARIANT_TAMED.equals(command)){
            if(((ItemSoulkey)itemStack.getItem()).variant != this.getVariantIndex()) {
                this.applyVariant(((ItemSoulkey) itemStack.getItem()).variant);
                if(isBoundPet()){
                    this.getPetEntry().setEntityVariant(this.getVariantIndex());
                }
                this.consumePlayersItem(player, hand, itemStack);
                cir.setReturnValue(true);
            }
        }
    }

    @Unique
    public boolean lycanitesTweaks$canPlayerSetTame(EntityPlayer player){
        if(this.isTemporary) return false;
        if(this.isBoundPet() && !CreatureManager.getInstance().config.isSoulboundAllowed(this.getEntityWorld())) return false;
        else return this.getPlayerOwner() == player;
    }
}
