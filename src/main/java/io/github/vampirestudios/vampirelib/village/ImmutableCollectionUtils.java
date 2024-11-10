/*
 * Copyright (c) 2024 OliviaTheVampire
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

package io.github.vampirestudios.vampirelib.village;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ImmutableCollectionUtils {
	public static <T> Set<T> getAsMutableSet(Supplier<Set<T>> getter, Consumer<Set<T>> setter) {
		Set<T> set = getter.get();

		if (!(set instanceof HashSet)) {
			setter.accept(set = new HashSet<>(set));
		}

		return set;
	}

	public static <T> List<T> getAsMutableList(Supplier<List<T>> getter, Consumer<List<T>> setter) {
		List<T> set = getter.get();

		if (!(set instanceof ArrayList)) {
			setter.accept(set = new ArrayList<>(set));
		}

		return set;
	}

	public static <K, V> Map<K, V> getAsMutableMap(Supplier<Map<K, V>> getter, Consumer<Map<K, V>> setter) {
		Map<K, V> map = getter.get();

		if (!(map instanceof HashMap)) {
			setter.accept(map = new HashMap<>(map));
		}

		return map;
	}
}