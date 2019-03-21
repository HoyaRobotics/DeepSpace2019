# Hoya Robotics Deep Space 2019
This is the code powering the Destination: Deep Space robot built by Hoya Robotics (Team 4152).

You are free to use any code snippets you find in your own project!
If you do, consider sending us an email at hoyarobotics@gmail.com to let us know.

## Authors
Oliver Byl

Nolan Meehan

## Structure
Each subsystem on the robot (driving, cargo, hatches, etc) are split into their own class with their own periodic methods. These methods are then called from the Robot class.

In the ValueMap class and at the top of various specific system classes you will find constants representing customizable robot features, such as button mappings and motor speeds. They are located there so as to allow easy changes to the robot, even by non-programmers.