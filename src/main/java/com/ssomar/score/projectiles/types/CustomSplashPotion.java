package com.ssomar.score.projectiles.types;

import com.ssomar.score.SCore;
import com.ssomar.score.projectiles.features.*;
import com.ssomar.score.projectiles.features.ParticlesFeature;

import java.io.File;

public class CustomSplashPotion extends SProjectiles {


    public CustomSplashPotion(String id, File file) {
        super(id, file);
    }

    public CustomSplashPotion(String id, File file, boolean showError) {
        super(id, file, showError);
    }

    @Override
    public CustomProjectile setup(CustomProjectile proj) {
        proj = new CustomNameFeature(proj);
        proj = new InvisibleFeature(proj);
        proj = new GlowingFeature(proj);
        proj = new BounceFeature(proj);
        proj = new GravityFeature(proj);
        proj = new DespawnFeature(proj);
        proj = new VelocityFeature(proj);
        proj = new SilentFeature(proj);
        proj = new ColorFeature(proj);
        /* Particle feature not available in 1.11 */
        if(!SCore.is1v11Less())
            proj = new ParticlesFeature(proj);
        proj = new PotionEffectsFeature(proj);
        if(!SCore.is1v13Less())
            proj = new RemoveWhenHitBlockFeature(proj);
        return proj;
    }
}
