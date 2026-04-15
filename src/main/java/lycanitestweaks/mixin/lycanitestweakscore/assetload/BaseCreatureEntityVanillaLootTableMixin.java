package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.ItemDrop;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntityVanillaLootTableMixin extends EntityLiving {

    @Shadow(remap = false)
    public CreatureInfo creatureInfo;

    public BaseCreatureEntityVanillaLootTableMixin(World worldIn) {
        super(worldIn);
    }

    // Modifying the parent check was causing crashes
    @WrapWithCondition(
            method = "setupMob",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_setupMobPartDropVanilla(List<ItemDrop> instance, Object e){
        return !ForgeConfigHandler.server.lootConfig.mobPartsVanilla;
    }

    @Unique
    @Override
    public ResourceLocation getLootTable() {
        return this.creatureInfo.getResourceLocation();
    }

    @Unique
    @Override
    public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source){
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        if(this.getLootTable() != null) {
            this.dropFewItems(wasRecentlyHit, lootingModifier);
            this.dropEquipment(wasRecentlyHit, lootingModifier);
        }
    }
}
