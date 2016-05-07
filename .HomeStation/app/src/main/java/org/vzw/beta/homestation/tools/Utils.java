package org.vzw.beta.homestation.tools;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by user109 on 21/03/2016.
 */
public class Utils {


    public final static String LANGUAGE_NL = "nl";
    public final static String LANGUAGE_EN = "en";
    public final static int FRAG_ACTION_HOME = 00;
    public final static int FRAG_ACTION_RADAR = 03;
    public final static int FRAG_ACTION_WEATHER = 02;
    public final static int FUEL= 03;
    public static int RULE_ELECTRICITY = 0;
    public static int RULE_GAS = 0;
    public static int RULE_WATER = 0;
    public static int RULE_FUEL = 0;
    public static String weatherIcon;
    public static Firebase myFirebaseRef;


    //    public static ArrayList<String> dataArrayDB = new ArrayList<>();
    public static Map<String, Object> dataObjectsElectricityDB = new HashMap<>();
    public static Map<String, Object> dataObjectsWaterDB = new HashMap<>();
    public static Map<String, Object> dataObjectsGasDB = new HashMap<>();
    public static Map<String, Object> dataObjectsFuelDB = new HashMap<>();
    public static Map<String, Object> savedDataObjectsDB = new HashMap<>();


}
