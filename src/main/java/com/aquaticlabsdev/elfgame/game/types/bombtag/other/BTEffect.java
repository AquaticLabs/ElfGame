package com.aquaticlabsdev.elfgame.game.types.bombtag.other;

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
public class BTEffect {

    private ParticleEffect particleEffect;
    private Sound sound;
    private float volume = 1f;

    public BTEffect(ParticleEffect particleEffect, Sound sound) {
        this.particleEffect = particleEffect;
        this.sound = sound;
    }
    public BTEffect volume(float volume) {
        this.volume = volume;
        return this;
    }

    public void play(Location location) {
        if (particleEffect != null) {
            new ParticleBuilder(particleEffect).setLocation(location.clone().add(0, 1,0)).setAmount(5).display();
        }
        if (sound != null) {
            location.getWorld().playSound(location, sound, volume, 1f);
        }
    }


}
