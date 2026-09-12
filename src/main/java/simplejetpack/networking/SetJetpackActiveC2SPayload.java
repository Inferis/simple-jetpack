package simplejetpack.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;
import simplejetpack.SimpleJetpack;
import simplejetpack.items.JetpackItem;

public record SetJetpackActiveC2SPayload(boolean active) implements CustomPacketPayload {
    public static Type<SetJetpackActiveC2SPayload> ID = new CustomPacketPayload.Type<>(SimpleJetpack.id("set_jetpack_active_c2s_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetJetpackActiveC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SetJetpackActiveC2SPayload::active,
            SetJetpackActiveC2SPayload::new);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            var jetpackStack = JetpackItem.getEquippedJetpack(context.player());
            if (!jetpackStack.isEmpty()) {
                JetpackItem.setActive(jetpackStack, active());
                context.player().getAbilities().mayfly = active();
                context.player().getAbilities().flying = active();
            }
        });
    }
}
