# Checkers with a Scoreboard

This project runs a checkers game that connects to a scoreboard with java sockets.
During the game the scoreboard updates to show the active piece count, as well as the number of kings.

The piece designs are hand drawn, and I modified and added sounds to each action.

> To run the project, run `Scoreboard`/`Scoreboard.java`, then `Checkers`/`Checkers.java`

## Folder Structure

- `src`: contains `Checkers`, `Images`, `Scoreboard`, and `Sounds` folders
- `Checkers`: contains all the files related to the game
    - `Checkers.java`: the main class that runs the checkers game (must be run after `Scoreboard.java`)
    - `Board.java`: The brain of the game, reads user inputs on the board and communicates to the scoreboard
    - `Data.java`: This class stores the state of the game
    - `movesMade.java`: This class converts a 'move' into this datatype
    - `Sound.java`: The player of the sound files
- `Images`: contains all images for the board
- `Scoreboard`/`Scoreboard.java`: contains the code for the scoreboard
- `Sounds`: contains all sound files used for the game