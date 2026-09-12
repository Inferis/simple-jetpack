package simplejetpack.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import simplejetpack.SimpleJetpack;
import simplejetpack.items.JetpackItem;

public record SetJetpackFuelC2SPayload(int fuel) implements CustomPacketPayload {
    public static Type<SetJetpackFuelC2SPayload> ID = new CustomPacketPayload.Type<>(SimpleJetpack.id("set_jetpack_damage_c2s_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetJetpackFuelC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetJetpackFuelC2SPayload::fuel,
            SetJetpackFuelC2SPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            var jetpackStack = JetpackItem.getEquippedJetpack(context.player());
            if (!jetpackStack.isEmpty()) {
                jetpackStack.set(JetpackItem.FUEL, fuel());
            }
        });
    }
}
