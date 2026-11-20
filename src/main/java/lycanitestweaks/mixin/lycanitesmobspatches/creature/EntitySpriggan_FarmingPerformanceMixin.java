package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntitySpriggan;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntitySpriggan.class)
public abstract class EntitySpriggan_FarmingPerformanceMixin extends TameableCreatureEntity {

    public EntitySpriggan_FarmingPerformanceMixin(World world) {
        super(world);
    }

    @Shadow(remap = false) private int farmingTick;

    // Disable loop by setting block range negative, on ticks where neither the server growth tick nor the client particles would happen
    @ModifyVariable(
            method = "onLivingUpdate",
            at = @At("STORE"),
            name = "farmingRange"
    )
    private int lycanitesTweaks_lycanitesMobsEntitySpriggan_onLivingUpdateSkipScan(int farmingRange, @Local(name = "currentFarmingRate") int currentFarmingRate) {
        int rate = this.world.isRemote ? 40 : currentFarmingRate;
        return this.farmingTick % rate == 0 ? farmingRange : -1;
    }

    // Blocks that don't tick randomly never get a growth tick anyway. With this they also don't get growth particles
    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "CONSTANT", args = "classValue=net/minecraft/block/IGrowable") //modify farmingBlock instanceof IGrowable
    )
    private boolean lycanitesTweaks_lycanitesMobsEntitySpriggan_onLivingUpdateTickingOnly(boolean isGrowable, @Local Block farmingBlock) {
        return isGrowable && farmingBlock.getTickRandomly();
    }
}
