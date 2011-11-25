package com.brodwall.kata.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuSolver {
    private SudokuBoard board = new SudokuBoard();

    public SudokuSolver() {
    }

    public SudokuSolver(String puzzle) {
        board.readBoard(puzzle);
    }

    public SudokuSolver(SudokuBoard board) {
        this.board = board;
    }

    private boolean findSolution(SudokuBoard board, int index) {
        int row = index / board.getSize(), column = index % board.getSize();

        if (index == board.getSize() * board.getSize()) {
            return true;
        }
        if (board.isFilled(row, column)) {
            return findSolution(board, index + 1);
        }

        for (Integer value : board.getOptionsForCell(row, column)) {
            board.setCellValue(row, column, value);
            if (findSolution(board, index + 1)) {
                return true;
            }
        }
        board.clearCell(row, column);
        return false;
    }

    private boolean generate(SudokuBoard board, int index) {
        int row = index / board.getSize(), column = index % board.getSize();

        if (index == board.getSize() * board.getSize()) {
            return true;
        }
        if (board.isFilled(row, column)) {
            return generate(board, index + 1);
        }

        List<Integer> list = board.getOptionsForCell(row, column);
        Collections.shuffle(list);
        for (Integer value : list) {
            board.setCellValue(row, column, value);
            if (generate(board, index + 1)) {
                return true;
            }
        }
        board.clearCell(row, column);
        return false;
    }

    public SudokuBoard generate(int n) {
        SudokuBoard gboard = new SudokuBoard(n);
        SudokuBoard retboard = null;
        Random rand = new Random();
        int totalNeed = n * n * 2 - 1;
        int k, j;
        if (generate(gboard, 0)) {
            retboard = new SudokuBoard(n);
            SudokuBoard tstboard = new SudokuBoard(n);
            for (int i = 0; i < totalNeed; i++) {
                do {
                    k = rand.nextInt(gboard.getSize());
                    j = rand.nextInt(gboard.getSize());
                } while (retboard.isFilled(k, j));
                retboard.setCellValue(k, j, gboard.getCellValue(k, j));
                tstboard.setCellValue(k, j, gboard.getCellValue(k, j));
            }

            // see if we can solve it
            while (!findSolution(tstboard, 0)) {
                do {
                    k = rand.nextInt(gboard.getSize());
                    j = rand.nextInt(gboard.getSize());
                } while (retboard.isFilled(k, j));
                retboard.setCellValue(k, j, gboard.getCellValue(k, j));
                tstboard.setCellValue(k, j, gboard.getCellValue(k, j));
            }
        }
        return retboard;
    }

    public boolean solve() {
        return findSolution(board, 0);
    }

    public String dumpBoard() {
        return board.toString();
    }

    public String dumpBoard(String sep) {
        return board.toString(sep);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        String sep = "\n";
        if (args.length > 1) {
            sep = args[1];
        }
        String line;
        while ((line = reader.readLine()) != null) {
            SudokuSolver solver = new SudokuSolver(line);
            System.out.println("Solving: ");
            System.out.println(solver.dumpBoard(sep));
            solver.solve();
            System.out.println("Solved: \n" + solver.dumpBoard(sep));
        }
    }
}
