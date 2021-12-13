package com.aquaticlabsdev.elfgame.scoreboard.valuetypes;

import java.util.List;

/**
 * @Author: extremesnow
 * On: 10/13/2021
 * At: 17:18
 */
public class AnimatedTitle extends Animation {

    public AnimatedTitle(String animationName, List<String> animationValues) {
        setAnimationName(animationName);
        setAnimationValues(animationValues);
        setAnimationSize(animationValues.size());
    }

}
