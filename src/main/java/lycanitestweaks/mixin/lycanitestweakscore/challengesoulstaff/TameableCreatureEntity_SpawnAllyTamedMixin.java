package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAbtu;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {
        EntityCacodemon.class,
        EntityAbtu.class
})
public abstract class TameableCreatureEntity_SpawnAllyTamedMixin extends TameableCreatureEntity {

    public TameableCreatureEntity_SpawnAllyTamedMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "spawnAlly",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", remap = true),
            remap = false
    )
    public Entity lycanitesTweaks_lycanitesMobsTameableCreatureEntity_spawnAllyPlayerOwner(Entity minion){
        if(this.getPlayerOwner() != null && minion instanceof TameableCreatureEntity) {
            ((TameableCreatureEntity)minion).setPlayerOwner(this.getPlayerOwner());
            this.copyPetBehaviourTo((TameableCreatureEntity)minion);
        }
        return minion;
    }
}
