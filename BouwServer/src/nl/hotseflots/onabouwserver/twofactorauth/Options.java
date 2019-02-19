package nl.hotseflots.onabouwserver.twofactorauth;

import nl.hotseflots.onabouwserver.Main;

public enum Options {
    MAX_TRIES(Main.plugin.getTwoFACFG().getInt("options.max-tries")), DENY_COMMANDS(Main.plugin.getTwoFACFG().getBoolean("options.deny-commands")), DENY_MOVEMENT(Main.plugin.getTwoFACFG().getBoolean("options.deny-movement")), DENY_INTERACTION(Main.plugin.getTwoFACFG().getBoolean("options.deny-interaction")), DENY_DAMAGE(Main.plugin.getTwoFACFG().getBoolean("options.deny-damage"));

    private int intValue;
    private String stringValue;
    private boolean booleanValue;

    private Options(int intValue, String stringValue, boolean booleanValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.booleanValue = booleanValue;
    }

    private Options(int intValue) {
        this.intValue = intValue;
    }

    private Options(String stringValue) {
        this.stringValue = stringValue;
    }

    private Options(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public boolean getBooleanValue() {
        return this.booleanValue;
    }
}
