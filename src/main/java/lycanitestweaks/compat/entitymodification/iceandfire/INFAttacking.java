package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import com.github.alexthe666.iceandfire.entity.EntityDreadScuttler;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.entity.util.EntityDreadMob;
import lycanitestweaks.compat.IceAndFireHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleAttacking;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;

public class INFAttacking extends ToggleAttacking {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":attacking";

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityAmphithere) {
            ((EntityAmphithere) entity).attackEntityAsMob(entity);
        }
        else if(entity instanceof EntityCockatrice) {
            ((EntityCockatrice) entity).attackEntityAsMob(entity);
        }
        else if(entity instanceof EntityCyclops) {
            EntityCyclops infEntity = (EntityCyclops) entity;
            Animation[] animations = infEntity.getAnimations();
            infEntity.setAnimation(animations[infEntity.getRNG().nextInt(animations.length)]);
        }
        else if(entity instanceof EntityDeathWorm) {
            ((EntityDeathWorm) entity).setAnimation(EntityDeathWorm.ANIMATION_BITE);
        }
        else if(entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            if(dragon.isFlying()) {
                dragon.setBreathingFire(!dragon.isBreathingFire());
            }
            else {
                if (dragon.getAnimation() == EntityDragonBase.ANIMATION_BITE) {
                    dragon.setAnimation(EntityDragonBase.ANIMATION_SHAKEPREY);
                } else {
                    switch (dragon.getRNG().nextInt(4)) {
                        case 0:
                            dragon.setAnimation(EntityDragonBase.ANIMATION_ROAR);
                            break;
                        case 1:
                            dragon.setAnimation(EntityDragonBase.ANIMATION_BITE);
                            break;
                        case 2:
                            dragon.setAnimation(EntityDragonBase.ANIMATION_WINGBLAST);
                            break;
                        case 3:
                            dragon.setAnimation(EntityDragonBase.ANIMATION_TAILWHACK);
                            break;

                    }
                }
            }
        }
        else if(entity instanceof EntityGorgon) {
            EntityGorgon infEntity = (EntityGorgon) entity;
            Animation[] animations = infEntity.getAnimations();
            infEntity.setAnimation(animations[infEntity.getRNG().nextInt(animations.length)]);
        }
        else if(entity instanceof EntityHippogryph) {
            ((EntityHippogryph) entity).attackEntityAsMob(entity);
        }
        else if(entity instanceof EntityMyrmexBase) {
            ((EntityMyrmexBase) entity).attackEntityAsMob(entity);
        }
        else if(entity instanceof EntitySeaSerpent) {
            ((EntitySeaSerpent) entity).setAnimation(EntitySeaSerpent.ANIMATION_BITE);
        }
        else if(entity instanceof EntitySiren) {
            EntitySiren infEntity = (EntitySiren) entity;
            if(infEntity.isAgressive()) {
                if(infEntity.getAnimation() == EntitySiren.NO_ANIMATION) {
                    infEntity.attackEntityAsMob(entity);
                }
                else {
                    infEntity.setAggressive(false);
                }
            }
            else {
                infEntity.setAggressive(true);
            }
        }
        else if(entity instanceof EntityStymphalianBird) {
            EntityStymphalianBird infEntity = (EntityStymphalianBird) entity;
            Animation animation = infEntity.isFlying() ? EntityStymphalianBird.ANIMATION_SHOOT_ARROWS : EntityStymphalianBird.ANIMATION_PECK;
            infEntity.setAnimation(animation);
        }
        else if(entity instanceof EntityTroll) {
            ((EntityTroll) entity).attackEntityAsMob(entity);
        }

        if(IceAndFireHandler.hasDreadHydra()) {
            if(entity instanceof EntityDreadBeast) {
                ((EntityDreadBeast) entity).attackEntityAsMob(entity);
            }
            else if(entity instanceof EntityDreadGhoul) {
                ((EntityDreadGhoul) entity).attackEntityAsMob(entity);
            }
            else if(entity instanceof EntityDreadLich) {
                ((EntityDreadLich) entity).setAnimation(EntityDreadLich.ANIMATION_SUMMON);
            }
            else if(entity instanceof EntityDreadScuttler) {
                ((EntityDreadScuttler) entity).attackEntityAsMob(entity);
            }
            else if(entity instanceof EntityHydra) {
                EntityHydra hydra = (EntityHydra) entity;
                int index = hydra.getRNG().nextInt(hydra.getHeadCount());
                entity.handleStatusUpdate((byte) (40 + index));
            }
            else if(entity instanceof EntityDreadKnight) {
                ((EntityDreadKnight) entity).swingArm(EnumHand.MAIN_HAND);
            }
            else if(entity instanceof EntityDreadThrall) {
                ((EntityDreadThrall) entity).swingArm(EnumHand.MAIN_HAND);
            }
            else if(entity instanceof EntityDreadQueen) {
                ((EntityDreadQueen) entity).setAnimation(IAnimatedEntity.NO_ANIMATION);
                ((EntityDreadQueen) entity).swingArm(EnumHand.MAIN_HAND);
            }
            else if(IceAndFireHandler.isRLCraft()) {
                if(entity instanceof EntityDreadMob) {
                    if(entity instanceof IAnimatedEntity) {
                        ((IAnimatedEntity) entity).setAnimation(IAnimatedEntity.NO_ANIMATION);
                    }
                    super.modifyEntity(entity);
                }
            }
        }

        if(IceAndFireHandler.hasGhost()) {
            if(entity instanceof EntityGhost) {
                EntityGhost infEntity = (EntityGhost) entity;
                Animation[] animations = infEntity.getAnimations();
                infEntity.setAnimation(animations[infEntity.getRNG().nextInt(animations.length)]);
            }
        }
    }
}
