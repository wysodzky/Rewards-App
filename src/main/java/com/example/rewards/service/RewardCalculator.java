package com.example.rewards.service;

import org.springframework.stereotype.Component;

import static com.example.rewards.common.CommonValues.ONE_POINT_LIMIT_DOLLAR_VALUE;
import static com.example.rewards.common.CommonValues.TWO_POINTS_LIMIT_DOLLAR_VALUE;

@Component
public class RewardCalculator {
    public int calculateRewardPoints(int transactionValue) {

        if (transactionValue > TWO_POINTS_LIMIT_DOLLAR_VALUE) {
            return (transactionValue - TWO_POINTS_LIMIT_DOLLAR_VALUE) * 2 + 50;
        }

        if (transactionValue > ONE_POINT_LIMIT_DOLLAR_VALUE) {
            return transactionValue - ONE_POINT_LIMIT_DOLLAR_VALUE;
        }

        return 0;
    }
}
