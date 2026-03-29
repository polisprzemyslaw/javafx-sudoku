package pl.comp.view;


import pl.comp.model.SudokuBoard;

enum DifficultyLevel {
    EASY(15),
    MEDIUM(25),
    HARD(40);

    private int fieldNumber;

    DifficultyLevel(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public void removeFields(SudokuBoard board) {

        int removed = 0;

        while (removed < this.fieldNumber) {
            int x = (int) (Math.random() * 9);
            int y = (int) (Math.random() * 9);
            if (board.getField(x,y) != 0) {
                board.setField(x, y, 0);
                removed++;
            }
        }
    }
}
