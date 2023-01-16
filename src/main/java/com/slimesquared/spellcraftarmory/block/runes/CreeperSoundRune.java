package com.slimesquared.spellcraftarmory.block.runes;

import com.slimesquared.spellcraftarmory.block.GenericRuneBlock;
import com.slimesquared.spellcraftarmory.block.entity.RuneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class CreeperSoundRune extends GenericRuneBlock {
    public CreeperSoundRune(Properties properties) {
        super(properties);
    }

    @Override
    public void activate(Player player, BlockPos pos) {
        super.activate(player, pos);
        int range = 16;
        if (player.level.getBlockEntity(pos) instanceof RuneBlockEntity) {
            var victims = player.level.getNearbyPlayers(TargetingConditions.forNonCombat().ignoreLineOfSight(), null, new AABB(pos.offset(-range, -range, -range), pos.offset(range, range, range)));
            for (var victim : victims) {
                victim.playSound(SoundEvents.CREEPER_PRIMED, 2.0F, 0.5F);
            }
        }
    }
}
