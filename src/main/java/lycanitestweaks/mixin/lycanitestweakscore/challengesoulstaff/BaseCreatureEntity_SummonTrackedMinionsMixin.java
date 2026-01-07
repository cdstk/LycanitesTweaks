package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAbtu;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import com.lycanitesmobs.core.entity.creature.EntityCalpod;
import com.lycanitesmobs.core.entity.creature.EntityGorgomite;
import com.lycanitesmobs.core.entity.creature.EntityGrell;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityAbtu.class,
        EntityCacodemon.class,
        EntityCalpod.class,
        EntityGrell.class,
        EntityGorgomite.class
})
public abstract class BaseCreatureEntity_SummonTrackedMinionsMixin extends BaseCreatureEntity {

    public BaseCreatureEntity_SummonTrackedMinionsMixin(World world) {
        super(world);
    }

    @WrapOperation(
            method = "spawnAlly",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_spawnAllyTracked(World instance, Entity entity, Operation<Boolean> original){
        this.summonMinion(((BaseCreatureEntity)entity), 0, 0);
        return true;
    }
}
