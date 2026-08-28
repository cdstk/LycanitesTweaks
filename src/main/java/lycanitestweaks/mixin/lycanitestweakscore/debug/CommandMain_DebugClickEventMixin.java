package lycanitestweaks.mixin.lycanitestweakscore.debug;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.command.CommandMain;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CommandMain.class)
public abstract class CommandMain_DebugClickEventMixin {

    @ModifyArg(
            method = "execute",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/command/ICommandSender;sendMessage(Lnet/minecraft/util/text/ITextComponent;)V", ordinal = 14)
    )
    private ITextComponent lycanitesTweaks_lycanitesMobsCommandMain_executeLocateChatCommand(ITextComponent component, @Local(argsOnly = true) ICommandSender commandSender){
        int start, end;

        start = component.getUnformattedText().indexOf("/tp ");
        end = component.getUnformattedText().indexOf(" - Origin: ");

        if(start != -1 && end > start) {
            String command = component.getUnformattedText().substring(start, end);
            command = command.replaceFirst("/tp", "/tp @s");

            component.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(command)));
            component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        return component;
    }
}
