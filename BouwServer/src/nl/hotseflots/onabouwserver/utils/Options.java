package nl.hotseflots.onabouwserver.utils;


import nl.hotseflots.onabouwserver.Main;

public enum Options
{
    MAX_TRIES(Main.getInstance().getConfig().getInt("Modules.TwoFA.Options.MAX_TRIES")),
    DENY_COMMANDS(Main.getInstance().getConfig().getBoolean("Modules.TwoFA.Options.DENY_COMMANDS")),
    DENY_MOVEMENT(Main.getInstance().getConfig().getBoolean("Modules.TwoFA.Options.DENY_MOVEMENT")),
    DENY_INTERACTION(Main.getInstance().getConfig().getBoolean("Modules.TwoFA.Options.DENY_INTERACTION")),
    DENY_DAMAGE(Main.getInstance().getConfig().getBoolean("Modules.TwoFA.Options.DENY_DAMAGE")),
    DENY_DROPPING(Main.getInstance().getConfig().getBoolean("Modules.TwoFA.Options.DENY_DROPPING"));

    private static Main main;
    private int intValue;
    private String stringValue;
    private boolean booleanValue;

    public static void init(Main main) {
        Options.main = main;
    }

    private Options(int intValue, String stringValue, boolean booleanValue)
    {
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.booleanValue = booleanValue;
    }

    private Options(int intValue)
    {
        this.intValue = intValue;
    }

    private Options(String stringValue)
    {
        this.stringValue = stringValue;
    }

    private Options(boolean booleanValue)
    {
        this.booleanValue = booleanValue;
    }

    public int getIntValue()
    {
        return this.intValue;
    }

    public String getStringValue()
    {
        return this.stringValue;
    }

    public boolean getBooleanValue()
    {
        return this.booleanValue;
    }
}
