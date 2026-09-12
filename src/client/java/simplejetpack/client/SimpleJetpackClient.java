package simplejetpack.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import simplejetpack.client.events.SimpleJetpackClientEvents;
import simplejetpack.client.screens.RechargerScreen;
import simplejetpack.menu.SimpleJetpackMenuTypes;

public class SimpleJetpackClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(SimpleJetpackMenuTypes.RECHARGER, RechargerScreen::new);
		SimpleJetpackClientEvents.registerEvents();
		SimpleJetpackKeys.registerKeyMappings();
	}
}