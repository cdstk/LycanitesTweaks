package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_NameTagUseMixin {

    @Shadow(remap = false) public abstract boolean canNameTag(EntityPlayer player);

    @WrapMethod(
            method = "assessInteractCommand",
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesBaseCreatureEntity_assessInteractCommandNameTag(HashMap<Integer, String> commands, EntityPlayer player, EnumHand hand, ItemStack itemStack, Operation<Boolean> original){
        if(itemStack != null) {
            // Name Tag:
            if(itemStack.getItem() == Items.NAME_TAG) {
                if(this.canNameTag(player))
                    return false;
            }
        }
        return original.call(commands, player, hand, itemStack);
    }
}
