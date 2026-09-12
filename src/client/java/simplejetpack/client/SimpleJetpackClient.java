package simplejetpack.client;

import net.fabricmc.api.ClientModInitializer;
import simplejetpack.client.events.SimpleJetpackClientEvents;

public class SimpleJetpackClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SimpleJetpackClientEvents.registerEvents();
		SimpleJetpackKeys.registerKeyMappings();
	}
}