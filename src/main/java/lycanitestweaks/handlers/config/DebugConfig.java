package lycanitestweaks.handlers.config;

import net.minecraftforge.common.config.Config;

public class DebugConfig {

//    @Config.Comment("Example client side config option")
//    @Config.Name("Example Client Option")
//    public boolean exampleClientOption = true;
//
//    @Config.Comment("Test Int")
//    @Config.Name("Test Int")
//    public int testInt = 1;
//
//    @Config.Comment("Test Double")
//    @Config.Name("Test Double")
//    public double testDouble = 1.0D;
//
//    @Config.Comment("Test Float")
//    @Config.Name("Test Float")
//    public float testFloat = 1.0F;

    @Config.Comment("Logging for information that is automatic but not available every tick")
    @Config.Name("Debug Log Automatic Information")
    public boolean debugLoggerAutomatic = false;

    @Config.Comment("Logging for information that is manually triggered by players")
    @Config.Name("Debug Log Manual Trigger Information")
    public boolean debugLoggerTrigger = false;

    @Config.Comment("Logging for information that can be dumped every tick")
    @Config.Name("Debug Log Tick Information")
    public boolean debugLoggerTick = false;

    @Config.Comment("Logging for information about parsing entries from the config, may output every tick")
    @Config.Name("Debug Log Config")
    public boolean debugConfig = false;
}
