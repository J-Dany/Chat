DROP DATABASE IF EXISTS `chat`;
CREATE DATABASE `chat`;
USE `chat`;

CREATE TABLE `users`(
	`id_user` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`username` VARCHAR(64) NOT NULL UNIQUE KEY,
	`password` VARCHAR(255) NOT NULL,
	`name` VARCHAR(64) NOT NULL,
	`surname` VARCHAR(64) NOT NULL,
	`email` VARCHAR(64) NOT NULL UNIQUE KEY,
	`photo` VARCHAR(255)
);

CREATE TABLE `friends`(
	`id_friend` INT NOT NULL PRIMARY KEY,
	`user1` INT NOT NULL,
	`user2` INT NOT NULL,
	CONSTRAINT UNIQUE KEY `univoco`(`user1`, `user2`),
	CONSTRAINT `fk_friendsUsers1` FOREIGN KEY (`user1`) REFERENCES `users`(`id_user`),
	CONSTRAINT `fk_friendsUsers2` FOREIGN KEY (`user2`) REFERENCES `users`(`id_user`)
);

CREATE TABLE `groups`(
	`id_group` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`group_name` VARCHAR(255) NOT NULL
);

CREATE TABLE messages(
	`id_message` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`data` DATETIME NOT NULL,
	`message` VARCHAR(512) NOT NULL,
	`id_group` INT,
	`sender` INT NOT NULL,
	`addresse` INT,
	CONSTRAINT `fk_messagesGroups` FOREIGN KEY (`id_group`) REFERENCES `groups`(`id_group`),
	CONSTRAINT `fk_messagesUsersSender` FOREIGN KEY (`sender`) REFERENCES `users`(`id_user`),
	CONSTRAINT `fk_messagesUsersAddresse` FOREIGN KEY (addresse) REFERENCES `users`(`id_user`)
);

CREATE TABLE `partecipants`(
	`id_group` INT NOT NULL,
	`username` VARCHAR(64) NOT NULL,
	CONSTRAINT `pk_Partecipants` PRIMARY KEY(`id_group`, `username`),
	CONSTRAINT `fk_partecipantsUsers` FOREIGN KEY (`username`) REFERENCES `users`(`username`),
	CONSTRAINT `fk_partecipantsGroups` FOREIGN KEY (`id_group`) REFERENCES `groups`(`id_group`)
);