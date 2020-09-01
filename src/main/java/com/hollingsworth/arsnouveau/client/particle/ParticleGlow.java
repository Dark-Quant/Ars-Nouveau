package com.hollingsworth.arsnouveau.client.particle;


import com.hollingsworth.arsnouveau.ArsNouveau;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

public class ParticleGlow extends SpriteTexturedParticle {
    public float colorR = 0;
    public float colorG = 0;
    public float colorB = 0;
    public float initScale = 0;
    public float initAlpha = 0;
    public static final String NAME = "glow";

    @ObjectHolder(ArsNouveau.MODID + ":" + ParticleGlow.NAME) public static ParticleType<ColorParticleTypeData> TYPE;

    public ParticleGlow(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime, IAnimatedSprite sprite) {
        super(worldIn, x,y,z,0,0,0);
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0){
            this.colorR = this.colorR/255.0f;
        }
        if (this.colorG > 1.0){
            this.colorG = this.colorG/255.0f;
        }
        if (this.colorB > 1.0){
            this.colorB = this.colorB/255.0f;
        }
        this.setColor(colorR, colorG, colorB);
        this.maxAge = (int)((float)lifetime*0.5f);
        this.particleScale = scale/8;
        this.initScale = scale;
        this.motionX = vx*2.0f;
        this.motionY = vy*2.0f;
        this.motionZ = vz*2.0f;
        this.initAlpha = a;
//        this.particleAngle = 2.0f*(float)Math.PI;
        this.selectSpriteRandomly(sprite);
//        this.setParticleTexture(sprite);
//        this.set
    }
    @Override
    public IParticleRenderType getRenderType() {
        return ModParticles.EMBER_RENDER;
    }


    @Override
    public int getBrightnessForRender(float pTicks){
        return 255;
    }


    @Override
    public void tick(){
        super.tick();

        if (new Random().nextInt(6) == 0){
            this.age++;
        }
        float lifeCoeff = (float)this.age/(float)this.maxAge;
        this.particleScale = initScale-initScale*lifeCoeff;
        this.particleAlpha = initAlpha*(1.0f-lifeCoeff);
        this.prevParticleAngle = particleAngle;
        particleAngle += 1.0f;
    }



    @Override
    public boolean isAlive() {
        return this.age < this.maxAge;
    }

    public static IParticleData createData(ParticleColor color) {
        return new ColorParticleTypeData(TYPE, color);
    }

    @OnlyIn(Dist.CLIENT)
    static class Factory implements IParticleFactory<ColorParticleTypeData> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(ColorParticleTypeData data, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
//            return new ParticleGlow(worldIn, x,y,z,xSpeed, ySpeed, zSpeed, worldIn.rand.nextInt(255), worldIn.rand.nextInt(255), worldIn.rand.nextInt(255), 1.0f, .25f, 36, this.spriteSet);
            return new ParticleGlow(worldIn, x,y,z,xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(), 1.0f, .25f, 36, this.spriteSet);

        }
    }
}