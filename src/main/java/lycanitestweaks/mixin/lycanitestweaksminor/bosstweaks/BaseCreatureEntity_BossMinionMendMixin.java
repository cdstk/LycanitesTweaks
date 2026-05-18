package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.mixin.vanilla.EntityLivingBase_InvokerMixin;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_BossMinionMendMixin extends EntityLiving {

    @Shadow(remap = false) public List<EntityPlayer> playerTargets;
    @Shadow(remap = false) public abstract boolean isBoss();
    @Shadow(remap = false) public abstract boolean isRareVariant();

    public BaseCreatureEntity_BossMinionMendMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onMinionDeath",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onMinionDeathKillMending(EntityLivingBase minion, DamageSource damageSource, CallbackInfo ci) {
        if(this.world.isRemote) return;
        if(minion == null || damageSource == null) return;

        if(this.isBoss() || this.isRareVariant()) {
            if(damageSource.getTrueSource() instanceof EntityPlayer || damageSource.damageType.equals("player"))
                this.playerTargets.forEach(player -> {
                    int xpDrop = 0;
                    if(minion instanceof BaseCreatureEntity && minion instanceof EntityLivingBase_InvokerMixin) {
                        BaseCreatureEntity creature = (BaseCreatureEntity) minion;
                        creature.setMinion(false);
                        xpDrop = ((EntityLivingBase_InvokerMixin) minion).lycanitesTweaks$invokeGetExperiencePoints(player);
                        creature.setMinion(true);
                    }
                    if(xpDrop == 0) return;

                    List<ItemStack> armorList = new ArrayList<>();
                    int xpValue = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(
                            this,
                            player,
                            xpDrop
                    );
                    player.getArmorInventoryList().forEach(itemStack -> {
                        if (!itemStack.isEmpty() && itemStack.isItemDamaged()) {
                            armorList.add(itemStack);
                        }
                    });
                    if(armorList.isEmpty()) return;

                    xpValue /= armorList.size();
                    if(xpValue <= 0) xpValue = 1;

                    for(ItemStack itemStack : armorList) {
                        float ratio = itemStack.getItem().getXpRepairRatio(itemStack);
                        int repairAmount = Math.min(lycanitesTweaks$roundAverage(xpValue * ratio), itemStack.getItemDamage());
                        itemStack.setItemDamage(itemStack.getItemDamage() - repairAmount);
                    }
                });
        }
    }

    @Unique
    private static int lycanitesTweaks$roundAverage(float value) {
        double floor = Math.floor(value);
        return (int) floor + (Math.random() < value - floor ? 1 : 0);
    }
}
