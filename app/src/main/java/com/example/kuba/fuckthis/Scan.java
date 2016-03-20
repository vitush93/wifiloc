package com.example.kuba.fuckthis;

public class Scan {
    private String bssid;
    private int signalLoss;
    private int freq;

    public Scan(String bssid, int signalLoss, int freq) {
        this.bssid = bssid;
        this.signalLoss = signalLoss;
        this.freq = freq;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getSignalLoss() {
        return signalLoss;
    }

    public void setSignalLoss(int signalLoss) {
        this.signalLoss = signalLoss;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

}