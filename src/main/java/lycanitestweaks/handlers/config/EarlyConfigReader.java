package lycanitestweaks.handlers.config;

import lycanitestweaks.LycanitesTweaks;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Copied from SRPMixins by Nischhelm which was inspired by fonnymunkey RLMixins
public class EarlyConfigReader {
	private static File configFile = null;
	private static String configBooleanString = null;
	private static String configDoubleString = null;

	public static boolean getBoolean(String name, boolean defaultValue) {
		if (configFile == null) configFile = new File("config", LycanitesTweaks.MODID + ".cfg");

		if (configBooleanString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					//All lines starting with "B:"
					configBooleanString = stream.filter(s -> s.trim().startsWith("B:")).collect(Collectors.joining());
				} catch (Exception e) {
                    LycanitesTweaks.LOGGER.error("Failed to parse " + LycanitesTweaks.NAME + " boolean config: {}", e.toString());
				}
			} else configBooleanString = "";
		}

		if (configBooleanString.contains("B:\"" + name + "\"="))
			return configBooleanString.contains("B:\"" + name + "\"=true");
		//If config is not generated yet or missing entries, we use the default value that would be written into it
		else return defaultValue;
	}

	public static double getDouble(String name, float defaultValue) {
		return getDouble(name, (double) defaultValue);
	}

	public static double getDouble(String name, double defaultValue) {
		if (configFile == null) configFile = new File("config", LycanitesTweaks.MODID + ".cfg");

		if (configDoubleString == null) {
			if (configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					configDoubleString = stream.filter(s -> s.trim().startsWith("D:")).collect(Collectors.joining());
				} catch (Exception e) {
					LycanitesTweaks.LOGGER.error("Failed to parse " + LycanitesTweaks.NAME + " double config: {}", e.toString());
				}
			} else configDoubleString = "";
		}

		if (configDoubleString.contains("D:\"" + name + "\"=")) {
			int index = configDoubleString.indexOf("D:\"" + name + "\"=");
			try {
				int startindex = configDoubleString.indexOf("=", index)+1;
				int endindex = configDoubleString.indexOf("D\\:", startindex);
				return Double.parseDouble(configDoubleString.substring(startindex, endindex == -1 ? configDoubleString.length() : endindex).trim());
			} catch (Exception e) {
				LycanitesTweaks.LOGGER.error(LycanitesTweaks.NAME + ": Failed to parse double config {}, {}", name, e);
				return 0;
			}
		}
		//If config is not generated yet or missing entries, we use the default value that will get written into it right after this
		else return defaultValue;
	}
}