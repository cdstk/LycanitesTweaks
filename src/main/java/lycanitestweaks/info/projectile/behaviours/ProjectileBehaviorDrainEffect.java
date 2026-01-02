package lycanitestweaks.info.projectile.behaviours;

import com.google.gson.JsonObject;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.helpers.JSONHelper;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviour;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectileBehaviorDrainEffect extends ProjectileBehaviour {

    private final Set<Potion> effectsList = new HashSet<>();
    private boolean effectsWhitelist = false;

    private boolean stealEffect = false;
    private boolean allEffects = false;
    private float chance = 1.0F;

    @Override
    public void loadFromJSON(JsonObject json) {
        if(json.has("effects")){
            effectsList.clear();
            for(String effectName : JSONHelper.getJsonStrings(json.get("effects").getAsJsonArray())){
                Potion effect = GameRegistry.findRegistry(Potion.class).getValue(new ResourceLocation(effectName));
                if(effect != null) {
                    effectsList.add(effect);
                }
            }
        }
        if(json.has("effectsWhitelist")){
            this.effectsWhitelist = json.get("effectsWhitelist").getAsBoolean();
        }
        if(json.has("stealEffect")){
            this.stealEffect = json.get("stealEffect").getAsBoolean();
        }
        if(json.has("allEffects")){
            this.allEffects = json.get("allEffects").getAsBoolean();
        }
        if(json.has("chance")){
            this.chance = json.get("chance").getAsFloat();
        }
    }

    @Override
    public void onProjectileDamage(BaseProjectileEntity projectile, World world, EntityLivingBase target, float damage) {
        if(projectile.getThrower() == null || target == null || world.isRemote) {
            return;
        }

        List<PotionEffect> toRemove = new ArrayList<>();

        if(world.rand.nextFloat() < this.chance) {
            List<PotionEffect> goodEffects = new ArrayList<>();
            for (PotionEffect effect : target.getActivePotionEffects()) {
                if (!effect.getPotion().isBadEffect()) goodEffects.add(effect);
            }

            goodEffects = goodEffects.stream().filter(potionEffect -> {
                boolean match = this.effectsList.contains(potionEffect.getPotion());
                return match == this.effectsWhitelist;
            })
            .collect(Collectors.toList());

            if(this.allEffects) toRemove = goodEffects;
            else if(!goodEffects.isEmpty()) toRemove.add(goodEffects.get(world.rand.nextInt(goodEffects.size())));
        }

        toRemove.forEach((effect) -> {
            target.removePotionEffect(effect.getPotion());
            if(this.stealEffect) projectile.getThrower().addPotionEffect(effect);
        });
    }
}
