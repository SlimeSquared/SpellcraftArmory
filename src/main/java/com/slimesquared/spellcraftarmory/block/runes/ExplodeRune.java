package com.slimesquared.spellcraftarmory.block.runes;

import com.slimesquared.spellcraftarmory.block.GenericRuneBlock;
import com.slimesquared.spellcraftarmory.block.entity.RuneBlockEntity;
import com.slimesquared.spellcraftarmory.networking.ModMessages;
import com.slimesquared.spellcraftarmory.networking.packet.RuneEmitterS2CPacket;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ExplodeRune extends GenericRuneBlock {
    public ExplodeRune(Properties properties) {
        super(properties);
    }

    @Override
    public void activate(Player player, BlockPos pos) {
        super.activate(player, pos);
        if (player.level.getBlockEntity(pos) instanceof RuneBlockEntity rune
                && rune.getTimer() == -1) {
            rune.setTimer(20);
            for (var otherPlayer : player.level.players()) {
                if (otherPlayer instanceof ServerPlayer) {
                    var vec = GenericRuneBlock.getConnectedDirection(rune.getBlockState()).getNormal();
                    var particlePos = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    particlePos = particlePos.add(-0.438 * vec.getX(), -0.438 * vec.getY(), -0.438 * vec.getZ());
                    ModMessages.sendToPlayer(new RuneEmitterS2CPacket(particlePos, new Vec3(vec.getX(), vec.getY(), vec.getZ()), RuneSpells.Explode), (ServerPlayer) otherPlayer);
                }
            }
        }
    }

    @Override
    public void timerActivate(Player player, BlockPos pos) {
        super.timerActivate(player, pos);
        if (player.level.getBlockEntity(pos) instanceof RuneBlockEntity) {
            player.level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 2, Explosion.BlockInteraction.NONE);
            player.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }
}
