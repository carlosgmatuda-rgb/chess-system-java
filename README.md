# Chess System

A Java console chess game, built as part of Nelio Alves' Java OOP course. The project implements a full chess match with move validation, check/checkmate detection and special moves, applying object-oriented programming concepts such as inheritance, abstraction and encapsulation.

## How it works

The application prints the board to the console and lets two players take turns entering the source and target positions of a move (e.g. `e2` to `e4`). After each move, the board is redrawn along with the captured pieces, whose turn it is, and whether a player is in check or checkmate.

### Features

- Full chess board with all pieces: **King, Queen, Rook, Bishop, Knight, Pawn**
- Move validation for each piece type
- Check and checkmate detection
- Special moves: **castling**, **en passant**, and **pawn promotion**
- Move history with captured pieces list
- Input validation with custom exceptions for invalid moves and positions

## Project architecture

The project is organized in two layers:

- **Board layer** — generic, game-agnostic classes that represent any board game: `Board`, `Piece`, `Position`, `BoardException`.
- **Chess layer** — chess-specific classes built on top of the board layer: `ChessMatch`, `ChessPiece`, `ChessPosition`, the piece subclasses (`King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn`), the `Color` enum, and `ChessException`.

This separation keeps the board logic reusable and independent from chess rules.

## How to run

```bash
javac *.java
java Program
```

## Technologies

- Java
