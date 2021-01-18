package model;

import java.util.ArrayList;

public class Universe {

	private static ArrayList<Character> characters = new ArrayList<Character>();
	private static ArrayList<Character> monsters = new ArrayList<Character>();

	public static ArrayList<Character> getCharacters() {
		return characters;
	}
	
	public static ArrayList<Character> getMonsters() {
		return monsters;
	}

	public static void creation() {
		
		Character human = new Character();
		human.setName("Link");
		human.setHpMax(180);
		human.setAttack(40);
		human.setDodgeProbability(0.2);
		characters.add(human);
		
		Character elf = new Character();
		elf.setName("Zelda");
		elf.setHpMax(120);
		elf.setAttack(45);
		elf.setDodgeProbability(0.25);
		characters.add(elf);
		
		Character gerudo = new Character();
		gerudo.setName("Urbosa");
		gerudo.setHpMax(210);
		gerudo.setAttack(35);
		gerudo.setDodgeProbability(0.15);
		characters.add(gerudo);

		Character bokoblin = new Character();
		bokoblin.setName("Bokoblin");
		bokoblin.setHpMax(20);
		bokoblin.setAttack(2);
		bokoblin.setDodgeProbability(0.5);
		monsters.add(bokoblin);
		
		Character gardien = new Character();
		gardien.setName("Gardien");
		gardien.setHpMax(80);
		gardien.setAttack(10);
		gardien.setDodgeProbability(0.1);
		monsters.add(gardien);
		
		Character bokoblin2 = new Character();
		bokoblin2.setName("Bokoblin");
		bokoblin2.setHpMax(20);
		bokoblin2.setAttack(2);
		bokoblin2.setDodgeProbability(0.5);
		monsters.add(bokoblin2);
		
		Character gardien2 = new Character();
		gardien2.setName("Gardien");
		gardien2.setHpMax(80);
		gardien2.setAttack(10);
		gardien2.setDodgeProbability(0.1);
		monsters.add(gardien2);
		

		Character ganon = new Character();
		ganon.setName("Ganon");
		ganon.setHpMax(200);
		ganon.setAttack(16);
		ganon.setDodgeProbability(0.35);
		monsters.add(ganon);

	}

}
