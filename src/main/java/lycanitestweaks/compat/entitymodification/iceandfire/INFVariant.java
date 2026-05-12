package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.google.gson.JsonObject;
import lycanitestweaks.compat.IceAndFireHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;

public class INFVariant extends CycleIndexedVariant {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":variant";

    public INFVariant() {
        super();
    }

    public INFVariant(JsonObject json) {
        super(json);
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        if(EntityAmphithere.class == entityInfo.getEntityClass()) {
            this.variantCount = 6;
        }
        else if(EntityCyclops.class == entityInfo.getEntityClass()) {
            this.variantCount = 4;
        }
        else if(EntityDeathWorm.class == entityInfo.getEntityClass()) {
            this.variantCount = 3;
        }
        else if(EntityDragonBase.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.variantCount = 4;
        }
        else if(EntityHippocampus.class == entityInfo.getEntityClass()) {
            this.variantCount = 6;
        }
        else if(EntityHippogryph.class == entityInfo.getEntityClass()) {
            this.variantCount = EnumHippogryphTypes.values().length;
        }
        else if(EntityMyrmexBase.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.variantCount = 2;
        }
        else if(EntityPixie.class == entityInfo.getEntityClass()) {
            this.variantCount = 5;
        }
        else if(EntitySeaSerpent.class == entityInfo.getEntityClass()) {
            this.variantCount = EnumSeaSerpent.values().length;
        }
        else if(EntitySiren.class == entityInfo.getEntityClass()) {
            this.variantCount = 4;
        }
        else if(EntityTroll.class == entityInfo.getEntityClass()) {
            this.variantCount = EnumTroll.values().length;
        }

        if(IceAndFireHandler.hasDreadHydra()) {
            if(EntityDreadBeast.class == entityInfo.getEntityClass()) {
                this.variantCount = 2;
            }
            else if(EntityDreadGhoul.class == entityInfo.getEntityClass()) {
                this.variantCount = 3;
            }
            else if(EntityDreadKnight.class == entityInfo.getEntityClass()) {
                this.variantCount = 3;
            }
            else if(EntityDreadLich.class == entityInfo.getEntityClass()) {
                this.variantCount = 5;
            }
            else if(EntityDreadThrall.class == entityInfo.getEntityClass()) {
                this.variantCount = 8;
            }
            else if(EntityHydra.class == entityInfo.getEntityClass()) {
                this.variantCount = 3;
            }
        }

        if(IceAndFireHandler.hasGhost()) {
            if(EntityGhost.class == entityInfo.getEntityClass()) {
                this.variantCount = 3;
            }
        }
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.variantIndex >= this.variantCount) this.variantIndex = 0;
        if(entity instanceof EntityAmphithere) {
            ((EntityAmphithere) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityCyclops) {
            ((EntityCyclops) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityDeathWorm) {
            ((EntityDeathWorm) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityDragonBase) {
            ((EntityDragonBase) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityHippocampus) {
            ((EntityHippocampus) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityHippogryph) {
            ((EntityHippogryph) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityMyrmexBase) {
            ((EntityMyrmexBase) entity).setJungleVariant(!((EntityMyrmexBase) entity).isJungle());
        }
        else if(entity instanceof EntityPixie) {
            ((EntityPixie) entity).setColor(this.variantIndex++);
        }
        else if(entity instanceof EntitySeaSerpent) {
            ((EntitySeaSerpent) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntitySiren) {
            ((EntitySiren) entity).setHairColor(this.variantIndex++);
        }
        else if(entity instanceof EntityTroll) {
            if(this.variantIndex >= EnumTroll.values().length) this.variantIndex = 0;
            ((EntityTroll) entity).setType(EnumTroll.values()[this.variantIndex++]);
        }

        if(IceAndFireHandler.hasDreadHydra()) {
            if(entity instanceof EntityDreadBeast) {
                ((EntityDreadBeast) entity).setVariant(this.variantIndex++);
            }
            else if(entity instanceof EntityDreadGhoul) {
                ((EntityDreadGhoul) entity).setVariant(this.variantIndex++);
            }
            else if(entity instanceof EntityDreadKnight) {
                ((EntityDreadKnight) entity).setArmorVariant(this.variantIndex++);
            }
            else if(entity instanceof EntityDreadLich) {
                ((EntityDreadLich) entity).setVariant(this.variantIndex++);
            }
            else if(entity instanceof EntityDreadThrall) {
                ((EntityDreadThrall) entity).setArmorVariant(this.variantIndex++);
            }
            else if(entity instanceof EntityHydra) {
                ((EntityHydra) entity).setVariant(this.variantIndex++);
            }
        }

        if(IceAndFireHandler.hasGhost()) {
            if(entity instanceof EntityGhost) {
                ((EntityGhost) entity).setColor(this.variantIndex++);
            }
        }
    }
}
