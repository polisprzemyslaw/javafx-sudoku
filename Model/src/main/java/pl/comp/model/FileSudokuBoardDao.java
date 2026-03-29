package pl.comp.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.comp.model.exceptions.SerialReadException;
import pl.comp.model.exceptions.SudokuDaoException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {

    String dirName;

    private static final Logger logger = LoggerFactory.getLogger(FileSudokuBoardDao.class);

    public FileSudokuBoardDao(String dirName) {
        this.dirName = dirName;
    }

    @Override
    public SudokuBoard read(String name) throws SudokuDaoException {
        SudokuBoard board;
        try (FileInputStream in = new FileInputStream(dirName + "/" + name);
             ObjectInputStream s = new ObjectInputStream(in)) {
            board = (SudokuBoard) s.readObject();

        } catch (IOException | ClassNotFoundException | ClassCastException | SerialReadException e) {
            logger.error("Read Error: {}", e.getMessage());
            throw new SudokuDaoException("error.file.read", e);
        }
        return board;
    }

    @Override
    public void write(String name, SudokuBoard board) throws SudokuDaoException {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (FileOutputStream f = new FileOutputStream(dirName + "/" + name);
             ObjectOutputStream s = new ObjectOutputStream(f)) {
            s.writeObject(board);
        } catch (IOException | ClassCastException | SerialReadException e) {
            logger.error("Write Error: {}", e.getMessage());
            throw new SudokuDaoException("error.file.write", e);
        }
    }

    @Override
    public List<String> names() {
        File dir = new File(dirName);
        List<String> names = new ArrayList<>();
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        names.add(file.getName());
                    }
                }
            }
        } else {
            logger.error("Folder nie istnieje lub nie jest folderem");
        }
        return names;
    }

    @Override
    public void close() {
        logger.info("Zamykanie plików SudokuBoard");
    }
}
