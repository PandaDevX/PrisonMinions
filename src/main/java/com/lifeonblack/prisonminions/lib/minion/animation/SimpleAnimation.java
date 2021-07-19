package com.lifeonblack.prisonminions.lib.minion.animation;

import com.lifeonblack.prisonminions.lib.minion.Minion;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class SimpleAnimation extends BukkitRunnable {

    private final Minion m;
    private boolean goingDown = false;
    private final long finishTime;
    private final Animation animation;
    public SimpleAnimation(Minion minion, Animation animation) {
        this.m = minion;
        this.m.getArmorStand().setRightArmPose(new EulerAngle(4, 0, 0));
        this.finishTime = System.currentTimeMillis() + 4000;
        animation.before();
        this.animation = animation;
    }

    @Override
    public void run() {
        if(finishTime <= System.currentTimeMillis()) {
            animation.after();
            cancel();
        }
        if(animation instanceof PreciseAnimation) {
            PreciseAnimation preciseAnimation = (PreciseAnimation) animation;
            preciseAnimation.between();
        }
        if (m.getArmorStand().getRightArmPose().getX() >= 5.2) {
            goingDown = false;
        }
        if (m.getArmorStand().getRightArmPose().getX() <= 4.7) {
            goingDown = true;
        }
        if(goingDown) {
            m.getArmorStand().setRightArmPose(m.getArmorStand().getRightArmPose().add(0.05, 0, 0));
        } else {
            m.getArmorStand().setRightArmPose(m.getArmorStand().getRightArmPose().subtract(0.05, 0, 0));
        }

    }
}
