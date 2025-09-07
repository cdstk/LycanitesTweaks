package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityWraith;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityWraith.class)
public abstract class EntityWraith_GriefSetMixin extends TameableCreatureEntity {

    public EntityWraith_GriefSetMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"),
            index = 5
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityWraith_onLivingUpdateGrief(boolean damagesTerrain){
        return damagesTerrain && this.isPVP();
    }

    @ModifyArg(
            method = "onDeath",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"),
            index = 5
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityWraith_onDeathGrief(boolean damagesTerrain){
        return damagesTerrain && this.isPVP();
    }
}
