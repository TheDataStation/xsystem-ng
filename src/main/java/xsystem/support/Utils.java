package xsystem.support;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

import xsystem.enums.CharClass;
import xsystem.enums.LCC;
import xsystem.enums.NUM;
import xsystem.enums.SPC;
import xsystem.enums.UCC;

public class Utils {

    //Returns Character Class of given character
    public static CharClass getCharacterClass(char c) {
        new Config();
        if(new String(Config.upperCaseChar).contains(Character.toString(c)))
            return new UCC();
        else if(new String(Config.lowerCaseChar).contains(Character.toString(c)))
            return new LCC();
        else if(new String(Config.numbers).contains(Character.toString(c)))
            return new NUM();
        else return new SPC(Character.toString(c));
    }

    //Performs Chi-Square test expecting a uniform distribution
    public static Boolean significant(ArrayList<Double> observed) {
        if(observed.size()<2) return false;
        else{
            ChiSquareTest chi = new ChiSquareTest();
            double[] exp = new double[observed.size()];
            long[] obs = new long[observed.size()];
            for(int i = 0; i< observed.size(); i++){
                obs[i] = observed.get(i).longValue();
                exp[i] = 1.0;
            }
            return chi.chiSquareTest(exp, obs, 0.01);
        }
    }

    public static HashMap<Character, Double> asciiMap() {
        HashMap<Character, Double> asciMap = new HashMap<>();
        for(int i=0; i<=256; i++) asciMap.put((char)i, 0.0);
        return asciMap;
    }

    public static ArrayList<Wrapper> mergeStreams(ArrayList<ArrayList<Wrapper>> streams){
        int next = 0;
        Boolean done = true;
        return mergeStreams(streams, next, done);
    }

    public static ArrayList<Wrapper> mergeStreams(ArrayList<ArrayList<Wrapper>> streams, int next) {
        Boolean done = true;
        return mergeStreams(streams, next, done);
    }

    public static ArrayList<Wrapper> mergeStreams(ArrayList<ArrayList<Wrapper>> streams, int next, Boolean done) {
        if(streams.isEmpty())
            return new ArrayList<Wrapper>();
        else{
            if(streams.size() - next == 1 && streams.get(next).isEmpty() && done)
                return new ArrayList<>();
            else if(streams.size() - next == 1 && streams.get(next).isEmpty() && !done)
                return mergeStreams(streams, (next+1)%streams.size());
            else if(streams.get(next).isEmpty())
                return mergeStreams(streams, (next+1)%streams.size(), done);
            else{
                ArrayList<Wrapper> res = new ArrayList<>();
                res.add(streams.get(next).get(0));
                ArrayList<Wrapper> newVal = streams.get(next);
                newVal.remove(0);
                streams.set(next, newVal);
                res.addAll(mergeStreams(streams, (next+1)%streams.size(), false));
                return res;
            }
        }
    }
}