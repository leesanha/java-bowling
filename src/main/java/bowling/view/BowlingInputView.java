package bowling.view;

import java.util.Scanner;

public class BowlingInputView {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String getPlayerNameWithPrompt(String message) {
        System.out.print(message);

        return SCANNER.nextLine();
    }

    public static int getBowlingScoreWithPrompt(String message) {
        System.out.print(message);

        return toInt(SCANNER.nextLine());
    }

    private static int toInt(String string) {
        if (string == null || !isOnlyDigits(string)) {
            throw new IllegalArgumentException("숫자로 입력되어야 합니다.");
        }

        return Integer.parseInt(string);
    }

    private static boolean isOnlyDigits(String string) {
        return string.chars().allMatch(Character::isDigit);
    }
}