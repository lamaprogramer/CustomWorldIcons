package net.iamaprogrammer.customworldicons;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CustomWorldIcons implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("customworldicons");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loaded Reimagined World Menus");
	}
}
