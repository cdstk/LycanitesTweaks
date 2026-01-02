package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityTroll;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityTroll.class)
public abstract class EntityTroll_GriefFlagMixin extends TameableCreatureEntity {

    public EntityTroll_GriefFlagMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityTroll;griefing:Z", remap = false)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityTroll_onLivingUpdateGriefFlag(boolean original){
        return (this instanceof ITameableCreatureEntity_TargetFlagMixin) ? original && ((ITameableCreatureEntity_TargetFlagMixin) this).lycanitesTweaks$shouldDoGrief(): original;
    }
}
