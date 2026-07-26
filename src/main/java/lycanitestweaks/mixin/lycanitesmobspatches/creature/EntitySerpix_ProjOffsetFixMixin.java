package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntitySerpix;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntitySerpix.class)
public abstract class EntitySerpix_ProjOffsetFixMixin extends TameableCreatureEntity {

    public EntitySerpix_ProjOffsetFixMixin(World world) {
        super(world);
    }

    @ModifyConstant(
            method = "attackRanged",
            constant = @Constant(doubleValue = 10.0D),
            remap = false
    )
    public double lycanitesTweaks_lycanitesMobsEntityIgnibus_mountAbilityShootFromPlayer(double constant){
        return 1.0D;
    }

    @ModifyExpressionValue(
            method = "attackRanged",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntitySerpix;getFacingPosition(D)Lnet/minecraft/util/math/BlockPos;"),
            remap = false
    )
    private BlockPos lycanitesTweaks_lycanitesMobsEntityIgnibus_attackRangedFaceTarget(BlockPos original){
        Vec3d facing = this.getLook(1.0F).normalize();
        return new BlockPos(
                this.posX + (facing.x * 4.0D),
                this.posY + (facing.y * 4.0D),
                this.posZ + (facing.z * 4.0D)
        );
    }
}
