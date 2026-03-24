# Conways Game of Life
This is a simple implementation of Conway's Game of Life. The game is a cellular automaton that simulates the life and 
death of cells on a grid based on a set of rules.

## Patterns
Here are the patterns that we used to design the game:

 - Factory Pattern: We are using a factory pattern to create cells, decoupling the creation of cells from the cell class.
 - Strategy Pattern: We are using a strategy pattern to determine the next state of a cell based on its current state. 
We inject the ruleset based on an aggregated Rule object into the grid.
 - Observer Pattern: We are using an observer pattern to notify the grid when a cell changes state, allowing the grid to update accordingly.
 - Builder Pattern: We are using a builder pattern to create the grid and cells, making grid setup and rule injection simpler. 

