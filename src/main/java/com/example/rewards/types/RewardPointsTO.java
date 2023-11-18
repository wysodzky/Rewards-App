package com.example.rewards.types;

public class RewardPointsTO {
    private int currentMonthPoints;
    private int lastMonthPoints;
    private int secondLastMonthPoints;

    private int total;

    public int getCurrentMonthPoints() {
        return currentMonthPoints;
    }

    public void setCurrentMonthPoints(int currentMonthPoints) {
        this.currentMonthPoints = currentMonthPoints;
    }

    public int getLastMonthPoints() {
        return lastMonthPoints;
    }

    public void setLastMonthPoints(int lastMonthPoints) {
        this.lastMonthPoints = lastMonthPoints;
    }

    public int getSecondLastMonthPoints() {
        return secondLastMonthPoints;
    }

    public void setSecondLastMonthPoints(int secondLastMonthPoints) {
        this.secondLastMonthPoints = secondLastMonthPoints;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
