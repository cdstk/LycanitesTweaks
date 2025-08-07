package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import com.lycanitesmobs.core.info.Variant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;

@Mixin(CreatureStats.class)
public abstract class CreatureStats_SpawnedAsBossBoostMixin {

    @Shadow(remap = false)
    public BaseCreatureEntity entity;

    // The order is late for /summon but is proper for Dungeon Crystals
    // Original lycanites dungeon spawn likely missing the timing as SpawnedAsBoss is set late
    @ModifyReturnValue(
            method = "getVariantMultiplier",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getVariantMultiplierBossRare(double original, @Local(argsOnly = true) String stat){
        if(this.entity.spawnedAsBoss && !this.entity.isBossAlways() && !this.entity.isRareVariant())
                return Variant.STAT_MULTIPLIERS.get("RARE-" + stat.toUpperCase(Locale.ENGLISH));
        return original;
    }
}
