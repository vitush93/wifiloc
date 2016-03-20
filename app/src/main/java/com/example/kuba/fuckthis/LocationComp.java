package com.example.kuba.fuckthis;

import java.util.Collection;
import java.util.HashMap;

public class LocationComp {

    public static final HashMap<String, Location> apLoc = new HashMap<>();

    static {
        apLoc.put("84:24:8D:C6:C2:30", new Location(0.0, 0.0, 0.0));
        apLoc.put("84:24:8D:C6:EB:A0", new Location(2.75, 7.2, 0.0));
        apLoc.put("84:24:8D:C6:C4:E0", new Location(5.5, 0.0, 0.0));
        apLoc.put("84:24:8D:C3:81:A0", new Location(8.25, 7.2, 0.0));
        apLoc.put("84:24:8D:C6:BE:D0", new Location(11.0, 0.0, 0.0));
    }

    public double getDistance(Scan s) {
        double freqInMHz = s.getFreq();
        double levelInDb = s.getSignalLoss();

        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public HashMap<String, Double> getDistances(Collection<Scan> scans) {
        HashMap<String, Double> distances = new HashMap<>();

        for (Scan s : scans) {
            distances.put(s.getBssid().toUpperCase(), getDistance(s));
        }

        return distances;
    }

    public Location getLocation(Collection<Scan> scans) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;

        HashMap<String, Double> distances = this.getDistances(scans);

        for(String s : distances.keySet()) {
            System.out.println(distances.get(s));
        }

        return new Location(0, 0, 0);
    }
}
