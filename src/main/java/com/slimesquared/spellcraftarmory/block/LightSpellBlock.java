package com.slimesquared.spellcraftarmory.block;

import com.slimesquared.spellcraftarmory.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class LightSpellBlock extends Block {
    public LightSpellBlock(Properties properties) {
        super(properties);
    }

    //xyz start point, xyz end point. Out of 16 pixels across block. Add to 16 to make centered.
    protected static final VoxelShape AABB = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        return AABB;
    }

    @Override
    public void animateTick(@NotNull BlockState state, Level level, BlockPos pos, @NotNull RandomSource rand) {
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.5D;
        double d2 = (double)pos.getZ() + 0.5D;
        //particle, xyz pos, xyz velocity
        level.addParticle(ParticleTypes.INSTANT_EFFECT, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        level.addParticle(ModParticles.LIGHT_PARTICLES.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}
