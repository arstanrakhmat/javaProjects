public class Model {
    Levels levels = new Levels();

    private int boyX, boyY;
    private int boyDirection = 2;//куда смотрит
    private int lev = 1;
    private int moves = 0;
    private int[][] desktop = levels.getLevel(lev);
    private int[][] ground = levels.getGround(lev);


    public void moveUp() {
        if (desktop[boyY - 1][boyX] == 0) {
            desktop[boyY - 1][boyX] = 2;
            desktop[boyY][boyX] = 0;
            moves++;
        } else if (desktop[boyY - 1][boyX] == 4) {
            if (desktop[boyY - 2][boyX] == 0) {
                desktop[boyY - 2][boyX] = 4;
                desktop[boyY - 1][boyX] = 2;
                desktop[boyY][boyX] = 0;
                moves++;
            }
        }
        boyDirection = 0;
    }

    public void moveRight() {
        if (desktop[boyY][boyX + 1] == 0) {
            desktop[boyY][boyX + 1] = 2;
            desktop[boyY][boyX] = 0;
            moves++;
        } else if (desktop[boyY][boyX + 1] == 4) {
            if (desktop[boyY][boyX + 2] == 0) {
                desktop[boyY][boyX + 2] = 4;
                desktop[boyY][boyX + 1] = 2;
                desktop[boyY][boyX] = 0;
                moves++;
            }
        }
        boyDirection = 1;
    }

    public void moveDown() {
        if (desktop[boyY + 1][boyX] == 0) {
            desktop[boyY + 1][boyX] = 2;
            desktop[boyY][boyX] = 0;
            moves++;
        } else if (desktop[boyY + 1][boyX] == 4) {
            if (desktop[boyY + 2][boyX] == 0) {
                desktop[boyY + 2][boyX] = 4;
                desktop[boyY + 1][boyX] = 2;
                desktop[boyY][boyX] = 0;
                moves++;
            }
        }
        boyDirection = 2;
    }

    public void moveLeft() {
        if (desktop[boyY][boyX - 1] == 0) {
            desktop[boyY][boyX - 1] = 2;
            desktop[boyY][boyX] = 0;
            moves++;
        } else if (desktop[boyY][boyX - 1] == 4) {
            if (desktop[boyY][boyX - 2] == 0) {
                desktop[boyY][boyX - 2] = 4;
                desktop[boyY][boyX - 1] = 2;
                desktop[boyY][boyX] = 0;
                moves++;
            }
        }
        boyDirection = 3;
    }

    public void getData() {
        for (int i = 0; i < desktop.length; i++) {
            for (int j = 0; j < desktop[i].length; j++) {
                if (desktop[i][j] == 2) {
                    boyY = i;
                    boyX = j;
                }
            }
        }
    }

    public void nextLevel() {
        if (lev + 1 == 5) {
            lev = 1;
        } else {
            lev++;
        }
        desktop = levels.getLevel(lev);
        ground = levels.getGround(lev);
        moves = 0;
        boyDirection = 2;
    }

    public void levelBefore() {
        if (lev - 1 == 0) {
            lev = 2;
        } else {
            lev--;
        }
        desktop = levels.getLevel(lev);
        ground = levels.getGround(lev);
        moves = 0;
        boyDirection = 2;
    }

    public boolean check() {
        int n = 0;
        int s = 0;
        for (int i = 0; i < ground.length; i++) {
            for (int j = 0; j < ground[i].length; j++) {
                if (ground[i][j] == 7) {
                    s++;
                    if (desktop[i][j] == 4) {
                        n++;
                    }
                }
            }
        }
        return n == s;
    }

    public int[][] getLevel() {
        return desktop;
    }

    public int[][] getGround() {
        return ground;
    }

    public int getLev() {
        return lev;
    }

    public int getMoves() {
        return moves;
    }

    public void reset() {
        moves = 0;
        desktop = levels.getLevel(lev);
        ground = levels.getGround(lev);
    }

    public int getBoyDirection() {
        return boyDirection;
    }
}
