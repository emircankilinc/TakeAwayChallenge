# Game Of Three - Coding Challenge Takeaway

The Goal is to implement a game with two independent units – the players –communicating with each other using an API.

# Description 

When a player starts, it incepts a random (whole) number and sends it to the second
player as an approach of starting the game. The receiving player can now always choose
between adding one of {1, 0, 1} to get to a number that is divisible by 3. Divide it by three. The
resulting whole number is then sent back to the original sender.
The same rules are applied until one player reaches the number 1(after the division).
See example below.For each "move", a sufficient output should get generated (mandatory: the added, and
the resulting number). Both players should be able to play automatically without user input. The
type of input (manual, automatic) should be optionally adjustable by the player.

# Using Technologies And Programming Languages

Spring Framework,Java,RabbitMq(CloudAmqp)

# Guide

takeaway_game project -> server side

takeaway_game_client project -> client side


->Takeaway_game project must run so all rest services will start running,

->Each player must run takeaway_game_client to play game,

->Active players,register service and communicate logic between clients over server side whic is named as takeaway_game app,

->After takeaway_game_client app run, player register game and waiting active users,

->If there is at least one active users instead of current player, the game will be start if current player wants to play another active player,

->While playing gaming communication is ensured without interruption thanks to queues,

->When a winner is declared ,both of players will know the winner,

# Project Successes

->There is no player count limit because of creating and binding generic queue,

->Each player can play the other active players,

->Using CloudAmqp instead of local RabbitMq,

->If server side service url wants to be changed, it will only be sufficient to change the variable named as game.url 
  which is located in  appplication.properties file,
  
