package lycanitestweaks.client.gui.overlays;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.IBossInfo_LycanitesBossMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnedAsBossInfoOverlay extends LycanitesBossInfoOverlay {

    private static final List<BouncingTexture> BOUNCING_TEXTURES = new ArrayList<>();
    private static long bouncingAddedTick = 0L;

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

        if (ForgeConfigHandler.clientFeaturesMixinConfig.easterEggBossInfoOverlay) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (bouncingAddedTick != creature.updateTick && player != null && (player.canEntityBeSeen(creature) || player.getDistance(creature) <= 8)) {
                if (creature.getAttackCooldown() == creature.getAttackCooldownMax()) {
                    if (isExperiment69R(creature)) {
                        bouncingAddedTick = creature.updateTick;
                        renderExperiment69R(event.getResolution(), creature.getRNG());
                    } else if (isKrillIssue(creature)) {
                        bouncingAddedTick = creature.updateTick;
                        renderKrillIssue(event.getResolution(), creature.getRNG());
                    }
                }
                if (creature.hurtTime == 9) {
                    if (isNotKotlin(creature)) {
                        bouncingAddedTick = creature.updateTick;
                        renderNotKotlin(event.getResolution(), creature.getRNG());
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void allBossHealthRenderPost(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.BOSSHEALTH) return;

        BOUNCING_TEXTURES.removeIf(bouncingTexture -> bouncingTexture.doRender() == null);
    }

    private static boolean isExperiment69R(BaseCreatureEntity creature) {
        return creature.hasCustomName()
                && creature.creatureInfo.getName().equals("wraamon")
                && creature.getCustomNameTag().equals("Experiment 69R");
    }

    private static boolean isKrillIssue(BaseCreatureEntity creature) {
        return creature.hasCustomName()
                && creature.creatureInfo.getName().equals("krake")
                && creature.getCustomNameTag().contains("rill ")
                && creature.getCustomNameTag().contains("ssue");
    }

    private static boolean isNotKotlin(BaseCreatureEntity creature) {
        return creature.hasCustomName()
                && creature.creatureInfo.getName().equals("kobold")
                && creature.getCustomNameTag().contains("ot ")
                && creature.getCustomNameTag().contains("otlin");
    }

    private static void renderExperiment69R(ScaledResolution scaledResolution, Random random) {
        if(instabilityTexture != null)
            BOUNCING_TEXTURES.add(
                    new BouncingTexture(
                            instabilityTexture,
                            random,
                            scaledResolution,
                            72,
                            72,
                            180
                    )
            );
    }

    private static void renderKrillIssue(ScaledResolution scaledResolution, Random random) {
        if(krillIssue != null)
            BOUNCING_TEXTURES.add(
                    new BouncingTexture(
                            krillIssue,
                            random,
                            scaledResolution,
                            137,
                            137,
                            180
                    )
            );
    }

    private static void renderNotKotlin(ScaledResolution scaledResolution, Random random) {
        if(random.nextInt(2) == 0) {
            if(hotKotlin != null)
                BOUNCING_TEXTURES.add(
                        new BouncingTexture(
                                hotKotlin,
                                random,
                                scaledResolution,
                                321,
                                554,
                                180
                        )
                );
        }
        else {
            if(kotlin != null)
                BOUNCING_TEXTURES.add(
                        new BouncingTexture(
                                kotlin,
                                random,
                                scaledResolution,
                                321,
                                503,
                                180
                        )
                );
        }
    }
}
