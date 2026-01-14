package lycanitestweaks.command;

import com.lycanitesmobs.ExtendedWorld;
import com.lycanitesmobs.core.mobevent.MobEvent;
import com.lycanitesmobs.core.mobevent.MobEventManager;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LycanitesTweaksCommand extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return LycanitesTweaks.MODID;
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender commandSender) {
        return "/lycanitestweaks <saveevent> <player> [eventname] [eventduration]";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 2){
            EntityPlayer targetPlayer = CommandBase.getEntity(server, sender, args[1], EntityPlayer.class);
            if(targetPlayer.world != null){
                ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(targetPlayer);
                ExtendedWorld extendedWorld = ExtendedWorld.getForWorld(targetPlayer.world);
                if(ltp != null && extendedWorld != null){
                    if(extendedWorld.serverWorldEventPlayer != null){
                        ltp.setSavedMobEvent(
                                extendedWorld.serverWorldEventPlayer.mobEvent.name,
                                extendedWorld.serverWorldEventPlayer.mobEvent.duration - extendedWorld.serverWorldEventPlayer.ticks
                        );
                    }
                    else {
                        throw new CommandException("commands.lycanitestweaks.saveevent.missing");
                    }
                }
            }
        }
        else {
            if (args.length < 4) throw new CommandException("commands.lycanitestweaks.invalidusage");

            EntityPlayer targetPlayer = CommandBase.getEntity(server, sender, args[1], EntityPlayer.class);
            if(targetPlayer.world != null){
                ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(targetPlayer);
                if(ltp != null){
                    ltp.setSavedMobEvent(args[2], parseInt(args[3]));
                }
            }
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
            completions.add("saveevent");
            //return CommandBase.getListOfStringsMatchingLastWord(args, completions); //only needed once there's multiple commands
        }
        else if (args.length == 2){
            completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()));
        }
        else if (args.length == 3){
            completions.addAll(MobEventManager.getInstance().mobEvents.keySet());
        }
        else if (args.length == 4){
            MobEvent mobEvent = MobEventManager.getInstance().getMobEvent(args[2]);
            if(mobEvent != null) completions.add(String.valueOf(mobEvent.duration));
        }
        return completions;
    }
}
