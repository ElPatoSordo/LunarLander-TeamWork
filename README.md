# LunarLander-TeamWork
This project contains:
* HTML with the elements of the game.
* Css: three versions that load by media query depending on the screen size.
* JS: with javascript that covers all the requirements of the project.
* Optimized images

Game link: http://35.205.136.41/LunarLanderTeamWork/game.html

Tasks developed:

## General:
* Background, image of the ship and Luna have been placed. A fixed ground has been placed on the screen to be seen on any type of device. The images have been optimized. Different sizes and shapes of backgrounds will be loaded depending on the device using CSS.
* Pages work in different search engines, such as Internet Explorer, Mozilla Firefox and Google Chrome.

## Ship:
* Pressing the space bar or touching the screen of the mobile changes the ship to *ship with engine turned on* and changes the acceleration of ga -g, and when releasing any of the two, the ship returns to look like *ship with engine off*.
* The value of g has been changed to 4 for the game to be faster.
* The position of the ship (in percentage of the screen) varies depending on the speed and is a function of acceleration.
* Pressing the spacebar or touching the screen of the mobile empties the fuel tank proportionally to the time that we keep pressed the propeller.
* It has been chosen to eliminate the functionality of turning on the engine when clicking, so that it will only do so when pressing the spacebar or when touching the screen.
* It has been controlled that the ship "goes off" when it runs out of fuel and when it reaches the ground at the right speed.
* The reference system has been inverted, that is, the speed of the ship when it goes down will be positive and the height will decrease until it reaches 0.
* When touching the surface of the Moon, you can see if the impact speed is lower than a threshold value, if yes, a congratulatory message is shown, if not, the ship explodes. In both cases the game ends and can be restarted with the menu option *New game*.
* Threshold values: 1m / s in difficult mode, 5m / s in medium mode, 7m / s in easy mode.

## Menu:
* A menu has been created on the side of the screen for the desktop version and a menu that occupies 100% of the space available for the mobile.
* Among other things, the menu includes a button to start a new game. This will cause the ship to start to fall and restart everything to its initial values if you have played before.
* By pressing the "Configuration" button you can access the difficulty selector and change it by pressing the button that appears.
* In the desktop version a button has been placed that pauses and resumes the game by pressing the "P" key. This button is not shown in the mobile version since it has one that shows the menu and pauses the game, and within it has a button to resume the game and hide that menu.
* In the mobile version, the menu is displayed when the page is loaded. When you open the configuration, the menu is hidden and when it is hidden it is displayed again, so that they do not overlap.
* There is a page of * Instructions * and a page of * About * accessible from the menu with their respective buttons to return to the initial page.
