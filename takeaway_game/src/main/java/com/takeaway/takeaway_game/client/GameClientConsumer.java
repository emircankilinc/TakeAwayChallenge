package com.takeaway.takeaway_game.client;

import java.util.Scanner;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takeaway.takeaway_game.entity.MoveState;
import com.takeaway.takeaway_game.entity.Player;

@Service
public class GameClientConsumer {

	@Autowired
	private RabbitTemplate template;

	@RabbitListener(queues = "q.game.0")
	private void consume(Player player) {
		Player nextMove = new Player();
		nextMove.setPlayerId(player.getRivalId());
		nextMove.setRivalId(player.getPlayerId());

		if (player.getMoveState().equals(MoveState.END)) {
			System.out.print("Winner : Player2");
		} else {
			Scanner scan = new Scanner(System.in); // Create a Scanner object
			System.out.print("Select your choose in {-1, 0 , 1} : ");
			String userChoice = scan.nextLine(); // Read user input
			Integer nextValue = (Integer.valueOf(player.getNextValue()) + Integer.valueOf(userChoice)) / 3;

			if (nextValue == 1) {
				nextMove.setMoveState(MoveState.END);
				this.template.convertAndSend(player.getPlayerId() + "", nextMove);
			} else {
				nextMove.setMoveState(MoveState.NEXT_MOVE);
				nextMove.setNextValue(String.valueOf(nextValue));
				this.template.convertAndSend(player.getPlayerId() + "", nextMove);
			}
		}

	}

}
