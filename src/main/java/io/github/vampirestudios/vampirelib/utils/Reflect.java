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

package io.github.vampirestudios.vampirelib.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class Reflect {
	public static Class<?> getInnerClass(Class<?> clazz, String name) {
		Class<?>[] declaredClasses = clazz.getDeclaredClasses();
		for (Class<?> c : declaredClasses) {
			if (c.getName().equalsIgnoreCase(name)) {
				return c;
			}
		}
		return null;
	}

	public static <T> T constructClass(Class<T> clazz, Object... args) {
		Constructor<T>[] cap = (Constructor<T>[]) clazz.getDeclaredConstructors();
		for (Constructor<T> c : cap) {
			Class<?>[] types = c.getParameterTypes();
			boolean match = true;
			for (int t = 0; t < types.length; t++) {
				Class<?> c_type = types[t];
				if (args[t] == null)
					continue;
				if (isInstanceOf(c_type, args[t].getClass()))
					continue;
				match = false;
				break;
			}
			if (match) {
				c.setAccessible(true);
				try {
					return c.newInstance(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/*public static Object getMemberByType(Class field_class, Class object_class)
	{
		return getMemberByType(field_class, object_class, null);
	}*/

	public static Field getFieldByType(Class<?> object_class, Class<?> field_class) {
		Field[] fields = object_class.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			if (isInstanceOf(f.getType(), field_class))
				return f;
		}
		return null;
	}


	public static <T> T getMemberByType(Class<?> object_class, Class<T> field_class, Object object) {
		Field f = getFieldByType(object_class, field_class);
		try {
			if (f != null) {
				return (T) f.get(object);
			}
		} catch (Exception ignored) {
		}
		return null;

		/*Field[] fields =object_class.getDeclaredFields();
		for(int i =0; i < fields.length; i++)
		{
			Field f = fields[i];
			f.setAccessible(true);
			if(f.getType().equals(field_class))
			{
				try
				{
					/*if (Modifier.isStatic(f.getModifiers()))
					{
						return f.get(null);
					}else*/
					/*{
						return f.get(object);
					}
				} catch (Exception e)
				{
				}
			}
		}
		return null;*/
	}


	public static <T> T[] getMemberArrayByType(Class<T> type, Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			if (f.getType().isArray()) {
				Class<?> ofArray = f.getType().getComponentType();
				if (isInstanceOf(ofArray, type)) {
					try {
						return (T[]) f.get(object);
					} catch (Exception ignored) {

					}
				}
			}
		}
		return null;
	}

	public static boolean isInstanceOf(Class<?> clazz, Class<?> possibleInstance) {
		if (clazz.isAssignableFrom(possibleInstance))
			return true;
		if (clazz.isPrimitive()) {
			try {
				if (clazz.getField("TYPE").get(null).equals(possibleInstance))
					return true;
			} catch (Exception ignored) {
			}
		}
		return false;
	}
}
