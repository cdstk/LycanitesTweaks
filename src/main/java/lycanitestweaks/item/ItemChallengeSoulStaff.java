package lycanitestweaks.item;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.pets.SummonSet;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemChallengeSoulStaff extends ItemCreatureInfoStaff {

    public ItemChallengeSoulStaff(String itemName, String textureName) {
        super(itemName, textureName);
    }

    // Temp way to give Lobber something as it isn't tameable
    @Override
    public void setCreatureTypeName(ItemStack itemStack, String type){
        if(type.equals("lobber")) {
            type = "volcan";
            this.setSpawnedAsBoss(itemStack);
        }
        super.setCreatureTypeName(itemStack, type);
    }

    public boolean getSpawnedAsBoss(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey("SpawnedAsBoss")) return nbt.getBoolean("SpawnedAsBoss");
        return false;
    }

    public void setSpawnedAsBoss(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setBoolean("SpawnedAsBoss", true);
        itemStack.setTagCompound(nbt);
    }

    // ==================================================
    //                       Use
    // ==================================================
    // ========== Durability ==========
    @Override
    public int getDurability() {
        return 1;
    }

    // ========== Summon Cost ==========
    @Override
    public float getSummonCostMod() {
        return 0F;
    }

    // ========== Summon Duration ==========
    @Override
    public int getSummonDuration() {
        return 20 * ForgeConfigHandler.server.customStaffConfig.challengeSoulStaffDuration;
    }

    // I don't like this but it works
    @Override
    public boolean getAdditionalCosts(EntityPlayer player) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
        boolean oneSpawn = false;
        if(extendedPlayer != null) {
            oneSpawn = extendedPlayer.summonFocus >= extendedPlayer.summonFocusCharge;
            extendedPlayer.summonFocus = 0;
        }

        return oneSpawn && super.getAdditionalCosts(player);
    }


    // ========== Minion Behaviour ==========
    // Override to avoid my own mixins
    @Override
    public void applyMinionBehaviour(TameableCreatureEntity minion, EntityPlayer player) {
        SummonSet summonSet = ExtendedPlayer.getForPlayer(player).getSelectedSummonSet();
        summonSet.applyBehaviour(minion);
        // Ensure properties are synced from stack nbt
        if(this.itemStack != null) {
            if(this.getSpawnedAsBoss(itemStack)) minion.spawnedAsBoss = true;
            if (!this.getCustomName(this.itemStack).isEmpty()) minion.setCustomNameTag(this.getCustomName(this.itemStack));
            if(this.getEntitySubspecies(this.itemStack) != -1) minion.setSubspecies(this.getEntitySubspecies(this.itemStack));
            if(this.getEntityVariant(this.itemStack) != -1) minion.setVariant(this.getEntityVariant(this.itemStack));
            if(this.getLevel(this.itemStack) != -1) minion.addLevel(this.getLevel(this.itemStack) - 1);
            minion.firstSpawn = false;
        }
        minion.getRandomSize();
    }

    // ==================================================
    //                     Repairs
    // ==================================================
    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack repairStack) {
        return false;
    }
}
