package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.levelmatch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityVespidQueen;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityVespidQueen.class)
public abstract class EntityVespidQueen_MechanicMinionLevelsMixin extends TameableCreatureEntity {

    public EntityVespidQueen_MechanicMinionLevelsMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "spawnAlly",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureInfo;createEntity(Lnet/minecraft/world/World;)Lnet/minecraft/entity/EntityLiving;"),
            remap = false
    )
    private EntityLiving lycanitestweaks_lycanitesMobsEntityVespidQueenBossEntity_spawnAllyLevels(EntityLiving minion){
        if(minion instanceof BaseCreatureEntity) ((BaseCreatureEntity)minion).applyLevel(this.getLevel());
        return minion;
    }
}
