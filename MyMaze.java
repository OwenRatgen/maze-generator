import java.util.Scanner;

public class MyMaze{
    private static int randStartRow;
    private static int randEndRow;
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        this.startRow = startRow;   // Setting up attributes? Ask in OH
        this.endRow = endRow;   // Setting up attributes? Ask in OH
        maze = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell();
            }
        }
    }

    public static MyMaze makeMaze(int level) {
        int randDirection = 0;
        MyMaze mazeGen = new MyMaze(0,0,0,0);


        if (level == 1) {
            randStartRow = (int) (Math.random() * 4); // src1
            randEndRow = (int) (Math.random() * 4); // src1
            mazeGen = new MyMaze(5, 5, randStartRow, randEndRow);
        }
        else if (level == 2) {
            randStartRow = (int) (Math.random() * 4) + 1; // src1
            randEndRow = (int) (Math.random() * 4) + 1; // src1
            mazeGen = new MyMaze(5, 20, randStartRow, randEndRow);
        }
        else if (level == 3) {
            randStartRow = (int) (Math.random() * 19) + 1; // src1
            randEndRow = (int) (Math.random() * 19) + 1; // src1
            mazeGen = new MyMaze(20, 20, randStartRow, randEndRow);
        }

        Stack1Gen<int[]> mazeStack = new Stack1Gen<int[]>();
        int[] index = new int[2];
        index[0] = randStartRow;
        index[1] = 0;
        mazeStack.push(index);
        mazeGen.maze[randStartRow][0].setVisited(true);

        while (!mazeStack.isEmpty()) {
            randDirection = (int) (Math.random() * mazeGen.maze.length-1) + 1; // src1
            int[] topStack = mazeStack.top();
            int[] currIndex = topStack;
            int currRow = currIndex[0];
            int currCol = currIndex[1];
            boolean upCellVisited = true;
            boolean downCellVisited = true;
            boolean rightCellVisited = true;
            boolean leftCellVisited = true;



            if (randDirection == 1 && currRow + 1 < mazeGen.maze.length && !mazeGen.maze[currRow + 1][currCol].getVisited()) { // If random direction equals one then the next cell will be a row above the current one
                mazeStack.push(new int[] {currRow+1, currCol});
                mazeGen.maze[currRow+1][currCol].setVisited(true);
                if (currRow <= mazeGen.maze.length-2) {
                    mazeGen.maze[currRow][currCol].setBottom(false);
                }
                currRow++;
            }
            else if (randDirection == 2 && currRow - 1 >= 0 && !mazeGen.maze[currRow - 1][currCol].getVisited()) { // If random direction equals two then the next cell will be a row below the current one
                currRow--;
                mazeStack.push(new int[] {currRow, currCol});
                mazeGen.maze[currRow][currCol].setVisited(true);
                mazeGen.maze[currRow][currCol].setBottom(false);
            }
            else if (randDirection == 3 && currCol + 1 < mazeGen.maze[0].length && !mazeGen.maze[currRow][currCol + 1].getVisited()) { // If random direction equals three then the next cell will be a column to the right of the current one
                mazeStack.push(new int[] {currRow, currCol+1});
                mazeGen.maze[currRow][currCol+1].setVisited(true);
                if (currCol <= mazeGen.maze[0].length-2) {
                    mazeGen.maze[currRow][currCol].setRight(false);
                }
                currCol++;
            }
            else if (randDirection == 4 && currCol - 1 >= 0 && !mazeGen.maze[currRow][currCol - 1].getVisited()) { // If random direction equals four then the next cell will be a column to the left of the current one
                currCol--;
                mazeStack.push(new int[] {currRow, currCol});
                mazeGen.maze[currRow][currCol].setVisited(true);
                mazeGen.maze[currRow][currCol].setRight(false);
            }

            if (currRow + 1 < mazeGen.maze.length) { // Every if statement from here and below is checking if the current cell has a neighbor that hasn't been visited
                if (!mazeGen.maze[currRow+1][currCol].getVisited()) {
                    downCellVisited = false;
                }
            }

            if (currCol + 1 < mazeGen.maze[0].length) {
                if (!mazeGen.maze[currRow][currCol+1].getVisited()) {
                    rightCellVisited = false;
                }
            }

            if (currRow - 1 >= 0) {
                if (!mazeGen.maze[currRow-1][currCol].getVisited()) {
                    upCellVisited = false;
                }
            }

            if (currCol - 1 >= 0) {
                if (!mazeGen.maze[currRow][currCol-1].getVisited()) {
                    leftCellVisited = false;
                }
            }

            if (upCellVisited && downCellVisited && rightCellVisited && leftCellVisited) {
                mazeStack.pop();
            }
        }


        for (int i = 0; i < mazeGen.maze.length; i++) {
            for (int j = 0; j < mazeGen.maze[0].length; j++) {
                mazeGen.maze[i][j].setVisited(false);
            }
        }

        return mazeGen;
    }

    public void printMaze() {
        String rowBorders = "";
        String rowAsteriks = "";
        int count = 0;
        boolean reachedExit = false;

        while (count < maze[0].length) {
            if (count == 0) { // Initializes the topmost borders
                System.out.print("|---|");
            }
            else {
                System.out.print("---|");
            }
            count++;
        }
        for (int i = 0; i < maze.length; i++) {
            if (i != 0) {
                System.out.println(rowAsteriks);
            }
            System.out.println(rowBorders);
            rowBorders = "";
            rowAsteriks = "";
            for (int j = 0; j < maze[0].length; j++) {

                if (j == 0 && i != getRandStartRow()) { // This if statement sets up the left borders of the maze
                        rowBorders += "|";
                        rowAsteriks += "|";
                }
                else if (j == 0 && i == getRandStartRow()){ // This checks to see if the index is on the random starting index
                    rowBorders += "|";
                    rowAsteriks += " ";
                }

                if (j == maze[0].length-1 && !maze[i][j].getBottom()) { // This sets up the borders on the rightmost part of the maze
                        rowBorders += "   |";
                }

                if (maze[i][j].getBottom() && i != maze.length) { // Checks to see if the element has a bottom
                    rowBorders += "---|";
                }
                else {
                    rowBorders += "    ";
                }

                if (maze[i][j].getVisited() && maze[i][j].getRight() && (j != maze[0].length-1 || i != getRandEndRow())) {// Checks to see if the element has a right and if it is the random ending index
                        rowAsteriks += " * |";
                }
                else if (maze[i][j].getVisited() && maze[i][j].getRight() && i == getRandEndRow() && j == maze[0].length-1) {
                    rowAsteriks += " *  ";
                }
                else if (maze[i][j].getVisited()) {
                        rowAsteriks += " *  ";
                }
                else if (!maze[i][j].getVisited() && maze[i][j].getRight()){
                    rowAsteriks += "   |";
                }
                else {
                    rowAsteriks += "    ";
                }
            }
        }
        System.out.println(rowAsteriks);
        System.out.println(rowBorders);
    }

    public void solveMaze() {
        Q1Gen<int[]> mazeQ = new Q1Gen<int[]>();
        int[] index = new int[2];
        index[0] = randStartRow;
        index[1] = 0;
        mazeQ.add(index);

        while (mazeQ.length() > 0) { // Checks the queue until it is empty
            int[] currIndex = mazeQ.remove(); // currIndex is set equal to the first element in queue and is then removed
            int row = currIndex[0];
            int col = currIndex[1];
            maze[row][col].setVisited(true);

            if (row == getRandEndRow() && col == maze[0].length-1) {
                break;
            }

            if (row - 1 >= 0 && !maze[row-1][col].getBottom() && !maze[row-1][col].getVisited()) {
                mazeQ.add(new int[] {row-1, col});
            }
            if (row + 1 < maze.length && !maze[row][col].getBottom() && !maze[row+1][col].getVisited()) {
                mazeQ.add(new int[] {row+1, col});
            }
            if (col - 1 >= 0 && !maze[row][col-1].getRight() && !maze[row][col-1].getVisited()) {
                mazeQ.add(new int[] {row, col-1});
            }
            if (col + 1 < maze[0].length && !maze[row][col].getRight() && !maze[row][col+1].getVisited()) {
                mazeQ.add(new int[] {row, col+1});
            }
        }

        printMaze();
    }

    public int getRandStartRow() {
        return randStartRow;
    }
    public void setRandStartRow(int num) {
        randStartRow = num;
    }

    public int getRandEndRow() {
        return randEndRow;
    }
    public void setRandEndRow(int num) {
        randEndRow = num;
    }

    public static void main(String[] args){
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Choose level 1, 2, or 3");
        int levelChoice = myScanner.nextInt();
        MyMaze mazeTest;


        if (levelChoice == 1) {
            mazeTest = makeMaze(1);
            mazeTest.solveMaze();
        }
        else if (levelChoice == 2) {
            mazeTest = makeMaze(2);
            mazeTest.solveMaze();
        }
        else if (levelChoice == 3) {
            mazeTest = makeMaze(3);
            mazeTest.solveMaze();
        }
        else {
            System.out.println("ERROR");
        }
    }
}
