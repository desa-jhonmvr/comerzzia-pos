
package com.comerzzia.util.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MGRI
 */
public class ValidadorTPV {
    
    public static boolean validarEmail(String email) {
        
        if (email.contains(".@") || email.contains("sukasa") || email.contains("todohogar")){
            return false;
        }
        Pattern p = Pattern.compile("[a-zA-Z0-9]+[.[a-zA-Z0-9_-]+]*@[a-z0-9][\\w\\.-]*[a-z0-9]\\.[a-z][a-z\\.]*[a-z]$");//me gusta esta
        Matcher m = p.matcher(email);

        return m.matches();
    
    }
}
