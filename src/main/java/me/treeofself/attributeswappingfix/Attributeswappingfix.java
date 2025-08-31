package me.treeofself.attributeswappingfix;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Attributeswappingfix implements ModInitializer {
	public static final String MOD_ID = "attribute-swapping-fix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Attribute Swapping Fix Started!");
	}
}