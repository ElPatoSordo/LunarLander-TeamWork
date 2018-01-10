# LunarLander-TeamWork
This project contains:
+ Two HTML files that correspond with the login and the game.
+ Css files to style both pages.
+ JS: with javascript/jquery/ajax that covers all the requirements of the project.
+ Java classes (entities) created with JPA libraries to connect and work with a data base.
+ Optimized images

Game link: http://35.205.136.41/LunarLanderTeamWork/game.html

Tasks developed:

## Register system:
+ The login page allows the user to create an account by entering a username, name, email and password. Then, the user can log in with his/her credentials and play the game.

## Games played record
+ Each game the player plays will be registered in the data base in a table called "Score". The record will contain the configuration's id (this configuration contains the user's id), the begin and end time of the game and the final speed apart from the score's id.

## PAAS
+ The app is deployed in a PAAS. We used Google Cloud's service to host a virtual machine with tomcat.

## Documentation
+ This project contains a CHANGELOG file, which contains information about the different versions of the project and a README file, which contains general information about the project.

## Spaceship:
+ Pressing the space bar or touching the screen of the mobile changes the spaceship to *engine on* state and changes the acceleration from g to -g, and when releasing any of the two, the spaceship returns to *engine off* state.
+ The position of the ship (in percentage of the screen) varies depending on the speed and is a function of acceleration.
+ Pressing the spacebar or touching the screen of the mobile empties the fuel tank proportionally to the time that we keep pressing the propeller.
+ When touching the surface of the Moon, you can see if the impact speed is lower than a threshold value. If so, a congratulatory message is shown, if not, the ship explodes. In both cases the game ends and can be restarted.

## Menu:
+ Among other things, the menu includes a button to start a new game. This will cause the ship to start to fall and restart everything to its initial values if you have played before.
+ By pressing the "Configuration" button you can access the difficulty selector and change it by pressing the button that appears.
+ In the desktop version a button has been placed that pauses and resumes the game.
+ There is a  *Instructions* window and another one for the *About* section accessible from the menu.
