package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityWraith;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityWraith.class)
public abstract class EntityWraith_GriefFlagMixin extends TameableCreatureEntity {

    public EntityWraith_GriefFlagMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"),
            index = 5
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityWraith_onLivingUpdateGriefFlag(boolean damagesTerrain){
        if(this instanceof ITameableCreatureEntity_TargetFlagMixin) return ((ITameableCreatureEntity_TargetFlagMixin) this).lycanitesTweaks$shouldDoGrief();
        return damagesTerrain;
    }

    @ModifyArg(
            method = "onDeath",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"),
            index = 5
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityWraith_onDeathGriefFlag(boolean damagesTerrain){
        if(this instanceof ITameableCreatureEntity_TargetFlagMixin) return ((ITameableCreatureEntity_TargetFlagMixin) this).lycanitesTweaks$shouldDoGrief();
        return damagesTerrain;
    }
}
