package simplejetpack.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;
import simplejetpack.SimpleJetpack;
import simplejetpack.blocks.RechargerBlockEntity;

public record OpenRechargerC2SPayload(BlockPos pos) implements CustomPacketPayload {
    public static Type<OpenRechargerC2SPayload> ID = new CustomPacketPayload.Type<>(SimpleJetpack.id("open_recharger_c2s_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenRechargerC2SPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenRechargerC2SPayload::pos,
            OpenRechargerC2SPayload::new);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            var player = context.player();
            if (player.level().getBlockEntity(pos()) instanceof RechargerBlockEntity rechargerBlockEntity) {
                player.openMenu(rechargerBlockEntity);
            }
        });
    }
}
