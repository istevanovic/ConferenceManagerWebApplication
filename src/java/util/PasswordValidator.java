
package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN
            = "pattern";
            //"^(?=.*[A-Z])(?=(?:.*[a-z]){3})(?=.*[0-9])(?=.*[!"#$%&'()*+,\-./:;<=>?@[\]^_`{|}~])(?=(?:(.)(?!\1\1))+$)[a-zA-Z0-9].{7,11}$";
            

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    
    public boolean validate(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}


