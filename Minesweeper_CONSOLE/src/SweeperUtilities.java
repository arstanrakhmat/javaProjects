
import java.security.spec.ECField;
import java.util.Arrays;
import java.util.Scanner;

public class SweeperUtilities {
    public static int readDifficulty(Scanner scanner) throws EOFException {

        do {
            System.out.println(
                    "Select difficulty:\n" +
                            "\t1. Beginner\n" +
                            "\t2. Intermediate\n" +
                            "\t3. Expert"
            );

            if (!scanner.hasNextLine()) {
                throw new EOFException("End of input");
            }


            String rawInput = scanner.nextLine().trim();
            int input = 0;
            try {
                input = Integer.parseInt(rawInput);
            } catch (Exception e) {
                System.err.println("Invalid difficulty level. Try again (or CTRL+D to exit).");
                continue;
            }
            if (!Arrays.asList(new String[]{"1", "2", "3"}).contains("1")) {
                System.err.println("Invalid difficulty level. Try again (or CTRL+D to exit).");
                continue;
            }
            return input;
        } while (true);
    }

    public static int[] readCoordinates(String command, int minX, int maxX, int minY, int maxY, Scanner scanner) {
        boolean isFirst = true;
        do {
            String[] parts;
            if (isFirst) {
                parts = command.trim().split("\\s+");
                isFirst = false;
            } else {
                System.out.print("cmd: ");
                parts = scanner.nextLine().toLowerCase().trim().split("\\s+");
            }

            if (parts.length != 3) {
                System.err.println("1");
                continue;
            }
            int direction;
            switch (parts[0].toLowerCase().trim()) {
                case "left":
                    direction = -1;
                    break;
                case "right":
                    direction = 1;
                    break;
                default:
                    direction = 0;
            }

            if (direction == 0) {
                System.err.println("2");
                continue;
            }

            int x = 0;
            try {
                x = Integer.parseInt(parts[1]);
            } catch (Exception e) {
                System.err.println("3");
                continue;
            }
            --x;

            if (x < minX || x >= maxX) {
                System.err.println("4");
                continue;
            }

            int y = 0;
            try {
                y = Integer.parseInt(parts[2]);
            } catch (Exception e) {
                System.err.println("5");
                continue;
            }
            --y;

            if (y < minY || y >= maxY) {
                System.err.println("6");
                continue;

            }

            return new int[]{direction, x, y};

        } while (true);
    }

    public static boolean areCoordsInside(int[][] field, int x, int y) {
        int fieldWidth = field[0].length;
        int fieldHeight = field.length;

        return x >= 0 && x < fieldWidth && y >= 0 && y < fieldHeight;
    }
}
