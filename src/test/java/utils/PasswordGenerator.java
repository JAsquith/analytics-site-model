package utils;

import java.util.Random;

/**
 * A Simple Class for generating random password strings
 *
 * @author Milton Asquith
 * @version 1.0
 * @since 2016-02-20
 */
public class PasswordGenerator {

    private static final char[] upperCaseChars;
    private static final char[] lowerCaseChars;
    private static final char[] numericChars;
    private static final char[] otherChars;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        numericChars = tmp.toString().toCharArray();

        tmp.setLength(0);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        lowerCaseChars = tmp.toString().toCharArray();

        tmp.setLength(0);
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            tmp.append(ch);
        upperCaseChars = tmp.toString().toCharArray();

        tmp.setLength(0);
        for (char ch = ' '; ch <= '/'; ++ch)
            tmp.append(ch);
        for (char ch = ':'; ch <= '@'; ++ch)
            tmp.append(ch);
        for (char ch = '['; ch <= '_'; ++ch)
            tmp.append(ch);
        for (char ch = '{'; ch <= '~'; ++ch)
            tmp.append(ch);
        otherChars = tmp.toString().toCharArray();
    }

    private static final Random random = new Random();

    public static String getPassword(){
        return getPassword(false);
    }
    public static String getPassword(boolean withUppercase) {
        return getPassword(withUppercase, false);
    }
    public static String getPassword(boolean withUppercase, boolean withNumeric) {
        return getPassword(withUppercase, withNumeric, false);
    }
    public static String getPassword(boolean withUppercase, boolean withNumeric, boolean withNonAlphanumeric) {
        int length = random.nextInt(11)+5;
        return getPassword(withUppercase, withNumeric, withNonAlphanumeric, length);
    }
    public static String getPassword(boolean withUppercase, boolean withNumeric
            , boolean withNonAlphanumeric, int length){
        if (length<3)
            throw new IllegalArgumentException("length < 3 (" + length +")");
        String charSet = "";
        for (char lowerCaseChar : lowerCaseChars) charSet = charSet + lowerCaseChar;
        if (withUppercase)
            for (char upperCaseChar : upperCaseChars) charSet = charSet + upperCaseChar;

        if (withNumeric)
            for (char numericChar : numericChars) charSet = charSet + numericChar;

        if (withNonAlphanumeric)
            for (char otherChar : otherChars) charSet = charSet + otherChar;

        char[] charArray = charSet.toCharArray();
        StringBuilder tmp = new StringBuilder();
        for (int idx = 0; idx < length; idx++)
            tmp.append(charArray[random.nextInt(charArray.length)]);
        return tmp.toString();
    }

    public static String getPasswordOfLength(int length) {
        return getPasswordOfLength(length, false);
    }
    public static String getPasswordOfLength(int length, boolean withUppercase) {
        return getPasswordOfLength(length, withUppercase, false);
    }
    public static String getPasswordOfLength(int length, boolean withUppercase, boolean withNumeric) {
        return getPasswordOfLength(length, withUppercase, withNumeric, false);
    }
    public static String getPasswordOfLength(int length, boolean withUppercase, boolean withNumeric, boolean withNonAlphanumeric) {
        return getPassword(withUppercase, withNumeric, withNonAlphanumeric, length);
    }
}
