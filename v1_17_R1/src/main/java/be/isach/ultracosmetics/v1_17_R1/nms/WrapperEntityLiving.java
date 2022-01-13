package be.isach.ultracosmetics.v1_17_R1.nms;

import be.isach.ultracosmetics.v1_17_R1.ObfuscatedFields;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author RadBuilder
 */
public class WrapperEntityLiving extends WrapperEntity {

    protected LivingEntity handle;

    public WrapperEntityLiving(LivingEntity handle) {
        super(handle);

        this.handle = handle;
    }

    public float getRotationYawHead() {
        return handle.yHeadRot;
    }

    public void setRotationYawHead(float rotationYawHead) {
        handle.yHeadRot = rotationYawHead;
    }

    public float getRenderYawOffset() {
        return handle.yBodyRot;
    }

    public void setRenderYawOffset(float renderYawOffset) {
        handle.yBodyRot = renderYawOffset;
    }

    public float getMoveStrafing() {
        return handle.xxa;
    }

    public void setMoveStrafing(float moveStrafing) {
        handle.xxa = moveStrafing;
    }

    public float getMoveForward() {
        return handle.zza;
    }

    public void setMoveForward(float moveForward) {
        handle.zza = moveForward;
    }

    public boolean isJumping() {
        return getField(ObfuscatedFields.JUMPING, LivingEntity.class, Boolean.class);
    }

    public void setJumping(boolean jumping) {
    	// not sure why there's a setJumping method but no isJumping method
        handle.setJumping(jumping);
    }

    public float getJumpMovementFactor() {
        return handle.yBodyRotO;
    }

    public void setJumpMovementFactor(float jumpMovementFactor) {
        handle.yBodyRotO = jumpMovementFactor;
    }

    public float getPrevLimbSwingAmount() {
        return handle.animationSpeedOld;
    }

    public void setPrevLimbSwingAmount(float prevLimbSwingAmount) {
        handle.animationSpeedOld = prevLimbSwingAmount;
    }

    public float getLimbSwingAmount() {
        return handle.animationSpeed;
    }

    public void setLimbSwingAmount(float limbSwingAmount) {
        handle.animationSpeed = limbSwingAmount;
    }

    public float getLimbSwing() {
        return handle.animationPosition;
    }

    public void setLimbSwing(float limbSwing) {
        handle.animationPosition = limbSwing;
    }

    public float getMoveSpeed() {
        return handle.getSpeed();
    }

    public void setMoveSpeed(float moveSpeed) {
        handle.setSpeed(moveSpeed);
    }

    @Override
    public LivingEntity getHandle() {
        return handle;
    }
}