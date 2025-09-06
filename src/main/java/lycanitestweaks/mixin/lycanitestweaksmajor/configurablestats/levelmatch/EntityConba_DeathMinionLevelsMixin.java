package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.levelmatch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityConba;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityConba.class)
public abstract class EntityConba_DeathMinionLevelsMixin extends TameableCreatureEntity {

    public EntityConba_DeathMinionLevelsMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "spawnVespidSwarm",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureInfo;createEntity(Lnet/minecraft/world/World;)Lnet/minecraft/entity/EntityLiving;"),
            remap = false
    )
    private EntityLiving lycanitestweaks_lycanitesMobsBaseCreatureEntity_spawnAllyLevels(EntityLiving minion){
        if(minion instanceof BaseCreatureEntity) ((BaseCreatureEntity)minion).applyLevel(this.getLevel());
        return minion;
    }
}
