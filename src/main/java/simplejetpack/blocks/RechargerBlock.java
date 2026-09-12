package simplejetpack.blocks;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import simplejetpack.items.SimpleJetpackItems;
import simplejetpack.networking.OpenRechargerC2SPayload;

public class RechargerBlock extends Block implements EntityBlock, BlockEntityTicker<RechargerBlockEntity> {
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape BIG_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    public static BooleanProperty HAS_JETPACK = BooleanProperty.create("has_jetpack");

    public RechargerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(HAS_JETPACK, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_JETPACK);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new RechargerBlockEntity(worldPosition, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof RechargerBlockEntity rechargerBlockEntity) {
            player.openMenu(rechargerBlockEntity);
            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.FAIL;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof RechargerBlockEntity rechargerBlockEntity) {
            if (itemStack.is(SimpleJetpackItems.JETPACK)) {
                rechargerBlockEntity.swapItem(player.getInventory(), itemStack);
            }
            else {
                player.openMenu(rechargerBlockEntity);
            }
            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (world.getBlockEntity(pos) instanceof RechargerBlockEntity rechargerBlockEntity) {
            Containers.dropContents(world, pos, rechargerBlockEntity.container());
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        if (type == SimpleJetpackBlockEntityTypes.RECHARGER) {
            return (BlockEntityTicker<T>)this;
        }
        else {
            return null;
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, RechargerBlockEntity entity) {
        entity.tick(level, pos, state);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HAS_JETPACK) ? BIG_SHAPE : SHAPE;
    }
}
