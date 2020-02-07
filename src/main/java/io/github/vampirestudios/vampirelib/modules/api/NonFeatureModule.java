/*
 * MIT License
 *
 * Copyright (c) 2020 Vampire Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.vampirestudios.vampirelib.modules.api;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public abstract class NonFeatureModule {

    private boolean enabled = true;
    private Identifier registryName;
    private String name;

    public NonFeatureModule(Identifier registryName, String name) {
        this.registryName = registryName;
        this.name = name;
    }

    public ModInitializer getModInitializer() {
        return null;
    }

    @Environment(EnvType.CLIENT)
    public ClientModInitializer getClientModInitializer() {
        return null;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public final void setEnabled(JsonObject obj) {
        setEnabled(obj.getBoolean("enabled", true));
    }

    public Identifier getRegistryName() {
        return registryName;
    }

    public String getName() {
        return name;
    }

    public void configure(JsonObject obj) {
    }

    public final JsonObject getConfig() {
        JsonObject obj = new JsonObject();
        obj.put("enabled", new JsonPrimitive(isEnabled()));
        obj.put("registryName", new JsonPrimitive(getRegistryName().toString()));
        obj.put("name", new JsonPrimitive(getName()));
        writeToConfig(obj);
        return obj;
    }

    public void writeToConfig(JsonObject obj) {
    }

}
