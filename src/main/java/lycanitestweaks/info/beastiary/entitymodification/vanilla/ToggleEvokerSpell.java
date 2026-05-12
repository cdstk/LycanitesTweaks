package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.nbt.NBTTagCompound;

public class ToggleEvokerSpell extends AbstractToggleState {

    public static final String TYPE_VALUE = "evokerSpell";

    private static NBTTagCompound notCasting = null;
    private static NBTTagCompound castingSpell = null;

    @Override
    public String getOptionLangKey() {
        return "creature.status.casting_spell";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntitySpellcasterIllager) {
            EntitySpellcasterIllager caster = (EntitySpellcasterIllager) entity;
            if(caster.isSpellcasting()) {
                caster.setSpellType(EntitySpellcasterIllager.SpellType.NONE);
                if(notCasting == null) {
                    notCasting = new NBTTagCompound();
                    notCasting.setInteger("SpellTicks", 0);
                }
                caster.readFromNBT(notCasting);
            }
            else {
                caster.setSpellType(EntitySpellcasterIllager.SpellType.getFromId(1));
                if(castingSpell == null) {
                    castingSpell = new NBTTagCompound();
                    castingSpell.setInteger("SpellTicks", 100);
                }
                caster.readFromNBT(castingSpell);
            }
        }
    }
}
