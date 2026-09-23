package lycanitestweaks.mixin.collisiondamage;

import collision.packets.PacketCollisionS;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PacketCollisionS.CollisionMessageHandler.class)
public abstract class CollisionMessageHandler_Mixin {

    @WrapOperation(
            method = "lambda$onMessage$0",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
    )
    private static boolean lycanitesTweaks_collisionDamageCollisionMessageHandler_onMessageWithInstability(EntityPlayer player, DamageSource source, float amount, Operation<Boolean> original) {
        PotionBase instability = ObjectManager.getEffect("instability");
        if (instability != null && player.isPotionActive(instability)) {
            source = new DamageSource(source.getDamageType()) {
                @Override
                public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                    return super.getDeathMessage(entityLivingBaseIn)
                            .appendSibling(new TextComponentTranslation("death.attack.collision.instability"))
                            .appendSibling(new TextComponentTranslation(instability.getName())
                            );
                }

            }.setDamageBypassesArmor();
        }
        return original.call(player, source, amount);
    }
}
