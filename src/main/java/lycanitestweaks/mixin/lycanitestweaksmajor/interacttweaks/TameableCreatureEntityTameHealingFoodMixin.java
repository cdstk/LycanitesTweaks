package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.client.localisation.LanguageManager;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureRelationshipEntry;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureKnowledge;
import com.lycanitesmobs.core.info.CreatureManager;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntityTameHealingFoodMixin extends AgeableCreatureEntity {

    @Shadow(remap = false)
    public abstract boolean isHealingItem(ItemStack itemStack);
    @Shadow(remap = false)
    public abstract void playEatSound();
    @Shadow(remap = false)
    public abstract void setPlayerOwner(EntityPlayer player);
    @Shadow(remap = false)
    public abstract void onTamedByPlayer();
    @Shadow(remap = false)
    protected abstract void playTameEffect(boolean success);

    public TameableCreatureEntityTameHealingFoodMixin(World world) {
        super(world);
    }

    @Inject(
            method = "getInteractCommands",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;isTamed()Z", ordinal = 1),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_getInteractCommandsDietTame(EntityPlayer player, EnumHand hand, ItemStack itemStack, CallbackInfoReturnable<HashMap<Integer, String>> cir, @Local HashMap<Integer, String> commands){
        if (!this.isTamed() && this.isHealingItem(itemStack) && !this.isBoss() && !this.isRareVariant() && this.creatureInfo.isTameable() && CreatureManager.getInstance().config.tamingEnabled) {
            if (LycanitesEntityUtil.isPracticallyFlying(this) && !ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.tamedWithFoodAllowFlying) {
                player.sendStatusMessage(new TextComponentTranslation("message.tame.fail.healingnofly"), true);
            }
            else commands.put(COMMAND_PIORITIES.IMPORTANT.id, "DietTame");
        }
    }

    @Inject(
            method = "performCommand",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_performCommandDietTame(String command, EntityPlayer player, EnumHand hand, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
        if(command.equals("DietTame")){
            this.playEatSound();
            this.lycanitesTweaks$dietTame(player, itemStack);
            this.consumePlayersItem(player, hand, itemStack);
            cir.setReturnValue(true);
        }
    }

    // Based on original
    @Unique
    public boolean lycanitesTweaks$dietTame(EntityPlayer player, ItemStack itemStack) {
        if(this.getEntityWorld().isRemote || this.isRareVariant() || this.isBoss()) {
            return super.isTamed();
        }

        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
        if(extendedPlayer == null) return this.isTamed();

        int healAmount = 4;
        if (itemStack.getItem() instanceof ItemFood) {
            ItemFood itemFood = (ItemFood)itemStack.getItem();
            healAmount = (int) Math.max(1, itemFood.getHealAmount(itemStack) * itemFood.getSaturationModifier(itemStack));
        }

        int knowledgeGain = (int) (healAmount * ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.tameWithFoodKnowledgeMult);

        extendedPlayer.studyCreature(this, knowledgeGain, false, true);

        // Already Tamed:
        if (this.isTamed()) return true;

        // Require Knowledge Rank 2:
        CreatureKnowledge creatureKnowledge = extendedPlayer.getBeastiary().getCreatureKnowledge(this.creatureInfo.getName());
        if (creatureKnowledge == null || creatureKnowledge.rank < 2) return this.isTamed();

        float reputationGain = healAmount * ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.tameWithFoodReputationMult;

        // Increase Reputation:
        CreatureRelationshipEntry relationshipEntry = this.relationships.getOrCreateEntry(player);
        int reputationAmount = (int) (reputationGain * (1F + this.getRNG().nextFloat()));
        relationshipEntry.increaseReputation(reputationAmount);

        // Apply Tame:
        if (this.creatureInfo.isTameable() && relationshipEntry.getReputation() >= this.creatureInfo.getTamingReputation()) {
            this.setPlayerOwner(player);
            this.onTamedByPlayer();
            this.unsetTemporary();
            String tameMessage = LanguageManager.translate("message.pet.tamed");
            tameMessage = tameMessage.replace("%creature%", this.getSpeciesName());
            player.sendMessage(new TextComponentString(tameMessage));
            this.playTameEffect(this.isTamed());
            player.addStat(ObjectManager.getStat(this.creatureInfo.getName() + ".tame"), 1);
            if (this.timeUntilPortal > this.getPortalCooldown()) {
                this.timeUntilPortal = this.getPortalCooldown();
            }
        }

        this.playTameEffect(this.isTamed());
        return this.isTamed();
    }
}
