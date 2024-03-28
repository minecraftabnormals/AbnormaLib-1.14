/*
 * Copyright (c) 2024-2023 OliviaTheVampire
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

package io.github.vampirestudios.vampirelib.client;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Proxy object for a value that is calculated on first access
 * @param <T> The type of the value
 */
public interface Lazy<T> extends Supplier<T>
{
    /**
     * Constructs a lazy-initialized object
     * @param supplier The supplier for the value, to be called the first time the value is needed.
     */
    static <T> Lazy<T> of(@NotNull Supplier<T> supplier)
    {
        return new Lazy.Fast<>(supplier);
    }

    /**
     * Constructs a thread-safe lazy-initialized object
     * @param supplier The supplier for the value, to be called the first time the value is needed.
     */
    static <T> Lazy<T> concurrentOf(@NotNull Supplier<T> supplier)
    {
        return new Lazy.Concurrent<>(supplier);
    }

    /**
     * Non-thread-safe implementation.
     */
    final class Fast<T> implements Lazy<T>
    {
        private Supplier<T> supplier;
        private T instance;

        private Fast(Supplier<T> supplier)
        {
            this.supplier = supplier;
        }

        @Nullable
        @Override
        public final T get()
        {
            if (supplier != null)
            {
                instance = supplier.get();
                supplier = null;
            }
            return instance;
        }
    }

    /**
     * Thread-safe implementation.
     */
    final class Concurrent<T> implements Lazy<T>
    {
        private volatile Object lock = new Object();
        private volatile Supplier<T> supplier;
        private volatile T instance;

        private Concurrent(Supplier<T> supplier)
        {
            this.supplier = supplier;
        }

        @Nullable
        @Override
        public final T get()
        {
            // Copy the lock to a local variable to prevent NPEs if the lock field is set to null between the
            // null-check and the synchronization
            Object localLock = this.lock;
            if (supplier != null)
            {
                // localLock is not null here because supplier was non-null after we copied the lock and both of them
                // are volatile
                synchronized (localLock)
                {
                    if (supplier != null)
                    {
                        instance = supplier.get();
                        supplier = null;
                        this.lock = null;
                    }
                }
            }
            return instance;
        }
    }
}
