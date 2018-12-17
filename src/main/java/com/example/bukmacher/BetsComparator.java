package com.example.bukmacher;

import java.util.Comparator;

public class BetsComparator implements Comparator<Match> {


    public int compare (Match match1, Match match2){
        if(match1.getBets().size()<match2.getBets().size()){
            return 1;
        }else if (match1.getBets().size()>match2.getBets().size()){
            return -1;
        }else if (match1.getBets().size()==match2.getBets().size()){
            return 0;
        }else if (match1.getBets().size()==0&&match2.getBets().size()>0){
            return -1;
        }else if (match1.getBets().size()>0&&match2.getBets().size()==0) {
            return 2;
        }else if (match1.getBets().size()==0&&match2.getBets().size()==0){
            return 0;
        }
        return 1;
    }
}
