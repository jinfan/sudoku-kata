package com.brodwall.kata.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuBoard {
    public static final int BOXSIZE = 3;
    public static final int SIZE = BOXSIZE * BOXSIZE;
    private final Integer[][] board = new Integer[SIZE][SIZE];

    public boolean isFilled(int row, int column) {
        return board[row][column] != null;
    }

    public List<Integer> getOptionsForCell(int row, int column) {
        List<Integer> options = new ArrayList<Integer>(Arrays.asList(1, 2, 3,
                4, 5, 6, 7, 8, 9));
        removeOptionsInRow(options, row);
        removeOptionsInColumn(options, column);
        removeOptionsInBox(options, row, column);
        return options;
    }

    private void removeOptionsInBox(List<Integer> options, int row, int column) {
        int boxRow = row - row % BOXSIZE, boxColumn = column - column % BOXSIZE;
        for (int rowOffset = 0; rowOffset < BOXSIZE; rowOffset++) {
            for (int columnOffset = 0; columnOffset < BOXSIZE; columnOffset++) {
                options.remove(board[boxRow + rowOffset][boxColumn
                                                         + columnOffset]);
            }
        }
    }

    private void removeOptionsInColumn(List<Integer> options, int column) {
        for (int row = 0; row < SIZE; row++)
            options.remove(board[row][column]);
    }

    private void removeOptionsInRow(List<Integer> options, int row) {
        for (int column = 0; column < SIZE; column++)
            options.remove(board[row][column]);
    }

    public void setCellValue(int row, int column, int value) {
        board[row][column] = value;
    }

    public void clearCell(int row, int column) {
        board[row][column] = null;
    }

    public int getCellValue(int row, int column) {
        return board[row][column];
    }

    public String dumpBoard() {
        StringBuilder result = new StringBuilder();
        for (Integer[] row : board) {
            for (Integer values : row) {
                result.append(values != null ? values.toString() : '.');
            }
            result.append("\n");
        }
        return result.toString();
    }

    public void readBoard(String boardAsString) {
        int index = 0;
        for (Integer[] row : board) {
            for (int column = 0; column < row.length; column++) {
                row[column] = readValue(boardAsString.charAt(index++));
            }
        }
    }

    private Integer readValue(char c) {
        return c != '.' ? Integer.valueOf(c + "") : null;
    }
}
