# JavaFX Sudoku Game

A desktop Sudoku application written in Java. This was a collaborative university project developed by a 2-person team. The project is built with Maven and divided into two separate modules (Model and View) to keep the game rules separated from the graphical interface.

## Features
* **Graphical Interface:** Built with JavaFX, including difficulty selection and support for 2 languages
* **Game Logic:** Board generation and puzzle solving using a backtracking algorithm.
* **Save & Load:** The game state can be saved and loaded in two ways: to a local file or to a database using JDBC.
* **Tested Code:** Core game logic and data saving mechanisms are covered by JUnit tests.

<img width="362" height="300" alt="obraz" src="https://github.com/user-attachments/assets/5f1b9632-a13e-4ae2-946a-98b3e2032767" />
<img width="360" height="150" alt="obraz" src="https://github.com/user-attachments/assets/8490d960-d8e7-4c1f-a4bd-80ddf813de48" />
<img width="358" height="400" alt="obraz" src="https://github.com/user-attachments/assets/ddf49ac2-5754-454f-86df-9554168e1cca" />
<img width="358" height="400" alt="obraz" src="https://github.com/user-attachments/assets/72ab2c85-591a-42ea-9c7c-d44d9ed4d313" />

## Project Structure
1. **Model:** This module contains the core game rules. It defines how the Sudoku board works, implements the solver algorithm, and handles data saving (DAO pattern for files and database). 
2. **View:** This module handles the JavaFX user interface. It includes controllers for various screens (main board, difficulty form, save/load menus).

## Technologies Used
* Java
* JavaFX
* Maven
* JDBC (H2 database)
* JUnit

## How to Run
1. Go to the Releases tab in this repository.
2. Download the packaged .jar file.
3. Double-click the file to launch the game (Java 25+ required)
