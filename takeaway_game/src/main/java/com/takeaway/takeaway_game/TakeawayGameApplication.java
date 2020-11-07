package com.takeaway.takeaway_game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.takeaway.takeaway_game.server.RabbitMqExample;

@SpringBootApplication
public class TakeawayGameApplication implements CommandLineRunner {

	@Autowired
	private RabbitMqExample exam;

	public static void main(String[] args) {
		SpringApplication.run(TakeawayGameApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		exam.producer();
	}

}
