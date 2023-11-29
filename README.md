# Untitled Boss Fight Game

## by Marcus Thoen


## Proposal
This project is intended to be a game where a player fights a series of custom-made bosses sequentially.
The player will have to attack the boss (which will have a fixed amount of health points) while navigating
around the attacks performed by the boss. Each boss will have a unique move set, providing a different
challenge to the player. <br> <br>
Current specifications (subject to change):
- The player will have **2 attacks**
  - 1 basic attack (usable anytime)
  - 1 ability attack (usable after a certain amount of player-issued attacks)
    - This attack will fire a barrage of projectiles, increasing in quantity based on the amount of attacks issued
        since the last cast (this is the complex list implementation - both projectiles and the overall group of them 
        will require non-trivial methods)
- The player will have **3 health points (HP)**, recovered fully after each fight
  - Boss attacks that connect with the player will damage the player for 1 HP
- Bosses will have **3 attacks** each
  - Attack patterns will be entirely randomized

The intended audience for this project is people who enjoy playing video games. This project is of interest
to me because I enjoy playing video games and because I am interested in game design as a prospective career.

## User Stories
- As a user, I want to be able to move my character left and right
- As a user, I want to be able to jump
- As a user, I want to be able to use my basic attack
- As a user, I want to be able to increment the amount of attacks issued counter when using a basic attack
- As a user, I want to be able to see the amount of attacks issued since the last ability attack
  (alternatively, the amount of attacks still required to be able to use the ability attack)
- As a user, I want to be able to use my ability attack once the appropriate amount of attacks have been issued
- As a user, I want the number of projectiles in my ability attack to increase with the amount of attacks issued since
    the last cast.
- As a user, I want the boss to be able to use its attacks
- As a user, I want to have an indication or telegraph that the boss is about to use a specific attack
- As a user, I want to be able to see my remaining HP
- As a user, I want to have the option to save the current game state
- As a user, I want to have the option to load a game state from a saved file

## Instructions for Grader
You can perform the first action related to adding Xs to a Y as follows:

    1) Press "ENTER" to issue an attack.

    2) You will be able to see the stationary player attack (X) you added to the list of player attacks (Y).

You can perform the second action related to adding Xs to a Y as follows:

    1) Hit the boss 3 total times with the attack outlined in Action 1.
    
    2) Press "E" to issue a spell attack. You will be able to see the moving player attack (X) added to the list of player attacks (Y).

The visual component is visible immediately upon game start. The entire game is represented visually.

You can save the game by pressing "ESC" (while actively in the game) to open the pause menu and selecting "Save and quit".

You can load a saved game by pressing the "Load Game" button on the home screen when the application is run.

