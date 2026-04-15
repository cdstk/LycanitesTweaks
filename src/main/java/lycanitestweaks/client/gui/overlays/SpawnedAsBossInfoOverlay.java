package lycanitestweaks.client.gui.overlays;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.util.IBossInfo_LycanitesBossMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnedAsBossInfoOverlay extends LycanitesBossInfoOverlay {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void bossBarRenderPre(RenderGameOverlayEvent.BossInfo event) {
        if(event.isCanceled()) return;
        if(!(event.getBossInfo() instanceof IBossInfo_LycanitesBossMixin)) return;
        IBossInfo_LycanitesBossMixin lycanitesBossInfo = (IBossInfo_LycanitesBossMixin) event.getBossInfo();

        if(!lycanitesBossInfo.lycanitesTweaks$isLycanitesBoss()) return;
        if(!(lycanitesBossInfo.lycanitesTweaks$getLycanitesEntity() instanceof BaseCreatureEntity)) return;
        BaseCreatureEntity creature = (BaseCreatureEntity) lycanitesBossInfo.lycanitesTweaks$getLycanitesEntity();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int rhs = event.getX() + 182;
        int drawY = (int) (event.getY() - ((float) event.getIncrement() / 2));

        // Right Hand Side
        drawRHS(creature, rhs, drawY, fontRenderer);
    }
}
