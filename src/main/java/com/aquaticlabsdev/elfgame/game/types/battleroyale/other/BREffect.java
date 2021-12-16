package com.aquaticlabsdev.elfgame.game.types.battleroyale.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

/**
 * @Author: extremesnow
 * On: 12/15/2021
 * At: 11:50
 */
public class BREffect {

    private ParticleEffect particleEffect;
    private Sound sound;


    public BREffect(ParticleEffect particleEffect, Sound sound) {
        this.particleEffect = particleEffect;
        this.sound = sound;
    }

    public void play(Location location) {
        new ParticleBuilder(particleEffect).setLocation(location.clone().add(0, 1,0)).setAmount(5).display();
        location.getWorld().playSound(location, sound, 1f, 1f);
    }


}
