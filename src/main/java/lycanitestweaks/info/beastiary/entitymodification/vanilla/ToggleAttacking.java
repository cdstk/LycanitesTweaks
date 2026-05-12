package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.EnumHand;

public class ToggleAttacking extends AbstractToggleState {

    public static final String TYPE_VALUE = "attacking";

    protected boolean toggle = false;

    @Override
    public String getOptionLangKey() {
        return "creature.status.attack";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityEnderman) {
            EntityEnderman enderman = (EntityEnderman) entity;
            if(enderman.isScreaming()) {
                enderman.setAttackTarget(null);
            }
            else {
                enderman.setAttackTarget(LycanitesTweaks.PROXY.getClientPlayer());
            }
        }
        else if(entity instanceof EntityGhast) {
            ((EntityGhast) entity).setAttacking(!((EntityGhast) entity).isAttacking());
        }
        else if(entity instanceof EntityIllusionIllager) {
            ((EntityIllusionIllager) entity).setSwingingArms(!((EntityIllusionIllager) entity).isAggressive());
        }
        else if(entity instanceof EntityShulker) {
            EntityShulker shulker = (EntityShulker) entity;
            shulker.updateArmorModifier(
                    shulker.getPeekTick() == 0
                    ? 100
                    : 0
            );
        }
        else if(entity instanceof EntityWolf) {
            ((EntityWolf) entity).setAngry(!((EntityWolf) entity).isAngry());
        }
        else if(entity instanceof EntityVex) {
            ((EntityVex) entity).setCharging(!((EntityVex) entity).isCharging());
        }
        else if(entity instanceof EntityVindicator) {
            ((EntityVindicator) entity).setAggressive(!((EntityVindicator) entity).isAggressive());
        }

        if(entity instanceof IRangedAttackMob) {
            this.toggle = !this.toggle;
            ((IRangedAttackMob) entity).setSwingingArms(this.toggle);
        }
        else if(entity instanceof EntityLivingBase) {
            ((EntityLivingBase) entity).swingArm(EnumHand.MAIN_HAND);
        }
    }
}
