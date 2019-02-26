package nl.hotseflots.onabouwserver.modules.TwoFactorAuth;

import java.security.GeneralSecurityException;

public class AuthenticationDetails
{
    private String name;
    private String key;
    private boolean isSetup;
    public int attempts;

    public AuthenticationDetails(String name, String key, boolean isSetup)
    {
        this.isSetup = isSetup;
        this.name = name;
        this.key = key;
    }

    public String getName()
    {
        return this.name;
    }

    public String getKey()
    {
        return this.key;
    }

    public boolean isSetup()
    {
        return this.isSetup;
    }

    public String generateValidPasscode()
            throws GeneralSecurityException
    {
        return TOTP.generateCurrentNumberString(this.key);
    }
}