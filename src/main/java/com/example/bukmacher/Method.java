package com.example.bukmacher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Method {
    public static List<Match> compareALl(List<Match>matches){
        matches.sort(new BetsComparator());
        return matches;
    }
}
