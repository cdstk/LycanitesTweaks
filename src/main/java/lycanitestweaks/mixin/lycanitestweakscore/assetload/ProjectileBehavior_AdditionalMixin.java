package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviour;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.info.projectile.behaviours.ProjectileBehaviorDrainEffect;
import lycanitestweaks.info.projectile.behaviours.ProjectileBehaviourAdvancedFireProjectiles;
import lycanitestweaks.info.projectile.behaviours.ProjectileBehaviourAdvancedSummon;
import lycanitestweaks.info.projectile.behaviours.ProjectileBehaviourTargetedForce;
import lycanitestweaks.info.projectile.behaviours.ProjectileBehaviourTryToDamageMinion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileBehaviour.class)
public abstract class ProjectileBehavior_AdditionalMixin {

    @Unique
    private final static String ADVANCED_FIRE_PROJECTILES = LycanitesTweaks.MODID + ":advancedFireProjectiles";
    @Unique
    private final static String ADVANCED_SUMMON = LycanitesTweaks.MODID + ":advancedSummon";
    @Unique
    private final static String DRAIN_EFFECT = LycanitesTweaks.MODID + ":drainEffect";
    @Unique
    private final static String TARGETED_FORCE = LycanitesTweaks.MODID + ":targetedForce";
    @Unique
    private final static String TRY_DAMAGE_MINION = LycanitesTweaks.MODID + ":tryToDamageMinion";

    @ModifyReturnValue(
            method = "createFromJSON",
            at = @At(value = "RETURN", ordinal = 0),
            remap = false
    )
    private static ProjectileBehaviour lycanitesTweaks_lycanitesMobsProjectileBehaviour_createFromJSON(ProjectileBehaviour original, @Local(argsOnly = true) JsonObject json, @Local String type){
        if(ADVANCED_FIRE_PROJECTILES.equals(type)){
            ProjectileBehaviour projectileBehaviour = new ProjectileBehaviourAdvancedFireProjectiles();
            projectileBehaviour.type = type;
            projectileBehaviour.loadFromJSON(json);
            return projectileBehaviour;
        }
        if(ADVANCED_SUMMON.equals(type)){
            ProjectileBehaviour projectileBehaviour = new ProjectileBehaviourAdvancedSummon();
            projectileBehaviour.type = type;
            projectileBehaviour.loadFromJSON(json);
            return projectileBehaviour;
        }
        if(DRAIN_EFFECT.equals(type)){
            ProjectileBehaviour projectileBehaviour = new ProjectileBehaviorDrainEffect();
            projectileBehaviour.type = type;
            projectileBehaviour.loadFromJSON(json);
            return projectileBehaviour;
        }
        if(TARGETED_FORCE.equals((type))){
            ProjectileBehaviour projectileBehaviour = new ProjectileBehaviourTargetedForce();
            projectileBehaviour.type = type;
            projectileBehaviour.loadFromJSON(json);
            return projectileBehaviour;
        }
        if(TRY_DAMAGE_MINION.equals(type)){
            ProjectileBehaviour projectileBehaviour = new ProjectileBehaviourTryToDamageMinion();
            projectileBehaviour.type = type;
            projectileBehaviour.loadFromJSON(json);
            return projectileBehaviour;
        }
        return original;
    }
}
