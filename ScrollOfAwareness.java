/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import java.util.ArrayList;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollableWindow;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfAwareness extends Scroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_FORESIGHT;
		image = ItemSpriteSheet.SCROLL_YNGVI;
		unique = true;
	}
	
	private void addText(String caption, ArrayList<? extends Item> items, StringBuilder builder) {
		if (!items.isEmpty()) {
			builder.append(caption + ":\n");

			for (Item item : items) {
				String cursed_status = "";
				String level = "";
				String quantity = "";

				if (item.cursed && !(item instanceof Armor || item instanceof Weapon)) cursed_status = "cursed ";
				if (item.level() > 0) level = " +" + Integer.toString(item.level());
				if (item.quantity() > 1) quantity = " x" + Integer.toString(item.quantity());
				
				builder.append("- " + cursed_status + item.name().toLowerCase() + level + quantity + "\n");
			}

			builder.append("\n");
		}
	}

	@Override
	public void doRead() {
		StringBuilder builder = new StringBuilder();

		ArrayList<Scroll> scrolls = new ArrayList<>();
		ArrayList<Potion> potions = new ArrayList<>();
		ArrayList<Item> equipment = new ArrayList<>();
		ArrayList<Wand> wands = new ArrayList<>();
		ArrayList<Item> others = new ArrayList<>();

		for (Item item : Dungeon.level.generatedItems) {
			if (item instanceof Scroll)
				scrolls.add((Scroll) item);

			else if (item instanceof Potion)
				potions.add((Potion) item);

			else if (item instanceof MeleeWeapon || item instanceof Armor || item instanceof Ring)
				equipment.add(item);
			
			else if (item instanceof Wand)
				wands.add((Wand) item);
			
			else
				others.add(item);
		}

		if (Ghost.Quest.spawned && Ghost.Quest.depth == Dungeon.depth) {
			ArrayList<Item> rewards = new ArrayList<>();
			rewards.add(Ghost.Quest.armor);
			rewards.add(Ghost.Quest.weapon);

			addText("_Ghost quest rewards_", rewards, builder);
		}

		if (Wandmaker.Quest.spawned && Wandmaker.Quest.depth == Dungeon.depth) {
			ArrayList<Item> rewards = new ArrayList<>();
			rewards.add(Wandmaker.Quest.wand1);
			rewards.add(Wandmaker.Quest.wand2);

			builder.append("_Wandmaker quest item_: ");

			switch (Wandmaker.Quest.type) {
				case 1: default:
					builder.append("corpse dust\n\n");
					break;
				case 2:
					builder.append("fresh embers\n\n");
					break;
				case 3:
					builder.append("rotberry seed\n\n");
			}

			addText("_Wandmaker quest rewards_", rewards, builder);
		}

		if (Imp.Quest.spawned && Imp.Quest.depth == Dungeon.depth) {
			ArrayList<Ring> rewards = new ArrayList<>();
			rewards.add(Imp.Quest.reward);

			addText("_Imp quest reward_", rewards, builder);
		}

		addText("_Scrolls_", scrolls, builder);
		addText("_Potions_", potions, builder);
		addText("_Equipment_", equipment, builder);
		addText("_Wands_", wands, builder);
		addText("_Other_", others, builder);

		if (builder.length() == 0) {
			GLog.i("No items found on this level");
		} else {
			builder.setLength(builder.length() - 1); // remove trailing newline
			GameScene.show(new ScrollableWindow(builder.toString()));
		}

		collect();

		Sample.INSTANCE.play( Assets.Sounds.READ );
	}

	@Override
	public String name() {
        return "Scroll of Awareness";
    }

    @Override
	public String desc() {
        StringBuilder builder = new StringBuilder();

        builder.append("This scroll will show you all the generated items in the current floor ");
		builder.append("and any quest rewards. ");
		builder.append("This scroll won't be consumed after being read.");

		return builder.toString();
    }

    @Override public boolean isIdentified() {
        return true;
    }

    @Override public boolean isKnown() {
		return true;
	}
	
}
