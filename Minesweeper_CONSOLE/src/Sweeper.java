import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Scanner;

class EOFException extends Exception {
    public EOFException(String message) {
        super(message);
    }
}

public class Sweeper {
    private static final int MINES_FOR_BEGINNER = 10;
    private static final int MINES_FOR_INTERMEDIATE = 40;
    private static final int MINES_FOR_EXPERT = 99;

    private static final int FIELD_WIDTH_FOR_BEGINNER = 8;
    private static final int FIELD_HEIGHT_FOR_BEGINNER = 8;
    private static final int FIELD_WIDTH_FOR_INTERMEDIATE = 16;
    private static final int FIELD_HEIGHT_FOR_INTERMEDIATE = 16;
    private static final int FIELD_WIDTH_FOR_EXPERT = 24;
    private static final int FIELD_HEIGHT_FOR_EXPERT = 24;

    private static final int FLAGS_FOR_BEGINNER = 10;
    private static final int FLAGS_FOR_INTERMEDIATE = 40;
    private static final int FLAGS_FOR_EXPERT = 99;

    private static final String MINE_CELL = "*";
    private static final String EMPTY_CELL = "#";
    private static final String COVERED_CELL = ".";
    private static final String FLAG = "T";

    private static final int MINE_VALUE = -1;
    private static final int EMPTY_VALUE = 0;

    private static final int[][] SHIFTS = { // shifts to find neighbouring cells
            {-1, -1, -1, 0, 0, 0, 1, 1, 1},
            {-1, 0, 1, -1, 0, 1, -1, 0, 1}
    };

    Scanner scanner;

    public Sweeper() {
        this.scanner = new Scanner(System.in);
    }

    void run() {
        System.out.println("Minesweeper\n");

        int difficulty = 1;   //change to strings
        try {
            difficulty = SweeperUtilities.readDifficulty(scanner);
        } catch (EOFException ignored) {
            System.exit(0);
        }

        int mines, cellsToUncover, flag_difficulty;
        int fieldWidth, fieldHeight;
        switch (difficulty) {
            case 1:
                mines = MINES_FOR_BEGINNER;
                fieldWidth = FIELD_WIDTH_FOR_BEGINNER;
                fieldHeight = FIELD_HEIGHT_FOR_BEGINNER;
                flag_difficulty = FLAGS_FOR_BEGINNER;
                cellsToUncover = fieldWidth * fieldHeight - mines;
                break;
            case 2:
                mines = MINES_FOR_INTERMEDIATE;
                fieldWidth = FIELD_WIDTH_FOR_INTERMEDIATE;
                fieldHeight = FIELD_HEIGHT_FOR_INTERMEDIATE;
                flag_difficulty = FLAGS_FOR_INTERMEDIATE;
                cellsToUncover = fieldWidth * fieldHeight - mines;
                break;
            case 3:
            default:
                mines = MINES_FOR_EXPERT;
                fieldWidth = FIELD_WIDTH_FOR_EXPERT;
                fieldHeight = FIELD_HEIGHT_FOR_EXPERT;
                flag_difficulty = FLAGS_FOR_EXPERT;
                cellsToUncover = fieldWidth * fieldHeight - mines;
                break;
        }

        ArrayList<Pair<Integer, Integer>> flags = new ArrayList<>();
        boolean[][] uncoveredField = new boolean[fieldHeight][fieldWidth];
        int[][] field = new int[fieldHeight][fieldWidth];

        int[] selection;
        int selX = 0, selY = 0;


        ArrayList<Integer[]> potentialMinePlaces = new ArrayList<>();
        for (int y = 0; y < fieldHeight; ++y) {
            outer:
            for (int x = 0; x < fieldWidth; ++x) {
                for (int i = 0; i < SHIFTS[0].length; ++i) {
                    int ny = selY + SHIFTS[0][i];
                    int nx = selX + SHIFTS[1][i];
                    if (x == nx && y == ny) {
                        break outer;
                    }
                }
                potentialMinePlaces.add(new Integer[]{y, x});
            }
        }

        for (int i = 0; i < mines; ++i) {
            int randomIndex = (int) (Math.random() * potentialMinePlaces.size());
            Integer[] mine = potentialMinePlaces.get(randomIndex);
            potentialMinePlaces.remove(randomIndex);

            int y = mine[0];
            int x = mine[1];
            field[y][x] = MINE_VALUE;
            for (int j = 0; j < SHIFTS[0].length; ++j) {
                int ny = y + SHIFTS[0][j];
                int nx = x + SHIFTS[1][j];
                if (SweeperUtilities.areCoordsInside(field, nx, ny) && field[ny][nx] != MINE_VALUE) {
                    ++field[ny][nx];
                }
            }
        }

        boolean lost = false, won = cellsToUncover == 0, switchBreak = false;
        int mouse = 0;
        help();
        present(field, uncoveredField, flags);
        while (!lost && !won) {
            switchBreak = false;
            System.out.print("cmd: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "quit":
                    System.out.println("Bye");
                    System.exit(1);
                    break;
                case "show":
                    showMines(field);
                    switchBreak = true;
                    break;
                case "help":
                    help();
                    switchBreak = true;
                    break;
                default:
                    selection = SweeperUtilities.readCoordinates(command, 0, fieldWidth, 0, fieldHeight, scanner);
                    mouse = selection[0];
                    selX = selection[1];
                    selY = selection[2];
                    break;


            }
            if (switchBreak) {
                continue;
            }
            int cell = field[selY][selX];
            if (mouse == -1 && !isFlag(selY, selX, flags)) {
                if (cell == MINE_VALUE) {
                    uncoveredField[selY][selX] = true;
                    lost = true;
                } else if (cell == EMPTY_VALUE) {
                    cellsToUncover -= floodUncover(selX, selY, field, uncoveredField);
                } else {
                    uncoveredField[selY][selX] = true;
                    --cellsToUncover;
                }
            } else if (mouse == 1 && flags.size() <= flag_difficulty) {
                int index = checkFlags(flags, selX, selY);
                if (index == -1) {
                    flags.add(new Pair<>(selX, selY));
                    cellsToUncover--;
                } else {
                    cellsToUncover++;
                    flags.remove(index);
                }
            }
            present(field, uncoveredField, flags);
            if (cellsToUncover == 0) {
                won = true;
            }
        }

        present(field, flags);

        if (lost) {
            System.out.println("Bad luck, the mine went off.");
        } else {
            System.out.println("You won! The field was cleared.");
        }
    }

