package simplejetpack;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplejetpack.blocks.SimpleJetpackBlockEntityTypes;
import simplejetpack.blocks.SimpleJetpackBlocks;
import simplejetpack.items.SimpleJetpackItems;
import simplejetpack.networking.SimpleJetpackNetworking;

public class SimpleJetpack implements ModInitializer {
	public static final String MOD_ID = "simplejetpack";
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SimpleJetpackBlocks.registerBlocks();
		SimpleJetpackBlockEntityTypes.registerBlockEntityTypes();
		SimpleJetpackItems.registerItems();
		SimpleJetpackNetworking.registerPayloads();
	}
}
