package com.aquaticlabsdev.elfgame.scoreboard.valuetypes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: extremesnow
 * On: 10/15/2021
 * At: 22:15
 */
public abstract class Animation {

    @Getter @Setter
    public String animationName;
    @Getter @Setter
    public List<String> animationValues;
    @Getter @Setter
    private int animationSize;
    @Getter @Setter
    private int animationSpeed;
    @Getter
    private int currentValue = 0;

    public String getCurrentFrame() {
        return getAnimationValues().get(currentValue);
    }

    public String getNextFrame() {
        addFrame();
        return getAnimationValues().get(currentValue);
    }

    public String getPreviousFrame() {
        removeFrame();
        return getAnimationValues().get(currentValue);
    }

    private void addFrame() {
        if (currentValue < animationSize-1) {
            currentValue++;
        } else {
            currentValue = 0;
        }
    }
    private void removeFrame() {
        if (currentValue > 0) {
            currentValue--;
        } else {
            currentValue = animationSize-1;
        }
    }

}
