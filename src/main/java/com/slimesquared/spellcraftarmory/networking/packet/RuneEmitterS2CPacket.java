package com.slimesquared.spellcraftarmory.networking.packet;

import com.slimesquared.spellcraftarmory.particle.ModParticles;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RuneEmitterS2CPacket {
    private final Vec3 pos;
    private final Vec3 vec;
    private final RuneSpells spell;
    public RuneEmitterS2CPacket(Vec3 pos, Vec3 vec, RuneSpells spell) {
        this.pos = pos;
        this.vec = vec;
        this.spell = spell;
    }

    public RuneEmitterS2CPacket(FriendlyByteBuf buf) {
        double x1 = buf.readDouble();
        double y1 = buf.readDouble();
        double z1 = buf.readDouble();
        this.pos = new Vec3(x1, y1, z1);

        double x2 = buf.readDouble();
        double y2 = buf.readDouble();
        double z2 = buf.readDouble();
        this.vec = new Vec3(x2, y2, z2);

        this.spell = RuneSpells.values()[buf.readInt()];
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);

        buf.writeDouble(vec.x);
        buf.writeDouble(vec.y);
        buf.writeDouble(vec.z);

        buf.writeInt(spell.ordinal());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            //Run on client, sender is null (since it is a server, not a player)
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }
            if (spell == RuneSpells.Teleport) {
                //Make sure to send 2 packets, one for player position, one for rune position
                level.addParticle(ModParticles.TELE_RUNE_EMITTER.get(), pos.x, pos.y, pos.z, vec.x, vec.y, vec.z);
            }
            if (spell == RuneSpells.Explode) {
                level.addParticle(ModParticles.EXPLODE_RUNE_EMITTER.get(), pos.x, pos.y, pos.z, vec.x, vec.y, vec.z);
            }
        });
        return true;
    }
}