    private int checkFlags(ArrayList<Pair<Integer, Integer>> flags, int selX, int selY) {
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).getKey() == selX && flags.get(i).getValue() == selY) {
                return i;
            }
        }
        return -1;
    }

    private static void present(int[][] field, ArrayList<Pair<Integer, Integer>> flags) {
        present(field, null, flags);
    }

    private static void present(int[][] field, boolean[][] uncoveredField, ArrayList<Pair<Integer, Integer>> flags) {
        int fieldWidth = field[0].length;
        int fieldHeight = field.length;

        for (int y = 0; y < fieldHeight; ++y) {
            for (int x = 0; x < fieldWidth; ++x) {
                if (uncoveredField == null || uncoveredField[y][x]) {
                    int cell = field[y][x];
                    if (cell == MINE_VALUE) {
                        System.out.print(MINE_CELL);
                    } else if (cell == EMPTY_VALUE) {
                        System.out.print(EMPTY_CELL);
                    } else {
                        System.out.print(cell);
                    }
                } else {
                    boolean cond = isFlag(y, x, flags);
                    if (cond) {
                        System.out.print(FLAG);
                    } else {
                        System.out.print(COVERED_CELL);
                    }
                }
            }
            System.out.println();
        }
    }

    private static boolean isFlag(int y, int x, ArrayList<Pair<Integer, Integer>> flags) {
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).getKey() == x && flags.get(i).getValue() == y) {
                return true;
            }
        }
        return false;
    }

    private static int floodUncover(int selX, int selY, int[][] field, boolean[][] uncoveredField) {
        int uncoveredCells = 0;

        for (int i = 0; i < SHIFTS[0].length; ++i) {
            int ny = selY + SHIFTS[0][i];
            int nx = selX + SHIFTS[1][i];
            if (SweeperUtilities.areCoordsInside(field, nx, ny) && !uncoveredField[ny][nx]) {
                uncoveredField[ny][nx] = true;
                ++uncoveredCells;

                if (field[ny][nx] == EMPTY_VALUE) {
                    uncoveredCells += floodUncover(nx, ny, field, uncoveredField);
                }
            }
        }
        return uncoveredCells;
    }

    private static void showMines(int[][] field) {
        int fieldWidth = field[0].length;
        int fieldHeight = field.length;

        for (int y = 0; y < fieldHeight; ++y) {
            for (int x = 0; x < fieldWidth; ++x) {
                int cell = field[y][x];
                if (cell == MINE_VALUE) {
                    System.out.print(MINE_CELL);
                } else if (cell == EMPTY_VALUE) {
                    System.out.print(EMPTY_CELL);
                } else {
                    System.out.print(cell);
                }
            }
            System.out.println();
        }
    }

    private static void help() {
        System.out.println("" +
                "Help:\n" +
                "left <row> <col>\n" +
                "     -left click with coordinates (row, col)\n" +
                "right <row> <col>\n" +
                "     -right click with coordinates (row, col)\n" +
                "show\n" +
                "     -show all mines (cheating)\n" +
                "quit\n" +
                "     -quit the game (EOF work too)\n" +
                "help\n" +
                "     -this text\n");
    }
}

