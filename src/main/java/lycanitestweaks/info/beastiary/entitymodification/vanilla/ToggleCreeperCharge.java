package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;

public class ToggleCreeperCharge extends AbstractToggleState {

    public static final String TYPE_VALUE = "creeperCharged";

    private static NBTTagCompound notCharged = null;

    @Override
    public String getOptionLangKey() {
        return "creature.status.charged";
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        this.knowledgeRank = 2;
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) entity;
            if(creeper.getPowered()) {
                if(notCharged == null) {
                    notCharged = new NBTTagCompound();
                    notCharged.setBoolean("powered", false);
                }
                creeper.readFromNBT(notCharged);
                creeper.extinguish();
            }
            else {
                creeper.onStruckByLightning(
                        new EntityLightningBolt(
                                creeper.world,
                                creeper.posX,
                                creeper.posY,
                                creeper.posZ,
                                true
                        ));
            }
        }
    }
}
