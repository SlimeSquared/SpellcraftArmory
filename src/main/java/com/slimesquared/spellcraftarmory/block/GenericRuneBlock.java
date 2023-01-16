package com.slimesquared.spellcraftarmory.block;

import com.slimesquared.spellcraftarmory.block.entity.ModBlockEntities;
import com.slimesquared.spellcraftarmory.block.entity.RuneBlockEntity;
import com.slimesquared.spellcraftarmory.item.ModItems;
import com.slimesquared.spellcraftarmory.runecapability.PlayerRunes;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GenericRuneBlock extends BaseEntityBlock {
    static final double border = 2.0;
    static final double thickness = 1.0;
    //xyz start, xyz end
    protected static final VoxelShape NORTH_AABB = Block.box(border, border, 16-thickness, 16-border, 16-border, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(border, border, 0.0D, 16-border, 16-border, thickness);
    protected static final VoxelShape WEST_AABB = Block.box(16-thickness, border, border, 16.0D, 16-border, 16-border);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, border, border, thickness, 16-border, 16-border);
    protected static final VoxelShape UP_AABB = Block.box(border, 0.0D, border, 16-border, thickness, 16-border);
    protected static final VoxelShape DOWN_AABB = Block.box(border, 16-thickness, border, 16-border, 16.0D, 16-border);
    public GenericRuneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_54665_, @NotNull BlockGetter p_54666_, @NotNull BlockPos p_54667_, @NotNull CollisionContext p_54668_) {
        return switch (p_54665_.getValue(FACE)) {
            case FLOOR -> UP_AABB;
            case WALL -> switch (p_54665_.getValue(FACING)) {
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
                case SOUTH -> SOUTH_AABB;
                case NORTH -> NORTH_AABB;
                default -> NORTH_AABB;
            };
            case CEILING -> DOWN_AABB;
        };
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING);
    }

    public void activate(Player player, BlockPos pos) {
        if (player.level.getBlockEntity(pos) instanceof RuneBlockEntity rune) {
            System.out.println(rune.getRuneSpell().name() + " Rune activated: " + pos + "  " + rune.getOwnerUUID());
        }
    }

    public void timerActivate(Player player, BlockPos pos) {
        if (player.level.getBlockEntity(pos) instanceof RuneBlockEntity rune) {
            System.out.println(rune.getRuneSpell().name() + " Timer activated: " + pos + "  " + rune.getOwnerUUID());
        }
    }

    public void fizzle(Player player, PlayerRunes.DimPosition dimPos) {
        if (player.level.dimension().equals(dimPos.dim)
                && player.level.getBlockEntity(dimPos.pos) instanceof RuneBlockEntity runeEntity
                && runeEntity.getOwnerUUID() == player.getUUID()) {
            unsafeFizzle(player.level, dimPos.pos, runeEntity);
        }
    }

    public void unsafeFizzle(Level level, BlockPos pos, RuneBlockEntity rune) {
        rune.setRuneSpell(RuneSpells.None);
        rune.setOwnerUUID(null);
        level.setBlockAndUpdate(pos, ModBlocks.RUNE.get().defaultBlockState()
                .setValue(GenericRuneBlock.FACING, rune.getBlockState().getValue(GenericRuneBlock.FACING))
                .setValue(GenericRuneBlock.FACE, rune.getBlockState().getValue(GenericRuneBlock.FACE)));
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.5D;
        double d2 = (double)pos.getZ() + 0.5D;
        level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0, 0, 0);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide() && player.getAbilities().mayBuild) {
            if (player.getItemInHand(hand).getItem() == ModItems.RUNE_WAND.get()) {
                this.activate(player, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public void effectsTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    }




    //BlockEntity
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RuneBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.RUNE.get(), RuneBlockEntity::tick);
    }



    //FaceAttachedHorizintalDirectionalBlock
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    public boolean canSurvive(BlockState p_53186_, LevelReader p_53187_, BlockPos p_53188_) {
        return canAttach(p_53187_, p_53188_, getConnectedDirection(p_53186_).getOpposite());
    }

    public static boolean canAttach(LevelReader p_53197_, BlockPos p_53198_, Direction p_53199_) {
        BlockPos blockpos = p_53198_.relative(p_53199_);
        return p_53197_.getBlockState(blockpos).isFaceSturdy(p_53197_, blockpos, p_53199_.getOpposite());
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_53184_) {
        for(Direction direction : p_53184_.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, p_53184_.getHorizontalDirection());
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite());
            }

            if (blockstate.canSurvive(p_53184_.getLevel(), p_53184_.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
    }

    public BlockState updateShape(BlockState p_53190_, Direction p_53191_, BlockState p_53192_, LevelAccessor p_53193_, BlockPos p_53194_, BlockPos p_53195_) {
        return getConnectedDirection(p_53190_).getOpposite() == p_53191_ && !p_53190_.canSurvive(p_53193_, p_53194_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_53190_, p_53191_, p_53192_, p_53193_, p_53194_, p_53195_);
    }

    public static Direction getConnectedDirection(BlockState p_53201_) {
        switch (p_53201_.getValue(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return p_53201_.getValue(FACING);
        }
    }


    //HorizontalDirectionalBlock
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockState rotate(BlockState p_54125_, Rotation p_54126_) {
        return p_54125_.setValue(FACING, p_54126_.rotate(p_54125_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_54122_, Mirror p_54123_) {
        return p_54122_.rotate(p_54123_.getRotation(p_54122_.getValue(FACING)));
    }
}
