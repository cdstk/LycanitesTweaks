package lycanitestweaks.item;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.pets.SummonSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemChallengeSoulStaff extends ItemCreatureInfoStaff {

    public ItemChallengeSoulStaff(String itemName, String textureName) {
        super(itemName, textureName);
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
        return 20 * 60 * 20;
    }

    // ========== Minion Behaviour ==========
    // Override to avoid my own mixins
    @Override
    public void applyMinionBehaviour(TameableCreatureEntity minion, EntityPlayer player) {
        SummonSet summonSet = ExtendedPlayer.getForPlayer(player).getSelectedSummonSet();
        summonSet.applyBehaviour(minion);
        // Ensure properties are synced from stack nbt
        if(this.itemStack != null) {
            if (!this.getCustomName(this.itemStack).isEmpty()) minion.setCustomNameTag(this.getCustomName(this.itemStack));
            if(this.getEntitySubspecies(this.itemStack) != -1) minion.setSubspecies(this.getEntitySubspecies(this.itemStack));
            if(this.getEntityVariant(this.itemStack) != -1) minion.setVariant(this.getEntityVariant(this.itemStack));
            if(this.getLevel(this.itemStack) != -1) minion.addLevel(this.getLevel(this.itemStack) - 1);
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
