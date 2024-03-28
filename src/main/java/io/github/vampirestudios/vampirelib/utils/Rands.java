/*
 * Copyright (c) 2023 OliviaTheVampire
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.vampirestudios.vampirelib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Rands {

	private static final Random rand = new Random();

	public static Random getRandom() {
		return rand;
	}

	public static int randInt(int bound) {
		return rand.nextInt(bound);
	}

	public static int randIntRange(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}

	public static float randFloatRange(float min, float max) {
		return min + rand.nextFloat() * (max - min);
	}

	public static float randFloat(float bound) {
		return ((float) rand.nextInt((int) (bound * 10) + 1)) / 10;
	}

	public static Color randColor() {
		return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}

	public static boolean chance(int bound) {
		return randInt(bound) == 0;
	}

	public static <O extends Object> O values(O[] values) {
		return values[randInt(values.length)];
	}

	public static <O extends Object> O list(List<O> list) {
		return list.get(randInt(list.size()));
	}

	public static <K, V extends Object> Map.Entry<K, V> map(Map<K, V> map) {
		Set<Map.Entry<K, V>> entry = map.entrySet();
		return new ArrayList<>(entry).get(randInt(entry.size()));
	}
}
