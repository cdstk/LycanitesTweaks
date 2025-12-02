package lycanitestweaks.mixin.shieldbreak;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shieldbreak.handlers.ModConfig;
import shieldbreak.util.PotionEntry;

import java.util.List;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_ShieldBreakMixin extends EntityLiving {

	@Shadow(remap = false) public CreatureStats creatureStats;
	@Shadow(remap = false) public int blockingTime;
	@Shadow(remap = false) public int currentBlockingTime;
	@Shadow(remap = false) public abstract boolean isBossAlways();
	@Shadow(remap = false) public abstract boolean isBlocking();
	@Shadow(remap = false) public abstract int getBlockingMultiplier();
	@Shadow(remap = false) public abstract void playAttackSound();
	@Shadow protected abstract SoundEvent getDeathSound();

	@Unique
	private int lycanitesTweaks$shieldCooldown = 0;

	public BaseCreatureEntity_ShieldBreakMixin(World worldIn) {
		super(worldIn);
	}

	@Inject(
			method = "onLivingUpdate",
			at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;currentBlockingTime:I", ordinal = 0, remap = false)
	)
	private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onLivingUpdateShieldBreak(CallbackInfo ci){
		this.lycanitesTweaks$shieldCooldown = Math.max(0, --this.lycanitesTweaks$shieldCooldown);
	}

	@ModifyReturnValue(
			method = "isBlocking",
			at = @At("RETURN"),
			remap = false
	)
	private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_isBlockingShieldBreak(boolean isBlocking){
		return isBlocking && lycanitesTweaks$shieldCooldown <= 0;
	}
	
	@Inject(
			method = "attackEntityFrom",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;getImmediateSource()Lnet/minecraft/entity/Entity;")
	)
	private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityFromBlockShieldBreak(DamageSource damageSrc, float damageAmount, CallbackInfoReturnable<Boolean> cir) {
		if(!(this.isBlocking())
				|| !(damageSrc.getTrueSource() instanceof EntityLivingBase)
				|| this.isBossAlways() // Asmodeus
		) return;

		//Weapon breaks shields
		EntityLivingBase attacker = (EntityLivingBase) damageSrc.getTrueSource();
		ItemStack attackerItem = attacker.getHeldItem(attacker.getActiveHand());

		if(!attackerItem.isEmpty() && attackerItem.getItem().canDisableShield(attackerItem, ItemStack.EMPTY, this, attacker)) {
			float chance = ModConfig.server.shieldBypassChance + Math.max(0, (EnchantmentHelper.getModifierForCreature(attacker.getHeldItemMainhand(), this.getCreatureAttribute()) - 0.5F) * 2F * 0.05F);
			if(this.world.rand.nextFloat() < chance) {//Shield broken by shieldbreak weapon
				this.world.playSound(null, this.getPosition(), this.getDeathSound(), SoundCategory.HOSTILE, 1.0F, 0.3F);
				lycanitesTweaks$shieldCooldown = ModConfig.server.shieldBypassCooldown;
				this.currentBlockingTime = 0;
				lycanitesTweaks$doShieldPotionEffects(attacker, this, false);
				return;
			}
		}
		
		int parryTicks = this.blockingTime - this.currentBlockingTime;
		float shieldProtection = (float) Math.max(
				ModConfig.server.damageMinimumThreshold,
				Math.min(
						ModConfig.server.damageMaximumThreshold,
						Math.max(1.0D, this.creatureStats.getDefense()) * this.getBlockingMultiplier() * ForgeConfigHandler.integrationConfig.shieldBreakDamageDefenceScaling
				)
		);

		float knockbackPower = ForgeConfigHandler.integrationConfig.shieldBreakLycanitesKnockback ? ModConfig.server.knockbackNormal : 0;
		boolean parry = false;
		boolean broken = false;

		//Normal block
		if(parryTicks < ModConfig.server.shieldRaiseTickDelay){

		}
		//Parry
		else if(parryTicks < (ModConfig.server.parryTickRange + ModConfig.server.shieldRaiseTickDelay)) {
			this.playAttackSound();
			knockbackPower = ModConfig.server.knockbackParry;
			parry = true;
		}
		//Shield broken by damage
		else if(damageAmount > shieldProtection) {
			this.world.playSound(null, this.getPosition(), this.getDeathSound(), SoundCategory.HOSTILE, 1.0F, 0.3F);
			lycanitesTweaks$shieldCooldown = (int)Math.max(ModConfig.server.cooldownTicksMinimum, Math.min(ModConfig.server.cooldownTicksMaximum, ((damageAmount - shieldProtection) * ModConfig.server.cooldownTicksScaling)));
			this.currentBlockingTime = 0;
			knockbackPower = ModConfig.server.knockbackBreak;
			broken = true;
		}
		//Normal block
		else{

		}

		if(!damageSrc.isProjectile() && knockbackPower > 0) {
			attacker.knockBack(this, knockbackPower, this.posX - attacker.posX, this.posZ - attacker.posZ);
			if(attacker instanceof EntityPlayerMP) {
				((EntityPlayerMP)attacker).connection.sendPacket(new SPacketEntityVelocity(attacker));
			}
			if(parry || broken) lycanitesTweaks$doShieldPotionEffects(attacker, this, parry);
		}
	}
	
	@Unique
	private static void lycanitesTweaks$doShieldPotionEffects(EntityLivingBase attacker, EntityLivingBase defender, boolean parry) {
		List<PotionEntry> attackerEffect;
		List<PotionEntry> defenderEffect;
		
		if(parry) {
			attackerEffect = ModConfig.server.getEffectAttackerParry();
			defenderEffect = ModConfig.server.getEffectDefenderParry();
		}
		else {
			attackerEffect = ModConfig.server.getEffectAttackerBreak();
			defenderEffect = ModConfig.server.getEffectDefenderBreak();
		}
		
		if(attackerEffect != null) {
			for(PotionEntry entry : attackerEffect) {
				attacker.addPotionEffect(new PotionEffect(entry.getPotion(), entry.getDuration(), entry.getAmplifier()));
			}
		}
		if(defenderEffect != null) {
			for(PotionEntry entry : defenderEffect) {
				defender.addPotionEffect(new PotionEffect(entry.getPotion(), entry.getDuration(), entry.getAmplifier()));
			}
		}
	}
}