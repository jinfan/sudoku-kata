package com.brodwall.kata.sudoku;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class SudokuSolverTest {
    private final SudokuBoard board = mock(SudokuBoard.class);
    private SudokuSolver solver;

    @Before
    public void allCellsAreFilled() {
        when(board.getSize()).thenReturn(9);
        when(board.isFilled(anyInt(), anyInt())).thenReturn(true);
        solver = new SudokuSolver(board);
    }

    @Test
    public void shouldFindSolutionToFilledBoard() {
        assertThat(solver.solve()).isTrue();
    }

    @Test
    public void shouldNotFindSolutionWhenCellHasNoOptions() throws Exception {
        when(board.isFilled(8, 8)).thenReturn(false);
        when(board.getOptionsForCell(8, 8)).thenReturn(noOptions());
        assertThat(solver.solve()).isFalse();
    }

    @Test
    public void shouldFindSolutionWhenCellHasOneOption() throws Exception {
        when(board.isFilled(8, 8)).thenReturn(false);
        when(board.getOptionsForCell(8, 8)).thenReturn(oneOption(3));
        assertThat(solver.solve()).isTrue();
        verify(board).setCellValue(8, 8, 3);
    }

    @Test
    public void shouldBacktrackWhenNoOptionsInFutureCell() throws Exception {
        when(board.isFilled(7, 8)).thenReturn(false);
        when(board.getOptionsForCell(7, 8)).thenReturn(options(1, 2, 3));
        when(board.isFilled(8, 8)).thenReturn(false);
        when(board.getOptionsForCell(8, 8)).thenReturn(noOptions()).thenReturn(
                oneOption(1));

        assertThat(solver.solve()).isTrue();

        verify(board).setCellValue(7, 8, 2);
        verify(board).setCellValue(8, 8, 1);
        verify(board, never()).setCellValue(7, 8, 3);
    }

    @Test
    public void shouldClearCellWhenBacktracking() throws Exception {
        when(board.isFilled(7, 8)).thenReturn(false);
        when(board.getOptionsForCell(7, 8)).thenReturn(options(1));
        when(board.isFilled(8, 8)).thenReturn(false);
        when(board.getOptionsForCell(8, 8)).thenReturn(noOptions());

        assertThat(solver.solve()).isFalse();

        InOrder order = inOrder(board);
        order.verify(board).setCellValue(7, 8, 1);
        order.verify(board).clearCell(7, 8);
    }

    @Test
    public void shouldSolveCompletePuzzle() throws Exception {
        String puzzle = "..3.2.6..9..3.5..1..18.64....81.29..7.......8..67.82....26.95..8..2.3..9..5.1.3..";
        SudokuSolver solver = new SudokuSolver(puzzle);
        solver.solve();

        String[] lines = solver.dumpBoard().split("\n");
        assertThat(lines).hasSize(9);
        for (String line : lines) {
            assertThat(line).matches("[1-9]{9}");
        }
    }

    @Test
    public void generatePuzzle() {
        SudokuSolver solver = new SudokuSolver();
        SudokuBoard b1 = solver.generate(3);
        assertThat(b1 != null);
        System.out.println(b1.toString(""));
    }
    private List<Integer> options(Integer... options) {
        return Arrays.asList(options);
    }

    private List<Integer> oneOption(int option) {
        return Arrays.asList(option);
    }

    private List<Integer> noOptions() {
        return new ArrayList<Integer>();
    }
}
