package com.hotseflots.bouwserver.utils;

import com.hotseflots.bouwserver.Main;

public class ConfigPaths {

    public static String namePath(String PlayerUUID) {
        return "players-history." + PlayerUUID + ".name";
    }

    public static String lastLoginPath(String PlayerUUID) {
        return "players-history." + PlayerUUID + ".last-login";
    }

    public static String ipAdressPath(String PlayerUUID) {
        return "players-history." + PlayerUUID + ".ip-adress";
    }

    public static String amountPath(String PlayerUUID, String punishment) {
        return "players-history." + PlayerUUID + "." + punishment + ".amount";
    }

    public static String punishmentIDPath(String PlayerUUID, String punishment, int punishmentID) {
        return "players-history." + PlayerUUID + "." + punishment + "." + punishmentID;
    }

    public static String GetDetailPath(String PlayerUUID, String punishment, int punishmentID, String detail) {
        return "players-history." + PlayerUUID + "." + punishment + "." + punishmentID + "." + detail;
    }

    public static String bannedPlayers() {
        return "banned-players";
    }

    public static String mutedPlayers() {
        return "muted-players";
    }

    public static String frozenPlayers() {
        return "frozen-players";
    }
}
