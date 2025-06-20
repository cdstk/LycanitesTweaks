package lycanitestweaks.handlers.config;

import net.minecraftforge.common.config.Config;

public class ClientConfig {
    @Config.Comment("Enable Debug logging for auto information dumping")
    @Config.Name("Enables debug logging automatic")
    public boolean debugLoggerAutomatic = false;

    @Config.Comment("Enable Debug logging for manually triggered information")
    @Config.Name("Enables debug logging trigger")
    public boolean debugLoggerTrigger = false;

    @Config.Comment("Enable Debug logging for constant tick information dumping")
    @Config.Name("Enables debug logging tick")
    public boolean debugLoggerTick = false;

    @Config.Comment("Example client side config option")
    @Config.Name("Example Client Option")
    public boolean exampleClientOption = true;

    @Config.Comment("Test Int")
    @Config.Name("Test Int")
    public int testInt = 1;

    @Config.Comment("Test Double")
    @Config.Name("Test Double")
    public double testDouble = 1.0D;

    @Config.Comment("Test Float")
    @Config.Name("Test Float")
    public float testFloat = 1.0F;

    @Config.Comment("Translate Client Text from data that is normally stored in English (such as NBT) instead of displaying the raw string")
    @Config.Name("Translate Text When Possible")
    public boolean translateWhenPossible = true;
}
