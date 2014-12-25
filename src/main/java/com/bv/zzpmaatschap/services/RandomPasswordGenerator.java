package com.bv.zzpmaatschap.services;


import java.util.Date;
import java.util.Random;

public class RandomPasswordGenerator {

    public static String generate() {
        Random rnd = new Random();
        rnd.setSeed(new Date().getTime());
        Long l = Math.abs(rnd.nextLong());
        StringBuilder sb = new StringBuilder();
        Character begin1 = new Character('0');
        Character begin2 = new Character('a');
        Character begin3 = new Character('A');
        double diff1 = determineLength(begin1, new Character('9'));
        double diff2 = determineLength(begin2, new Character('z'));
        double diff3 = determineLength(begin3, new Character('Z'));

        String source = String.valueOf(l*l).concat(String.valueOf(l)).concat(String.valueOf(l));
        for (int i = 0; i < (4 * (3*2)); i = i + (3*2)) {
            sb.append(Character.toString((char)convertPart(begin1, diff1, source, i)));
            sb.append(Character.toString((char)convertPart(begin2, diff2, source, i + 2)));
            sb.append(Character.toString((char)convertPart(begin3, diff3, source, i + 4)));

        }

        return sb.toString();
    }

    public static double determineLength(char begin, char end) {

        return end - begin;
    }

    public static int convertPart(char begin, double diff, String ff, int i) {
        String part1 = ff.substring(i, i + 2);
        double p1 = new Integer(part1);
        return begin + Double.valueOf((diff / 100) * p1).intValue();
    }
}