## Phase 4: Task 2

    Fri Nov 24 13:02:09 PST 2023 - Added basic attack at x: 616, y: 510.5
    
    Fri Nov 24 13:02:09 PST 2023 - Removed basic attack at x: 616.0, y: 510.5
    
    Fri Nov 24 13:02:09 PST 2023 - Added basic attack at x: 609, y: 510.5
    
    Fri Nov 24 13:02:09 PST 2023 - Removed basic attack at x: 609.0, y: 510.5
    
    Fri Nov 24 13:02:10 PST 2023 - Added basic attack at x: 519, y: 510.5
    
    Fri Nov 24 13:02:10 PST 2023 - Removed basic attack at x: 519.0, y: 510.5
    
    Fri Nov 24 13:02:10 PST 2023 - Added basic attack at x: 438, y: 510.5
    
    Fri Nov 24 13:02:10 PST 2023 - Removed basic attack at x: 438.0, y: 510.5
    
    Fri Nov 24 13:02:11 PST 2023 - Added basic attack at x: 356, y: 510.5
    
    Fri Nov 24 13:02:11 PST 2023 - Removed basic attack at x: 356.0, y: 510.5
    
    Fri Nov 24 13:02:11 PST 2023 - Added basic attack at x: 229, y: 510.5
    
    Fri Nov 24 13:02:12 PST 2023 - Removed basic attack at x: 229.0, y: 510.5
    
    Fri Nov 24 13:02:13 PST 2023 - Added basic attack at x: 269, y: 510.5
    
    Fri Nov 24 13:02:14 PST 2023 - Removed basic attack at x: 269.0, y: 510.5
    
    Fri Nov 24 13:02:14 PST 2023 - Added basic attack at x: 508, y: 510.5
    
    Fri Nov 24 13:02:14 PST 2023 - Removed basic attack at x: 508.0, y: 510.5
    
    Fri Nov 24 13:02:15 PST 2023 - Added basic attack at x: 649, y: 510.5
    
    Fri Nov 24 13:02:15 PST 2023 - Removed basic attack at x: 649.0, y: 510.5
    
    Fri Nov 24 13:02:16 PST 2023 - Added spell attack at x: 897, y: 515.5
    
    Fri Nov 24 13:02:16 PST 2023 - Added basic attack at x: 850, y: 510.5
    
    Fri Nov 24 13:02:16 PST 2023 - Removed basic attack at x: 850.0, y: 510.5
    
    Fri Nov 24 13:02:17 PST 2023 - Added basic attack at x: 1054, y: 481.25
    
    Fri Nov 24 13:02:17 PST 2023 - Removed basic attack at x: 1054.0, y: 481.25
    
    Fri Nov 24 13:02:18 PST 2023 - Added spell attack at x: 795, y: 505.5
    
    Fri Nov 24 13:02:18 PST 2023 - Added basic attack at x: 795, y: 510.5
    
    Fri Nov 24 13:02:18 PST 2023 - Removed basic attack at x: 795.0, y: 510.5
    
    Fri Nov 24 13:02:19 PST 2023 - Added spell attack at x: 666, y: 515.5
    
    Fri Nov 24 13:02:19 PST 2023 - Added basic attack at x: 666, y: 510.5
    
    Fri Nov 24 13:02:19 PST 2023 - Removed basic attack at x: 666.0, y: 510.5
    
    Fri Nov 24 13:02:19 PST 2023 - Removed spell attack at x: -3103.0, y: 515.5
    
    Fri Nov 24 13:02:20 PST 2023 - Added spell attack at x: 539, y: 515.5
    
    Fri Nov 24 13:02:20 PST 2023 - Added basic attack at x: 543, y: 510.5
    
    Fri Nov 24 13:02:20 PST 2023 - Removed basic attack at x: 543.0, y: 510.5
    
    Fri Nov 24 13:02:20 PST 2023 - Added basic attack at x: 419, y: 510.5
    
    Fri Nov 24 13:02:20 PST 2023 - Removed basic attack at x: 419.0, y: 510.5
    
    Fri Nov 24 13:02:21 PST 2023 - Added basic attack at x: 313, y: 510.5
    
    Fri Nov 24 13:02:21 PST 2023 - Removed basic attack at x: 313.0, y: 510.5
    
    Fri Nov 24 13:02:21 PST 2023 - Added basic attack at x: 197, y: 510.5
    
    Fri Nov 24 13:02:21 PST 2023 - Removed spell attack at x: 4795.0, y: 505.5
    
    Fri Nov 24 13:02:22 PST 2023 - Added spell attack at x: 199, y: 515.5
    
    Fri Nov 24 13:02:22 PST 2023 - Removed basic attack at x: 197.0, y: 510.5
    
    Fri Nov 24 13:02:22 PST 2023 - Removed spell attack at x: 4666.0, y: 515.5
    
    Fri Nov 24 13:02:22 PST 2023 - Added basic attack at x: -68, y: 490.75
    
    Fri Nov 24 13:02:22 PST 2023 - Removed basic attack at x: -68.0, y: 490.75
    
    Fri Nov 24 13:02:23 PST 2023 - Removed spell attack at x: 4539.0, y: 515.5
    
    Fri Nov 24 13:02:23 PST 2023 - Added basic attack at x: 263, y: 463.0
    
    Fri Nov 24 13:02:24 PST 2023 - Removed basic attack at x: 263.0, y: 463.0
    
    Fri Nov 24 13:02:24 PST 2023 - Added basic attack at x: 358, y: 510.5
    
    Fri Nov 24 13:02:24 PST 2023 - Removed basic attack at x: 358.0, y: 510.5
    
    Fri Nov 24 13:02:25 PST 2023 - Removed spell attack at x: 4199.0, y: 515.5
    
    Fri Nov 24 13:02:25 PST 2023 - Added basic attack at x: 831, y: 481.25
    
    Fri Nov 24 13:02:26 PST 2023 - Removed basic attack at x: 831.0, y: 481.25
    
    Fri Nov 24 13:02:27 PST 2023 - Added basic attack at x: 1102, y: 510.5
    
    Fri Nov 24 13:02:27 PST 2023 - Added spell attack at x: 1108, y: 515.5
    
    Fri Nov 24 13:02:27 PST 2023 - Removed basic attack at x: 1102.0, y: 510.5

## Phase 4: Task 3

When reviewing my UML Diagram, I realized that it would have been beneficial to implement the
singleton design pattern in a few cases. Notably, the Game class could be refactored to use this
design pattern. All 4 JPanels I use could be designed this way as well. This would greatly reduce the
amount of coupling in the code and avoid the need to pass around object references to a majority of the
classes in the program.