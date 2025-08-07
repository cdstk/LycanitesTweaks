package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_RandomBossNameMixin extends EntityLiving {

    @Unique
    public Integer lycanitesTweaks$prefixIndex = null;

    public BaseCreatureEntity_RandomBossNameMixin(World world) {
        super(world);
    }

    @Shadow(remap = false)
    public boolean spawnedAsBoss;
    @Shadow(remap = false)
    public abstract String getVariantName();
    @Shadow(remap = false)
    public abstract String getSubspeciesName();
    @Shadow(remap = false)
    public abstract String getSpeciesName();

    // Will be different upon reload, but these are despawnble
    @Inject(
            method = "getFullName",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getFullNameRandomBoss(CallbackInfoReturnable<String> cir){
        if(this.spawnedAsBoss && !this.world.isRemote){
            String name = new TextComponentTranslation("%s%s%s",
                    this.getVariantName(),
                    this.getSubspeciesName().isEmpty() ? " " : this.getSubspeciesName() + " ",
                    this.getSpeciesName())
                    .getFormattedText();

            if(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.spawnedAsBossNaturalSpawnNames > 0) {
                if(this.lycanitesTweaks$prefixIndex == null)
                    this.lycanitesTweaks$prefixIndex = this.getRNG().nextInt(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.spawnedAsBossNaturalSpawnNames);
                name = new TextComponentTranslation("creature.spawnedasboss.prefix." + this.lycanitesTweaks$prefixIndex).getFormattedText() + " " + name;
            }
            cir.setReturnValue(name);
        }
    }

}
