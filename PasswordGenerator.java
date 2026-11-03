import java.security.SecureRandom;

// This class contains ALL the password logic.
// It does not know anything about web pages - it only makes and checks passwords.
public class PasswordGenerator {

    // The groups of characters a password can use
    private static final String UPPER   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER   = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS  = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+";

    // SecureRandom is safer than Random for passwords
    private SecureRandom random = new SecureRandom();

    // Pick one random character from a text
    private char randomChar(String text) {
        int index = random.nextInt(text.length());
        return text.charAt(index);
    }

    // Generate a password
    public String generate(int length, boolean useUpper, boolean useLower,
                           boolean useDigits, boolean useSymbols) {

        String allChars = "";   // all characters we are allowed to use
        String password = "";   // the password we are building

        // Step 1: add one guaranteed character from every chosen group
        if (useUpper)   { allChars += UPPER;   password += randomChar(UPPER); }
        if (useLower)   { allChars += LOWER;   password += randomChar(LOWER); }
        if (useDigits)  { allChars += DIGITS;  password += randomChar(DIGITS); }
        if (useSymbols) { allChars += SYMBOLS; password += randomChar(SYMBOLS); }

        // If the user selected nothing, we cannot make a password
        if (allChars.isEmpty()) {
            return "";
        }

        // Step 2: fill the remaining length with random characters
        while (password.length() < length) {
            password += randomChar(allChars);
        }

        // Step 3: shuffle so the guaranteed characters are not always at the start
        return shuffle(password);
    }

    // Mix up the characters of a text (Fisher-Yates shuffle)
    private String shuffle(String text) {
        char[] letters = text.toCharArray();
        for (int i = letters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = letters[i];
            letters[i] = letters[j];
            letters[j] = temp;
        }
        return new String(letters);
    }

    // Give the password a score and return Weak / Medium / Strong
    public String checkStrength(String password) {
        int score = 0;

        if (password.length() >= 8)  score++;   // not too short
        if (password.length() >= 12) score++;   // nice and long

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSymbol = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c))      hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c))     hasDigit = true;
            else                               hasSymbol = true;
        }

        if (hasUpper)  score++;
        if (hasLower)  score++;
        if (hasDigit)  score++;
        if (hasSymbol) score++;

        // Maximum score is 6
        if (score <= 3) return "Weak";
        if (score <= 5) return "Medium";
        return "Strong";
    }
}
