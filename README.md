# Conways Game of Life
This is a simple implementation of Conway's Game of Life. The game is a cellular automaton that simulates the life and 
death of cells on a grid based on a set of rules.

## Patterns
Here are the patterns that we used to design the game:
 - MVC Pattern: We are using a Model-View-Controller pattern to separate the concerns of the game. The model represents the state of the grid and cells, 
the view is responsible for rendering the grid and cells, and the controller handles user input and updates the model accordingly.
 - Strategy Pattern: We are using a strategy pattern to determine the next state of a cell based on its current state. 
We inject the ruleset based on an aggregated Rule object into the game.
 - Observer Pattern: We are using an observer pattern to notify the grid when a cell changes state, allowing the grid to update accordingly.
 - Builder Pattern: We are using a builder pattern to create the grid and cells, making grid setup and rule injection simpler. 

## Names
Mia Ray, Julia Aronow

## Java Version: JDK 25

## Running the Game

To run the game, run java/automata/display/GameOfLifeApp.main()

This will run with default rules, but using the builder pattern you can alter game setup.