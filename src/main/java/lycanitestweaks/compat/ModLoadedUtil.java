package lycanitestweaks.compat;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;

public abstract class ModLoadedUtil {

    // Only used in Mixins, current Fermium Booter version can't version check
    public static final String BLOODMOON_MODID = "bloodmoon";
    public static final String CLAIMIT_MODID = "claimitapi";
    public static final String COLLISIONDAMAGE_MODID = "collisiondamage";
    public static final String DDD_MODID = "distinctdamagedescriptions";
    public static final String ICEANDFIRE_MODID = "iceandfire";
    public static final String INCONTROL_MODID = "incontrol";
    public static final String REACHFIX_MODID = "reachfix";
    public static final String SHIELDBREAK_MODID = "shieldbreak";
    public static final String SRP_MODID = "srparasites";
    public static final String SWITCHBOW_MODID = "switchbow";

    // Mixins and LoadedContainer
    public static final String BAUBLES_MODID = "baubles";
    public static final String POTIONCORE_MODID = "potioncore";
    public static final String QUALITYTOOLS_MODID = "qualitytools";
    public static final String RLCOMBAT_MODID = "bettercombatmod";
    public static final String RLTWEAKER_MODID = "rltweaker";
    public static final String SME_MODID = "somanyenchantments";

    public static LoadedContainer baubles = new LoadedContainer(BAUBLES_MODID);
    public static LoadedContainer potionCore = new LoadedContainer(POTIONCORE_MODID);
    public static LoadedContainer qualityTools = new LoadedContainer(QUALITYTOOLS_MODID);
    public static LoadedContainer rlCombat = new LoadedContainer(RLCOMBAT_MODID);
    public static LoadedContainer rltweaker = new LoadedContainer(RLTWEAKER_MODID);
    public static LoadedContainer sme = new LoadedContainer(SME_MODID);

    // Nischhelm style
    public static boolean versionInRange(LoadedContainer container, String version) {
        if (!container.isLoaded()) return false;
        VersionRange range;
        try {
            range = VersionRange.createFromVersionSpec(version);
        } catch (Exception e) {
            return false;
        }
        return range.containsVersion(container.getVersion());
    }

    public static class LoadedContainer{
        private Boolean isLoaded = null;
        private DefaultArtifactVersion version;
        private final String key;
        private LoadedContainer(String key){
            this.key = key;
        }
        public boolean isLoaded(){
            if(this.isLoaded == null) isLoaded = Loader.isModLoaded(key);
            return isLoaded;
        }
        public DefaultArtifactVersion getVersion(){
            if(version == null) version = new DefaultArtifactVersion(Loader.instance().getIndexedModList().get(key).getVersion());
            return version;
        }
    }
}
