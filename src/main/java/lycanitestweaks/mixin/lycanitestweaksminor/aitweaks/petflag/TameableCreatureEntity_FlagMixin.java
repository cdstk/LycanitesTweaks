package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntity_FlagMixin extends AgeableCreatureEntity implements ITameableCreatureEntity_TargetFlagMixin {

    @Shadow(remap = false)
    public abstract boolean petControlsEnabled();

    // TAMED byte is used locally, so fine to create and use additional
    @Unique
    private static final DataParameter<Byte> LT_PET = EntityDataManager.createKey(BaseCreatureEntity.class, DataSerializers.BYTE);

    public TameableCreatureEntity_FlagMixin(World world) {
        super(world);
    }

    @Inject(
            method = "entityInit",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_entityInitPetFlags(CallbackInfo ci){
        this.dataManager.register(LT_PET, (byte)0);
    }

    @Inject(
            method = "copyPetBehaviourTo",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_copyPetBehaviourToPetFlags(TameableCreatureEntity target, CallbackInfo ci){
        ((ITameableCreatureEntity_TargetFlagMixin)target).lycanitesTweaks$setTargetBoss(this.lycanitesTweaks$shouldTargetBoss());
        ((ITameableCreatureEntity_TargetFlagMixin)target).lycanitesTweaks$setDoGrief(this.lycanitesTweaks$shouldDoGrief());
    }

    @Inject(
            method = "performGUICommand",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;playTameSound()V"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_performGUICommandPetFlags(EntityPlayer player, byte guiCommandID, CallbackInfo ci){
        if (guiCommandID == PET_COMMAND_TARGET_BOSS) {
            this.lycanitesTweaks$setTargetBoss(!this.lycanitesTweaks$shouldTargetBoss());
        }
        else if (guiCommandID == PET_COMMAND_DO_GRIEF) {
            this.lycanitesTweaks$setDoGrief(!this.lycanitesTweaks$shouldDoGrief());
        }
        else if(guiCommandID == PET_COMMAND_INV_LEVELING) {
            this.lycanitesTweaks$setInventoryLevelup(!this.lycanitesTweaks$shouldInventoryLevelup());
        }
    }

    @ModifyReturnValue(
            method = "canAttackEntity",
            at = @At(value = "RETURN", ordinal = 5),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntity_canAttackEntityBoss(boolean original, EntityLivingBase targetEntity){
        if(!this.getEntityWorld().isRemote
                && targetEntity instanceof BaseCreatureEntity
                && (((BaseCreatureEntity) targetEntity).isBoss() || ((BaseCreatureEntity) targetEntity).isRareVariant())){
            return this.lycanitesTweaks$shouldTargetBoss();
        }
        return original;
    }

    @Inject(
            method = "onTamedByPlayer",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_onTamedByPlayerPetFlags(CallbackInfo ci){
        this.lycanitesTweaks$setTargetBoss(true);
        this.lycanitesTweaks$setDoGrief(true);
    }

    @Inject(
            method = "readEntityFromNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_readEntityFromNBTPetFlags(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        if(nbtTagCompound.hasKey(NBT_TARGET_BOSS)) {
            this.lycanitesTweaks$setTargetBoss(nbtTagCompound.getBoolean(NBT_TARGET_BOSS));
        }
        else {
            this.lycanitesTweaks$setTargetBoss(true);
        }

        if(nbtTagCompound.hasKey(NBT_DO_GRIEF)) {
            this.lycanitesTweaks$setDoGrief(nbtTagCompound.getBoolean(NBT_DO_GRIEF));
        }
        else {
            this.lycanitesTweaks$setDoGrief(true);
        }

        if(nbtTagCompound.hasKey(NBT_INVENTORY_LEVELING)) {
            this.lycanitesTweaks$setInventoryLevelup(nbtTagCompound.getBoolean(NBT_INVENTORY_LEVELING));
        }
        else {
            this.lycanitesTweaks$setInventoryLevelup(true);
        }
    }

    @Inject(
            method = "writeEntityToNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_writeEntityToNBTPetFlags(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        nbtTagCompound.setBoolean(NBT_TARGET_BOSS, this.lycanitesTweaks$shouldTargetBoss());
        nbtTagCompound.setBoolean(NBT_DO_GRIEF, this.lycanitesTweaks$shouldDoGrief());
        nbtTagCompound.setBoolean(NBT_INVENTORY_LEVELING, this.lycanitesTweaks$shouldInventoryLevelup());
    }

    @Unique
    public boolean lycanitesTweaks$shouldTargetBoss() {
        return (this.getByteFromDataManager(LT_PET) & PET_AI_TARGET_BOSS) != 0;
    }

    @Unique
    public void lycanitesTweaks$setTargetBoss(boolean set) {
        if(!this.petControlsEnabled()) set = false;

        byte tamedStatus = this.getByteFromDataManager(LT_PET);
        if(set) {
            this.dataManager.set(LT_PET, (byte) (tamedStatus | PET_AI_TARGET_BOSS));
        }
        else {
            this.setAttackTarget(null);
            this.dataManager.set(LT_PET, (byte) (tamedStatus - (tamedStatus & PET_AI_TARGET_BOSS)));
        }
    }

    @Override
    public boolean lycanitesTweaks$shouldDoGrief(){
        return (this.getByteFromDataManager(LT_PET) & PET_ABILITY_DO_GRIEF) != 0;
    }

    @Override
    public void lycanitesTweaks$setDoGrief(boolean set){
        if(!this.petControlsEnabled()) set = false;

        byte tamedStatus = this.getByteFromDataManager(LT_PET);
        if(set) {
            this.dataManager.set(LT_PET, (byte) (tamedStatus | PET_ABILITY_DO_GRIEF));
        }
        else {
            this.dataManager.set(LT_PET, (byte) (tamedStatus - (tamedStatus & PET_ABILITY_DO_GRIEF)));
        }
    }

    @Override
    public boolean lycanitesTweaks$shouldInventoryLevelup(){
        return (this.getByteFromDataManager(LT_PET) & PET_ABILITY_INV_LEVELING) != 0;
    }

    @Override
    public void lycanitesTweaks$setInventoryLevelup(boolean set){
        if(!this.petControlsEnabled()) set = false;

        byte tamedStatus = this.getByteFromDataManager(LT_PET);
        if(set) {
            this.dataManager.set(LT_PET, (byte) (tamedStatus | PET_ABILITY_INV_LEVELING));
            if(LycanitesEntityUtil.shouldLevelFromStack((TameableCreatureEntity)(Object)this)){
                for(int slotID = this.getNoBagSize(); slotID < this.getBagSize(); slotID++) {
                    ItemStack itemStack = this.inventory.getStackInSlot(slotID);
                    itemStack = LycanitesEntityUtil.attemptLevelingFromStack((TameableCreatureEntity)(Object)this, itemStack);
                    this.inventory.setInventorySlotContents(slotID, itemStack);
                }
            }
        }
        else {
            this.dataManager.set(LT_PET, (byte) (tamedStatus - (tamedStatus & PET_ABILITY_INV_LEVELING)));
        }
    }
}
