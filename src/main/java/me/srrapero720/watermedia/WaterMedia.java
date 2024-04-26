package me.srrapero720.watermedia;

import me.srrapero720.watermedia.api.WaterMediaAPI;
import me.srrapero720.watermedia.core.tools.DataTool;
import me.srrapero720.watermedia.core.tools.JarTool;
import me.srrapero720.watermedia.loaders.ILoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

public class WaterMedia {
	public static final String ID = "watermedia";
	public static final String NAME = "WATERMeDIA";
	public static final Logger LOGGER = LogManager.getLogger(ID);
	public static final Marker IT = MarkerManager.getMarker("Bootstrap");
	private static final List<ClassLoader> CLASS_LOADERS = new ArrayList<>();

	private static final String NO_BOOT_NAME = "watermedia.disableBoot";
	private static final boolean NO_BOOT = Boolean.parseBoolean(System.getProperty(NO_BOOT_NAME));
	private static ILoader bootstrap;
	private static WaterMedia instance;

	private WaterMedia() {}

	public static WaterMedia prepare(ILoader boot) {
		if (boot == null) throw new NullPointerException("Bootstrap is null");
		if (instance != null) throw new NullPointerException("WaterMedia is already prepared");
		LOGGER.info(IT, "Preparing '{}' on '{}'", NAME, boot.name());
		LOGGER.info(IT, "WaterMedia version '{}'", JarTool.readString("/watermedia/version.cfg"));

		WaterMedia.bootstrap = boot;
		return instance = new WaterMedia();
	}

	public static void attachClassLoader(Class<?> classFrom, ClassLoader classLoader) {
		CLASS_LOADERS.add(classLoader);
		LOGGER.info(IT, "Attached new search class loader from {}", classFrom.getName());
	}

	public void start() throws Exception {
		if (NO_BOOT) {
			LOGGER.error(IT, "Refusing to bootstrap WATERMeDIA, detected D{}=true", NO_BOOT_NAME);
			return;
		}

		List<WaterMediaAPI> modules = DataTool.toList(ServiceLoader.load(WaterMediaAPI.class));
		modules.sort(Comparator.comparingInt(e -> e.priority().ordinal()));

		for (WaterMediaAPI m: modules) {
			LOGGER.info(IT, "Starting {}", m.getClass().getSimpleName());
			if (!m.prepare(bootstrap)) {
				LOGGER.warn(IT, "Module {} refuses to be loaded, skipping", m.getClass().getSimpleName());
				continue;
			}
			m.start(bootstrap);
			LOGGER.info(IT, "Module {} loaded successfully", m.getClass().getSimpleName());
		}
		LOGGER.info(IT, "Startup finished");
	}

	public static ILoader getLoader() { return bootstrap; }
}