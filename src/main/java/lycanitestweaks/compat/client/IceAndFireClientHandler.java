package lycanitestweaks.compat.client;

import com.github.alexthe666.iceandfire.IceAndFire;
import lycanitestweaks.compat.IceAndFireHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class IceAndFireClientHandler {

    private static Boolean underscoreZero = null;

    private static boolean checkUnderscoreZero() {
        if(underscoreZero == null) {
            if(IceAndFireHandler.isROTN()) underscoreZero = ModLoadedUtil.versionInRange(ModLoadedUtil.iceandfire, "(,1.9.1-1.3.3]");
            else if(IceAndFireHandler.isRLCraft()) underscoreZero = false;
            else underscoreZero = ModLoadedUtil.versionInRange(ModLoadedUtil.iceandfire, "[1.8.0,)");
        }
        return underscoreZero;
    }

    public static String getModdedLore(GenericEntityInfo entityInfo) {
        String entityName = "";
        switch (entityInfo.getEntityEntryName()) {
            case "amphithere": entityName = "amphithere"; break;
            case "if_cockatrice": entityName = "cockatrice"; break;
            case "cyclops": entityName = "cyclops"; break;
            case "deathworm": entityName = "deathworm"; break;
            case "firedragon": entityName = "firedragon"; break;
            case "gorgon": entityName = "gorgon"; break;
            case "hippocampus": entityName = "hippocampus"; break;
            case "hippogryph": entityName = "hippogryph"; break;
            case "if_hydra": entityName = "hydra"; break;
            case "icedragon": entityName = "icedragon"; break;
            case "lightningdragon": entityName = "lightningdragon"; break;
            case "if_pixie": entityName = "pixie"; break;
            case "seaserpent": entityName = "seaserpent"; break;
            case "siren": entityName = "siren"; break;
            case "stymphalianbird": entityName = "stymphalianbird"; break;
            case "if_troll": entityName = "troll"; break;
            case "snowvillager": entityName = "villagers"; break;
        }
        if(entityName.isEmpty()) {
            if(entityInfo.getEntityEntryName().contains("dread_")) entityName = "dread_mobs";
            else if(entityInfo.getEntityEntryName().contains("myrmex_")) entityName = "myrmex";
        }

        if(entityName.isEmpty()) return "";

        StringBuilder bestiaryText = new StringBuilder();
        String currentLanguage = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getJavaLocale().toString();
        if(checkUnderscoreZero()) currentLanguage = Minecraft.getMinecraft().gameSettings.language + "_0";
        String filePath = "assets/iceandfire/lang/bestiary/" + currentLanguage + "/";

        for(int i = 0; i < 6; i++) {
            String fileName = entityName + "_" + i + ".txt";
            ClassLoader classLoader = IceAndFire.class.getClassLoader();

            try (InputStream fileReader = classLoader.getResourceAsStream(filePath + fileName)) {
                if(fileReader == null) continue;

                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.contains("<") || line.contains(">")) {
                            continue;
                        }
                        line = line.trim();
                        line = line.replaceAll("§.", "");
                        if(line.isEmpty()) continue;

                        bestiaryText.append(line).append(" ");
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return bestiaryText.toString();
    }
}
