package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ExtraMobBehaviour;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.IBaseCreatureEntityTransformIntoBossMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntityTransformIntoBossMixin extends EntityLiving implements IBaseCreatureEntityTransformIntoBossMixin {

    @Shadow public abstract void addTrackingPlayer(EntityPlayerMP player);
    @Shadow public abstract void removeTrackingPlayer(EntityPlayerMP player);

    @Shadow(remap = false)
    public abstract boolean isBoss();
    @Shadow(remap = false)
    public abstract boolean isHostileTo(Entity target);
    @Shadow(remap = false)
    public abstract boolean isMinion();
    @Shadow(remap = false)
    public abstract boolean isTamed();
    @Shadow(remap = false)
    public abstract int getLevel();
    @Shadow(remap = false)
    public abstract void setLevel(int level);
    @Shadow(remap = false)
    public abstract void refreshAttributes();

    @Shadow(remap = false)
    public boolean spawnedAsBoss;
    @Shadow(remap = false)
    public float damageLimit;
    @Shadow(remap = false)
    public int damageMax;
    @Shadow(remap = false)
    public ExtraMobBehaviour extraMobBehaviour;

    @Unique
    private boolean lycanitesTweaks$canTransformIntoBoss = false;

    public BaseCreatureEntityTransformIntoBossMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public boolean lycanitesTweaks$canTransformIntoBoss(){
        return !this.isBoss() && !this.isMinion() && !this.isTamed() && this.lycanitesTweaks$canTransformIntoBoss;
    }

    @Unique
    @Override
    public void lycanitesTweaks$setCanTransformIntoBoss(boolean flag){
        lycanitesTweaks$canTransformIntoBoss = flag;
    }

    @Unique
    @Override
    public boolean lycanitesTweaks$transformIntoBoss(){
        if(this.lycanitesTweaks$canTransformIntoBoss()){
            EntityPlayer player = world.getClosestPlayerToEntity(this, 128);

            this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true));
            this.spawnedAsBoss = true;

            if(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.canTransformBossPML){
                IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
                if(pml != null){
                    // Different from Encounter Crystals, do not add original spawn boost + boss boost
                    int levelBoost = pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.EncounterEvent, (BaseCreatureEntity)(Object)this);
                    if(levelBoost > this.getLevel()) this.setLevel(levelBoost);
                }
            }

            if(!this.isHostileTo(player) && this.extraMobBehaviour != null) {
                this.extraMobBehaviour.aggressiveOverride = true;
                this.extraMobBehaviour.aiAttackPlayers = true;
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                this.extraMobBehaviour.writeToNBT(nbtTagCompound);
                this.extraMobBehaviour.readFromNBT(nbtTagCompound);
            }
            this.refreshAttributes();
            // TODO Look into fixing this globally, annoying that sometimes the mob needs a reload
            // Fine that only the triggering player sees it
            if(player instanceof EntityPlayerMP) {
                this.removeTrackingPlayer((EntityPlayerMP) player);
                this.addTrackingPlayer((EntityPlayerMP) player);
            }
            return true;
        }
        else{
            return false;
        }
    }

    @Unique
    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt){
        super.onStruckByLightning(lightningBolt);
        this.lycanitesTweaks$transformIntoBoss();
    }

    @Inject(
            method = "readEntityFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal = 0)
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_readEntityFromNBTTransformBoss(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        if (nbtTagCompound.hasKey("TransformIntoBoss")) {
            this.lycanitesTweaks$setCanTransformIntoBoss(nbtTagCompound.getBoolean("TransformIntoBoss"));
        }
    }

    @Inject(
            method = "writeEntityToNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setBoolean(Ljava/lang/String;Z)V", ordinal = 0)
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_writeEntityToNBTTransformBoss(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        nbtTagCompound.setBoolean("TransformIntoBoss", this.lycanitesTweaks$canTransformIntoBoss);
    }
}
