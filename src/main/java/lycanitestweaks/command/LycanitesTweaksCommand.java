package lycanitestweaks.command;

import com.lycanitesmobs.ExtendedWorld;
import com.lycanitesmobs.core.mobevent.MobEvent;
import com.lycanitesmobs.core.mobevent.MobEventManager;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.info.beastiary.GenericBestiary;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.GenericEntityKnowledge;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LycanitesTweaksCommand extends CommandBase {

    public static final String SAVE_EVENT = "saveevent";

    public static final String BESTIARY = "bestiary";
    public static final String ADD = "add";
    public static final String COMPLETE = "complete";
    public static final String CLEAR = "clear";

    public static final String BESTIARY_RELOAD = "bestiaryreload";

    @Override
    @Nonnull
    public String getName() {
        return LycanitesTweaks.MODID;
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender commandSender) {
        StringBuilder usage = new StringBuilder();

        usage.append("/lycanitestweaks <bestiaryreload>").append("\n");
        usage.append("/lycanitestweaks <saveevent> <player> [eventname] [eventduration]").append("\n");
        usage.append("/lycanitestweaks <bestiary> <player>  <add, complete, clear> [addEntityID, completeRank] [addRank]");

        return usage.toString();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if(args.length < 1) {
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            throw new CommandException("commands.lycanitestweaks.invalidusage");
        }

        switch (args[0]) {
            case BESTIARY_RELOAD:
                GenericEntityInfoManager.getInstance().reload();
                sender.sendMessage(new TextComponentTranslation("commands.lycanitestweaks.bestiary.reload"));
                break;
            case SAVE_EVENT: {
                EntityPlayer targetPlayer = CommandBase.getEntity(server, sender, args[1], EntityPlayer.class);
                if (targetPlayer.world != null) {
                    ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(targetPlayer);

                    String eventName = args.length > 2 ? args[2] : "";
                    int eventDuration = args.length > 3 ? parseInt(args[3]) : 0;

                    if (ltp != null) {
                        if (eventName.isEmpty()) {
                            ExtendedWorld extendedWorld = ExtendedWorld.getForWorld(targetPlayer.world);
                            if (extendedWorld.serverWorldEventPlayer != null) {
                                eventName = extendedWorld.serverWorldEventPlayer.mobEvent.name;
                                eventDuration = extendedWorld.serverWorldEventPlayer.mobEvent.duration;
                            } else {
                                throw new CommandException("commands.lycanitestweaks.saveevent.missing");
                            }
                        }
                        ltp.setSavedMobEvent(eventName, eventDuration);
                    }
                }
                break;
            }
            case BESTIARY: {
                EntityPlayer targetPlayer = CommandBase.getEntity(server, sender, args[1], EntityPlayer.class);
                ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(targetPlayer);
                if (ltp != null) {
                    GenericBestiary bestiary = ltp.getBestiary();
                    switch (args[2]) {
                        case ADD: {
                            if (args.length < 4) {
                                throw new CommandException("lyc.command.beastiary.add.invalid");
                            }

                            String entityName = args[3];
                            int rank = args.length > 4 ? parseInt(args[4]) : 3;

                            GenericEntityInfo entityInfo = GenericEntityInfoManager.getInstance().getEntityInfo(entityName);
                            if (entityInfo == null) {
                                throw new CommandException("lyc.command.beastiary.add.unknown");
                            }

                            GenericEntityKnowledge entityKnowledge = new GenericEntityKnowledge(bestiary, entityInfo.getEntityId(), rank, 0);
                            if (bestiary.updateEntityKnowledge(entityKnowledge, true)) {
                                bestiary.sendAddedMessage(entityKnowledge);
                                bestiary.sendToClient(entityKnowledge);
                            } else {
                                bestiary.sendKnownMessage(entityKnowledge);
                            }
                            break;
                        }
                        case COMPLETE: {
                            int rank = args.length > 3 ? parseInt(args[3]) : 3;

                            GenericBestiary beastiary = ltp.getBestiary();
                            for (GenericEntityInfo entityInfo : GenericEntityInfoManager.getInstance().entities.values()) {
                                beastiary.updateEntityKnowledge(new GenericEntityKnowledge(beastiary, entityInfo.getEntityId(), rank, 0), true);
                            }

                            beastiary.sendAllToClient();
                            sender.sendMessage(new TextComponentTranslation("lyc.command.beastiary.complete"));
                            break;
                        }
                        case CLEAR:
                            ltp.getBestiary().entityKnowledgeMap.clear();
                            ltp.getBestiary().sendAllToClient();
                            sender.sendMessage(new TextComponentTranslation("commands.lycanitestweaks.bestiary.clear", ltp.getPlayer().getName()));
                            break;
                    }
                }
                break;
            }
            default:
                throw new CommandException("commands.lycanitestweaks.invalidusage");
        }
    }

    // ==================================================
    //                     Permission
    // ==================================================
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender commandSender) {
        if(commandSender instanceof EntityPlayer) {
            if(!commandSender.canUseCommand(this.getRequiredPermissionLevel(), this.getName()))
                return false;
        }
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, SAVE_EVENT, BESTIARY, BESTIARY_RELOAD));
        }
        else if (args.length == 2){
            switch (args[0]) {
                case SAVE_EVENT:
                case BESTIARY:
                    completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()));
                    break;
            }
        }
        else if (args.length == 3){
            switch (args[0]) {
                case SAVE_EVENT:
                    completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, MobEventManager.getInstance().mobEvents.keySet()));
                    break;
                case BESTIARY:
                    completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, ADD, COMPLETE, CLEAR));
                    break;
            }
        }
        else if (args.length == 4){
            MobEvent mobEvent = MobEventManager.getInstance().getMobEvent(args[2]);
            if(mobEvent != null) completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, Collections.singletonList(mobEvent.duration)));
        }
        return completions;
    }
}
