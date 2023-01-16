package com.slimesquared.spellcraftarmory.block;

import com.slimesquared.spellcraftarmory.screen.SewingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;

public class SewingTableBlock extends CraftingTableBlock {
    private static final Component CONTAINER_TITLE = Component.literal("Sewing");

    public SewingTableBlock(Properties properties) {
        super(properties);
    }

    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) -> {
            return new SewingTableMenu(id, inventory, ContainerLevelAccess.create(level, pos));
        }, CONTAINER_TITLE);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    private static final VoxelShape LEG1 = Block.box(0, 0, 0, 4, 3, 4);
    private static final VoxelShape LEG2 = Block.box(0, 0, 12, 4, 3, 16);
    private static final VoxelShape LEG3 = Block.box(12, 0, 0, 16, 3, 4);
    private static final VoxelShape LEG4 = Block.box(12, 0, 12, 16, 3, 16);
    private static final VoxelShape TOP = Block.box(0, 3, 0, 16, 16, 16);
    private static final VoxelShape SHAPE = Shapes.or(LEG1, LEG2, LEG3, LEG4, TOP);

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState p_60547_, BlockGetter p_60548_, BlockPos p_60549_) {
        return SHAPE;
    }
}
