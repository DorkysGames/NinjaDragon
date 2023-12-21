package com.mygdx.NinjaDragon.Tools;


import com.badlogic.gdx.Preferences;

/**
 * Created by Brian on 1/2/2017.
 */

public class AchievementHandler {

    private int achievementCompleted = 0;
    private int multipleAchievementChecker = 0;

    public int checkForCompletedAchievements(Preferences prefs, int levelCompleted, boolean noPowerups, int infiniteModeScore) {
        // achievement for completing level 1
        if(levelCompleted == 1 && !prefs.getBoolean("Achievement1", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 1);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 1);
            achievementProperties(prefs, 1);
        }
        // achievement for completing level 10
        if(levelCompleted == 10 && !prefs.getBoolean("Achievement2", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 2);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 2);
            achievementProperties(prefs, 2);
        }
        // achievement for completing level 20
        if(levelCompleted == 20 && !prefs.getBoolean("Achievement3", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 3);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 3);
            achievementProperties(prefs, 3);
        }
        // achievement for completing level 20 without powerups
        if(levelCompleted == 20 && noPowerups && !prefs.getBoolean("Achievement4", false)){
            // use achievement property for level 21 unlocking
            achievementProperties(prefs, 4);
        }
        // achievement for completing secret level 25
        if(levelCompleted == 25 && !prefs.getBoolean("Achievement5", false)){
            prefs.putBoolean("hyperModeUnlocked", true);
            achievementProperties(prefs, 5);
        }
        // achievement for scoring 50 points in infinite mode
        if(infiniteModeScore >= 50 && !prefs.getBoolean("Achievement6", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 1);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 1);
            achievementProperties(prefs, 6);
        }
        // achievement for scoring 100 points in infinite mode
        if(infiniteModeScore >= 100 && !prefs.getBoolean("Achievement7", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 2);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 2);
            achievementProperties(prefs, 7);
        }
        // achievement for scoring 250 points in infinite mode
        if(infiniteModeScore >= 250 && !prefs.getBoolean("Achievement8", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 3);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 3);
            achievementProperties(prefs, 8);
        }
        // achievement for scoring 500 points in infinite mode
        if(infiniteModeScore >= 500 && !prefs.getBoolean("Achievement9", false)){
            // use achievement property for level 25 unlocking
            achievementProperties(prefs, 9);
        }
        // achievement for using 10 shields
        if (prefs.getInteger("shieldsUsed", 0) >= 10 && !prefs.getBoolean("Achievement10", false)) {
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 1);
            achievementProperties(prefs, 10);
        }
        // achievement for using 25 shields
        if(prefs.getInteger("shieldsUsed", 0) >= 25 && !prefs.getBoolean("Achievement11", false)){
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 3);
            achievementProperties(prefs, 11);
        }
        // achievement for using 50 shields
        if(prefs.getInteger("shieldsUsed", 0) >= 50 && !prefs.getBoolean("Achievement12", false)){
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 5);
            achievementProperties(prefs, 12);
        }
        // achievement for using 10 invincibility
        if(prefs.getInteger("invincibilityUsed", 0) >= 10 && !prefs.getBoolean("Achievement13", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 1);
            achievementProperties(prefs, 13);
        }
        // achievement for using 25 invincibility
        if(prefs.getInteger("invincibilityUsed", 0) >= 25 && !prefs.getBoolean("Achievement14", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 3);
            achievementProperties(prefs, 14);
        }
        // achievement for using 50 invincibility
        if(prefs.getInteger("invincibilityUsed", 0) >= 50 && !prefs.getBoolean("Achievement15", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 5);
            achievementProperties(prefs, 15);
        }
        // achievement for dying 100 times
        if(prefs.getInteger("deathCount", 0) >= 100 && !prefs.getBoolean("Achievement16", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 1);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 1);
            achievementProperties(prefs, 16);
        }
        // achievement for dying 250 times
        if(prefs.getInteger("deathCount", 0) >= 250 && !prefs.getBoolean("Achievement17", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 2);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 2);
            achievementProperties(prefs, 17);
        }
        // achievement for dying 500 times
        if(prefs.getInteger("deathCount", 0) >= 500 && !prefs.getBoolean("Achievement18", false)){
            prefs.putInteger("invincibilityCount", prefs.getInteger("invincibilityCount", 0) + 3);
            prefs.putInteger("shieldCount", prefs.getInteger("shieldCount", 0) + 3);
            achievementProperties(prefs, 18);
        }
        if(multipleAchievementChecker >= 2) {
            achievementCompleted = 100;
        }
        return achievementCompleted;
    }

    private void achievementProperties (Preferences prefs, int achievementNumber) {
        prefs.putBoolean("Achievement" + achievementNumber, true);
        prefs.flush();
        achievementCompleted = achievementNumber;
        multipleAchievementChecker++;
    }
}
