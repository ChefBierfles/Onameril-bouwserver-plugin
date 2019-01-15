package com.hotseflots.bouwserver.utils;

import com.hotseflots.bouwserver.Main;

public class ConfigPaths {

    public static String namePath(String PlayerUUID) {
        return PlayerUUID + ".name";
    }

    public static String lastLoginPath(String PlayerUUID) {
        return PlayerUUID + ".last-login";
    }

    public static String ipAdressPath(String PlayerUUID) {
        return PlayerUUID + ".ip-adress";
    }

    public static String amountPath(String PlayerUUID, String punishment) {
        return PlayerUUID + "." + punishment + ".amount";
    }

    public static String punishmentIDPath(String PlayerUUID, String punishment, int punishmentID) {
        return PlayerUUID + "." + punishment + "." + punishmentID;
    }

    public static String GetDetailPath(String PlayerUUID, String punishment, int punishmentID, String detail) {
        return PlayerUUID + "." + punishment + "." + punishmentID + "." + detail;
    }
}
