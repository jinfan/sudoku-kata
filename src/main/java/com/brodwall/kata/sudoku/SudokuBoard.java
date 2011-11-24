package com.brodwall.kata.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuBoard {
    private static final int DEFAULT_BOXSIZE = 3;
    private static final int DEFAULT_SIZE = DEFAULT_BOXSIZE * DEFAULT_BOXSIZE;

    private int boxSize = DEFAULT_BOXSIZE;
    private int size = DEFAULT_SIZE;

    private Integer[][] board;

    public SudokuBoard(int boxSize) {
        super();
        this.boxSize = boxSize;
        initSize();
    }

    public SudokuBoard() {
        super();
        initSize();
    }

    private void initSize() {
        this.size = this.boxSize * this.boxSize;
        board = new Integer[size][size];
    }

    public int getSize() {
        return size;
    }

    public int getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(int boxSize) {
        this.boxSize = boxSize;
        initSize();
    }

    public boolean isFilled(int row, int column) {
        return board[row][column] != null;
    }

    public List<Integer> getOptionsForCell(int row, int column) {
        List<Integer> options = createOptions(size);
        removeOptionsInRow(options, row);
        removeOptionsInColumn(options, column);
        removeOptionsInBox(options, row, column);
        return options;
    }

    private List<Integer> createOptions(int m) {
        List<Integer> options = new ArrayList<Integer>(m);
        for (int i = 1; i <= m; i++) {
            options.add(i);
        }

        return options;
    }

    private void removeOptionsInBox(List<Integer> options, int row, int column) {
        int boxRow = row - row % boxSize, boxColumn = column - column % boxSize;
        for (int rowOffset = 0; rowOffset < boxSize; rowOffset++) {
            for (int columnOffset = 0; columnOffset < boxSize; columnOffset++) {
                options.remove(board[boxRow + rowOffset][boxColumn
                        + columnOffset]);
            }
        }
    }

    private void removeOptionsInColumn(List<Integer> options, int column) {
        for (int row = 0; row < size; row++) {
            options.remove(board[row][column]);
        }
    }

    private void removeOptionsInRow(List<Integer> options, int row) {
        for (int column = 0; column < size; column++) {
            options.remove(board[row][column]);
        }
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
        int length = boardAsString.length();
        int mysize = (int) Math.sqrt(Math.sqrt(length));
        this.setBoxSize(mysize);

        int index = 0;
        for (Integer[] row : board) {
            for (int column = 0; column < row.length; column++) {
                row[column] = readValue(boardAsString.charAt(index++));
            }
        }
    }

    private Integer readValue(char c) {
        return c != '.' && c != '0' ? Integer.valueOf(c + "") : null;
    }

}
