package com.brodwall.kata.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SudokuSolver {
    private SudokuBoard board = new SudokuBoard();
    int size;

    public SudokuSolver(String puzzle) {
        board.readBoard(puzzle);
        size = board.getSize();
    }

    public SudokuSolver(SudokuBoard board) {
        this.board = board;
        size = board.getSize();
    }

    private boolean findSolution(SudokuBoard board, int index) {
        int row = index / size, column = index % size;

        if (index == size * size) {
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
