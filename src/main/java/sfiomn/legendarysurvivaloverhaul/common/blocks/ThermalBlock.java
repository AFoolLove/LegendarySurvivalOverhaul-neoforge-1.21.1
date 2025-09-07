package sfiomn.legendarysurvivaloverhaul.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.hooks.client.screen.ScreenHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.AbstractThermalBlockEntity;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.CoolerBlockEntity;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.HeaterBlockEntity;

public class ThermalBlock extends BaseEntityBlock implements EntityBlock
{
	public static final MapCodec<ThermalBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ThermalTypeEnum.CODEC.fieldOf("thermalType").forGetter(block -> block.thermalType), propertiesCodec()
	).apply(instance, ThermalBlock::new));

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	private static final VoxelShape[] SHAPES = new VoxelShape[]
			{
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // DOWN
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // UP
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // NORTH
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // SOUTH
					Block.box(0.0d, 0.0d, 0.0d, 16.0d,  16.0d, 16.0d), // WEST
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // EAST
			};

	public final ThermalTypeEnum thermalType;
	
	public ThermalBlock(ThermalTypeEnum thermalType, Properties properties)
	{
		super(properties);
		this.thermalType = thermalType;
		
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, Boolean.FALSE));
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SHAPES[state.getValue(FACING).get3DDataValue()];
	}


	@Override
	protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
		if (level.isClientSide) {
			return InteractionResult.PASS;
		}
		this.interactWith(level, pos, player);
		return InteractionResult.CONSUME;
	}

	private void interactWith(Level level, BlockPos pos, Player player) {
		BlockEntity blockEntity = level.getBlockEntity(pos);

		if (blockEntity instanceof HeaterBlockEntity be && player instanceof ServerPlayer) {
//            NetworkHooks.openScreen((ServerPlayer) player, be, pos);
			player.openMenu(be, pos);
		} else if (blockEntity instanceof CoolerBlockEntity be && player instanceof ServerPlayer) {
//            NetworkHooks.openScreen((ServerPlayer) player, be, pos);
			player.openMenu(be, pos);
		} else {
			throw new IllegalStateException("Tile entity container is missing!");
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof AbstractThermalBlockEntity) {
				if (level instanceof ServerLevel)
					Containers.dropContents(level, pos, ((AbstractThermalBlockEntity) blockEntity));

				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		if (this.thermalType == ThermalTypeEnum.COOLING) {
			return new CoolerBlockEntity(blockPos, blockState);
		} else {
			return new HeaterBlockEntity(blockPos, blockState);
		}
	}
}
