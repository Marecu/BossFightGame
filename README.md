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