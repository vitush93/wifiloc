package com.example.kuba.fuckthis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by Kuba on 19.03.2016.
 */
public class ApRatioGuess {

    ArrayList<Scan> all;
    ArrayList<Scan> allAverage;

    HashMap<String, ArrayList<Scan>> map = new HashMap<>();
    HashMap<String, Double> avgs = new HashMap<>();

    public ApRatioGuess() {
        ArrayList<Scan> all = new ArrayList<>();
    }


    public void addAll(ArrayList<Scan> scanArrayList) {

        all.addAll(scanArrayList);
    }

    public void getRatio() {
        //TODO pomer dvou nej signalu
    }

    public void avg() {
        for (Scan s : all) {
            if (map.containsKey(s.getBssid())) {
                map.get(s.getBssid()).add(s);
            } else {
                map.put(s.getBssid(), new ArrayList<Scan>());
                map.get(s.getBssid()).add(s);
            }
        }

        for (String mac : map.keySet()) {
            int sum = 0;
            for (Scan s : map.get(mac)) {
                sum += s.getSignalLoss();
            }

            avgs.put(mac, (double) sum / map.get(mac).size());
        }

        // TODO remove
        for (String mac : avgs.keySet()) {
            System.out.println(mac + " avg: " + avgs.get(mac));
        }
    }

    public void mid() {
        Hashtable<Scan, Integer> hTable = new Hashtable<>();
        for (int i = 0; i < all.size(); i++) {
            Scan currentScan = all.get(i);
            if (hTable.containsKey(currentScan)) {
                int newValue = hTable.get(currentScan) + currentScan.getSignalLoss();
                hTable.put(currentScan, newValue); //gathering  signalLoss
            } else {
                hTable.put(currentScan, currentScan.getSignalLoss());
            }
        }

        // Values are now gathered
        Set<Scan> keys = hTable.keySet();
        for (Scan key : keys) {
            System.out.println("Value of " + key + " is: " + hTable.get(key));
        }


    }
}
