package com.hollingsworth.arsnouveau.common.lib;

public class RitualLib {

    public static final String DIG = prependRitual("burrowing");
    public static final String MOONFALL = prependRitual("moonfall");
    public static final String SUNRISE = prependRitual("sunrise");
    public static final String CLOUDSHAPER = prependRitual("cloudshaping");
    public static final String DISINTEGRATION = prependRitual("disintegration");
    public static final String CHALLENGE = prependRitual("challenge");
    public static final String OVERGROWTH = prependRitual("overgrowth");
    public static final String FERTILITY = prependRitual("fertility");
    public static final String RESTORATION = prependRitual("restoration");
    public static final String WARP = prependRitual("warping");
    public static final String SCRYING = prependRitual("scrying");
    public static final String FLIGHT = prependRitual("flight");
    public static final String WILDEN_SUMMON = prependRitual("wilden_summon");
    public static final String BINDING = prependRitual("binding");
    public static final String AWAKENING = prependRitual("awakening");

    public static String prependRitual(String ritual) {
        return "ritual_" + ritual;
    }
}
