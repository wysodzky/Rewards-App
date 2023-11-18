package com.example.rewards;

import com.example.rewards.service.RewardCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RewardCalculatorTest {

    private static RewardCalculator rewardCalculator;


    @BeforeAll
    static void init() {
        rewardCalculator = new RewardCalculator();
    }


    @Test
    void testOverOneHundredTwentyDollarsTransaction(){
        int result = rewardCalculator.calculateRewardPoints(120);
        Assertions.assertEquals(90, result);
    }

    @Test
    void testOneHundredDollarsTransaction() {
        int result = rewardCalculator.calculateRewardPoints(100);
        Assertions.assertEquals(50, result);
    }

    @Test
    void testFiftyDollarsTransaction() {
        int result = rewardCalculator.calculateRewardPoints(50);
        Assertions.assertEquals(0, result);
    }

    @Test
    void testThreeHundredDollarsTransaction(){
        int result = rewardCalculator.calculateRewardPoints(300);
        Assertions.assertEquals(450, result);
    }

    @Test
    void testOneHundredSeventyFiveDollarsTransaction() {
        int result = rewardCalculator.calculateRewardPoints(175);
        Assertions.assertEquals(200, result);
    }

    @Test
    void testSeventyFiveDollarsTransaction(){
        int result = rewardCalculator.calculateRewardPoints(75);
        Assertions.assertEquals(25, result);
    }
}
