package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.world.BossInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_NBTBossDamageLimitMixin {

    @Shadow(remap = false)
    public boolean spawnedAsBoss;
    @Shadow(remap = false)
    public float damageLimit;
    @Shadow(remap = false)
    public int damageMax;

    @Inject(
            method = "createBossInfo",
            at = @At("HEAD"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_createBossInfoDamageLimit(BossInfo.Color color, boolean darkenSky, CallbackInfo ci){
        if(this.spawnedAsBoss){
            this.damageLimit = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
            this.damageMax = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
        }
    }
}
