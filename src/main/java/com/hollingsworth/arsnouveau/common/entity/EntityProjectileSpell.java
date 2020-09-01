package com.hollingsworth.arsnouveau.common.entity;

import com.hollingsworth.arsnouveau.client.particle.*;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.common.spell.SpellResolver;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class EntityProjectileSpell extends ArrowEntity {

    private int age;
    private SpellResolver spellResolver;
    public int xpColor;

    public int pierceLeft;

    public EntityProjectileSpell(EntityType<? extends ArrowEntity> type, World worldIn, SpellResolver spellResolver, int pierceLeft) {
        super(type, worldIn);
        this.spellResolver = spellResolver;
        age = 0;
        this.pierceLeft = pierceLeft;
    }

    public EntityProjectileSpell(final EntityType<? extends EntityProjectileSpell> entityType, final World world) {
        super(entityType, world);

    }

    public EntityProjectileSpell(final World world, final double x, final double y, final double z) {
        super(world, x, y, z);
    }

    public EntityProjectileSpell(final World world, final LivingEntity shooter) {
        super(world, shooter);
    }

    public void particles(){

        for(int i =0; i < 10; i++){
            double minRange = -0.1;
            double maxRange = 0.1;
            double d0 = getPosX() + ParticleUtil.inRange(minRange, maxRange);
            double d1 = getPosY()+ ParticleUtil.inRange(minRange, maxRange);
            double d2 = getPosZ()+ ParticleUtil.inRange(minRange, maxRange);
            world.rand.nextInt(255);
//            world.addParticle(ParticleTypes.ENCHANTED_HIT, d0, d1, d2, 0.0, 0.0, 0.0);
//            world.addParticle(ParticleSource.createData(new ParticleColor( world.rand.nextInt(255),  world.rand.nextInt(255),  world.rand.nextInt(255))), d0, d1, d2, 0.0, 0.0, 0.0);
            world.addParticle(ParticleSource.createData(new ParticleColor(204,51,255)), d0, d1, d2, 0.0, 0.0, 0.0);

        }

    }

    @Override
    public void tick() {
        age++;


        this.lastTickPosX = this.getPosX();
        this.lastTickPosY = this.getPosY();
        this.lastTickPosZ = this.getPosZ();
        //super.tick();

        if (this.inGround) {
            this.inGround = false;
            this.setMotion(this.getMotion().mul((double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F)));
        }

        AxisAlignedBB axisalignedbb = this.getBoundingBox().expand(this.getMotion()).grow(1.0D);

        for(Entity entity : this.world.getEntitiesInAABBexcluding(this, axisalignedbb, (p_213881_0_) -> {
            return !p_213881_0_.isSpectator() && p_213881_0_.canBeCollidedWith();
        })) {

        }

        RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, axisalignedbb, (p_213880_1_) -> {
            return !p_213880_1_.isSpectator() && p_213880_1_.canBeCollidedWith();
        }, RayTraceContext.BlockMode.OUTLINE, true);


        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && this.world.getBlockState(((BlockRayTraceResult)raytraceresult).getPos()).getBlock() == Blocks.NETHER_PORTAL) {
                this.setPortal(((BlockRayTraceResult)raytraceresult).getPos());
            } else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
                this.onHit(raytraceresult);
            }
        }

        Vec3d vec3d = this.getMotion();
        double x = this.getPosX() +vec3d.x;
        double y = this.getPosY() + vec3d.y;
        double z = this.getPosZ() + vec3d.getZ();

        this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));


        while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
        this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);


        if (!this.hasNoGravity()) {
            Vec3d vec3d1 = this.getMotion();
            this.setMotion(vec3d1.x, vec3d1.y , vec3d1.z);
        }

        this.setPosition(x,y,z);
        if(world.isRemote && this.age > 1) {
            for (int i = 0; i < 10; i++) {
                double minRange = -0.1;
                double maxRange = 0.1;
                double d0 = getPosX() + ParticleUtil.inRange(minRange, maxRange);
                double d1 = getPosY() + ParticleUtil.inRange(minRange, maxRange);
                double d2 = getPosZ() + ParticleUtil.inRange(minRange, maxRange);

//            world.addParticle(ParticleTypes.ENCHANT, d0, d1, d2, 0.0, 0.0, 0.0);
//                world.addParticle(ParticleSource.createData(new ParticleColor(204,51,255)), d0, d1, d2, this.getMotion().x, this.getMotion().y, this.getMotion().z);
//                world.addParticle(ParticleSource.createData(new ParticleColor(204,51,255)), prevPosX, prevPosY, prevPosZ, this.getMotion().x, this.getMotion().y, this.getMotion().z);

            }
//            double minRange = -0.1;
//            double maxRange = 0.1;
//            for (float j = 0.0F; j < 1.0F; j += 0.05F) {
//                for (int i = 0; i < 2; ++i) {
//                    world.addParticle(ParticleSource.createData(new ParticleColor(204, 51, 255)),
//                            this.prevPosX + (this.getMotion().x * j) +ParticleUtil.inRange(minRange, maxRange),
//                            this.prevPosY + (this.getMotion().y * j) +ParticleUtil.inRange(minRange, maxRange),
//                            this.prevPosZ + (this.getMotion().z * j) +ParticleUtil.inRange(minRange, maxRange),
//                            0, 0, 0);
//                }
//            }
            double deltaX = getPosX() - lastTickPosX;
            double deltaY = getPosY() - lastTickPosY;
            double deltaZ = getPosZ() - lastTickPosZ;
            double dist = Math.ceil(Math.sqrt(deltaX*deltaX+deltaY*deltaY+deltaZ*deltaZ) * 20);
            int counter = 0;

            for (double i = 0; i < dist; i ++){
                double coeff = i/dist;
                counter += world.rand.nextInt(3);
                if (counter % (Minecraft.getInstance().gameSettings.particles.getId() == 0 ? 1 : 2 * Minecraft.getInstance().gameSettings.particles.getId()) == 0) {


                    world.addParticle(ParticleGlow.createData(new ParticleColor(255, 30, 20)), (float) (prevPosX + deltaX * coeff), (float) (prevPosY + deltaY * coeff), (float) (prevPosZ + deltaZ * coeff), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f));
                }
                //                ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)(prevPosX+ deltaX *coeff), (float)(prevPosY+ deltaY *coeff), (float)(prevPosZ+ deltaZ *coeff), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 255, 64, 16, 2.0f, 12);
                //                ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)(prevPosX+ deltaX *coeff), (float)(prevPosY+ deltaY *coeff), (float)(prevPosZ+ deltaZ *coeff), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 255, 64, 16, 2.0f, 12);
            }

        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    public EntityProjectileSpell(World world, LivingEntity shooter, SpellResolver spellResolver, int maxPierce) {
        super(world, shooter);
        this.spellResolver = spellResolver;
        age = 0;
        pierceLeft = maxPierce;
    }


    /**
     * Sets throwable heading based on an entity that's throwing it
     */
    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        Vec3d vec3d = entityThrower.getMotion();
        this.setMotion(this.getMotion().add(vec3d.x, entityThrower.onGround ? 0.0D : vec3d.y, vec3d.z));
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;

    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.SPELL_PROJ;
    }

    protected void attemptRemoval(){
        this.pierceLeft--;
        if(this.pierceLeft < 0){
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        //super.onHit(result);

        if(!world.isRemote &&  result != null && result.getType() == RayTraceResult.Type.ENTITY) {
            if (((EntityRayTraceResult) result).getEntity().equals(this.getShooter())) return;
            if(this.spellResolver != null) {
                this.spellResolver.onResolveEffect(world, (LivingEntity) this.getShooter(), (EntityRayTraceResult) result);
                 attemptRemoval();
            }

        }
        if(world.isRemote && result instanceof BlockRayTraceResult){
            SpellBook.spawnParticles(result.getHitVec().x, result.getHitVec().y, result.getHitVec().z, world);
        }

        if (!world.isRemote && result instanceof BlockRayTraceResult) {
//           if(!world.getBlockState(((BlockRayTraceResult) result).getPos()).getMaterial().blocksMovement())
//               return;
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)result;
            if(this.spellResolver != null) {
                this.spellResolver.onResolveEffect(this.world, (LivingEntity) this.getShooter(), blockraytraceresult);
            }
           attemptRemoval();
        }
    }

    public EntityProjectileSpell(FMLPlayMessages.SpawnEntity packet, World world){
        super(ModEntities.SPELL_PROJ, world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        if(tag.contains("pierce")){
            this.pierceLeft = tag.getInt("pierce");
        }
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putInt("pierce", this.pierceLeft);
    }
}
