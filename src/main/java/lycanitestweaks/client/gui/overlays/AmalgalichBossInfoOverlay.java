package lycanitestweaks.client.gui.overlays;

import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketLycanitesBossInfo;
import lycanitestweaks.util.IBossInfo_LycanitesBossMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AmalgalichBossInfoOverlay extends LycanitesBossInfoOverlay {

    private static ResourceLocation fearTexture = null;
    private static ResourceLocation decayTexture = null;

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(!event.getWorld().isRemote) return;
        if(event.getEntity() instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) event.getEntity();
            if(creature.creatureInfo.getName().equals("amalgalich"))
                PacketHandler.instance.sendToServer(new PacketLycanitesBossInfo(creature));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void bossBarRenderPre(RenderGameOverlayEvent.BossInfo event) {
        if(event.isCanceled()) return;
        if(!(event.getBossInfo() instanceof IBossInfo_LycanitesBossMixin)) return;
        IBossInfo_LycanitesBossMixin lycanitesBossInfo = (IBossInfo_LycanitesBossMixin) event.getBossInfo();

        if(!lycanitesBossInfo.lycanitesTweaks$isLycanitesBoss()) return;
        if(!(lycanitesBossInfo.lycanitesTweaks$getLycanitesEntity() instanceof EntityAmalgalich)) return;
        EntityAmalgalich amalgalich = (EntityAmalgalich) lycanitesBossInfo.lycanitesTweaks$getLycanitesEntity();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int lhs = event.getX();
        int rhs = event.getX() + 182;
        int drawY = (int) (event.getY() - ((float) event.getIncrement() / 2));

        if(fearTexture == null) fearTexture = AssetManager.getTexture("effect.fear");
        if(fearTexture == null) fearTexture = amalgalich.creatureInfo.getIcon();
        if(decayTexture == null) decayTexture = AssetManager.getTexture("effect.decay");
        if(decayTexture == null) decayTexture = amalgalich.creatureInfo.getIcon();

        // Left Hand Side
        // Consumption
        if (amalgalich.getConsumptionAnimation() == 1) {
            drawTexture(
                    decayTexture,
                    lhs -= 18,
                    drawY,
                    0,
                    0,
                    0,
                    1,
                    1,
                    16,
                    16
            );
        }
        else if(amalgalich.getConsumptionAnimation() > 0) {
            drawTexture(
                    fearTexture,
                    lhs -= 18,
                    drawY,
                    0,
                    0,
                    0,
                    1,
                    1,
                    16,
                    16
            );
        }
        // Right Hand Side
        drawRHS(amalgalich, rhs, drawY, fontRenderer);
    }
}
