/*
 * Copyright (c) 2022 OliviaTheVampire
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

package io.github.vampirestudios.vampirelib.api;

import java.util.Locale;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.*;

import io.github.vampirestudios.vampirelib.Something;
import io.github.vampirestudios.vampirelib.modules.FeatureManager;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientModInitializer.class)
public abstract class BasicModClass implements ModInitializer, ClientModInitializer {
	public static FeatureManager featureManager;
	private final String modId;
	private final String modName;
	private final String modVersion;
	private final Logger logger;
	private boolean printVersionMessage = true;
	private ConfigHolder<? extends CustomConfig> config;

	/**
	 * Creates a copy of the ${@link BasicModClass} given and defines if it's client-side or not.
	 *
	 * @param basicModClass another instance of this class
	 * @param client        if this is client-side or not
	 */
	protected BasicModClass(BasicModClass basicModClass, boolean client) {
		this(basicModClass.modId, basicModClass.modName, basicModClass.modVersion, client);
	}

	/**
	 * Creates a new instance of this class with client set to false.
	 *
	 * @param modName    the name of this mod
	 * @param modVersion the version of this mod
	 */
	protected BasicModClass(String modName, String modVersion) {
		this(modName, modVersion, false);
	}

	/**
	 * Creates a new instance of this class with client set to false.
	 *
	 * @param modId      the mod id of this mod
	 * @param modName    the name of this mod
	 * @param modVersion the version of this mod
	 */
	protected BasicModClass(String modId, String modName, String modVersion) {
		this(modId, modName, modVersion, false);
	}

	/**
	 * Creates a new instance of this class with mod id being the lowercase version of the name.
	 *
	 * @param modName    the name of this mod
	 * @param modVersion the version of this mod
	 * @param client     if this is client-side or not
	 */
	protected BasicModClass(String modName, String modVersion, boolean client) {
		this(modName, modName, modVersion, client);
	}

	/**
	 * Creates a new instance of this class.
	 *
	 * @param modId      the mod id of this mod
	 * @param modName    the name of this mod
	 * @param modVersion the version of this mod
	 * @param client     if this is client-side or not
	 */
	protected BasicModClass(String modId, String modName, String modVersion, boolean client) {
		this.modId = modId.toLowerCase(Locale.ROOT);
		this.modName = modName;
		this.modVersion = modVersion;
		this.logger = LoggerFactory.getLogger(this.modName + (client ? " Client" : ""));
		if (!client) featureManager = this.registerFeatureManager();
	}

	/**
	 * Creates an ${@link FeatureManager} instance with the mod id of this mod
	 * and checks if it's not already registered.
	 *
	 * @return a ${@link FeatureManager} with the mod id of this mod
	 */
	private FeatureManager registerFeatureManager() {
		ResourceLocation id = new ResourceLocation(this.modId, "feature_manager");
		if (Something.FEATURE_MANAGERS.containsKey(id)) {
			return Something.FEATURE_MANAGERS.get(id);
		} else {
			return FeatureManager.createFeatureManager(id);
		}
	}

	/**
	 * Creates a new config for this mod with the specified config class.
	 *
	 * @param config the class of the config that will be used
	 */
	public void createConfig(Class<? extends CustomConfig> config) {
		AutoConfig.register(config, GsonConfigSerializer::new);
		this.config = AutoConfig.getConfigHolder(config);
	}

	/**
	 * @return an instance of this mod's config.
	 */
	public ConfigHolder<? extends CustomConfig> getConfig() {
		return this.config;
	}

	/**
	 * Creates an ${@link ResourceLocation} with the specified and path and the mod id of the mod.
	 *
	 * @param path the path for this resource location
	 *
	 * @return a new ${@link ResourceLocation} with the specified and path and the mod id of the mod
	 */
	public ResourceLocation identifier(String path) {
		return new ResourceLocation(this.modId(), path);
	}

	/**
	 * Creates an ${@link ResourceLocation} with the specified namespace and path.
	 *
	 * @param namespace the namespace for this resource location
	 * @param path      the path for this resource location
	 *
	 * @return a new ${@link ResourceLocation} with the specified namespace and path
	 */
	public ResourceLocation identifier(String namespace, String path) {
		return new ResourceLocation(namespace, path);
	}

	/**
	 * Registers the common features.
	 */
	public void registerFeatures() {
	}

	/**
	 * Registers the client features.
	 */
	@Environment(EnvType.CLIENT)
	public void registerFeaturesClient() {
	}

	/**
	 * Registers the server features.
	 */
	@Environment(EnvType.SERVER)
	public void registerFeaturesServer() {
	}

	/**
	 * Initializes the common.
	 */
	public void commonPostRegisterFeatures() {
		featureManager.initCommon(this.modId());
	}

	/**
	 * Initializes the client features.
	 */
	@Environment(EnvType.CLIENT)
	public void clientPostRegisterFeatures() {
		featureManager.initClient(this.modId());
	}

	/**
	 * Initializes the server features.
	 */
	@Environment(EnvType.SERVER)
	public void serverPostRegisterFeatures() {
		featureManager.initServer(this.modId());
	}

	public FeatureManager featureManager() {
		return featureManager;
	}

	/**
	 * @return the logger for this instance of the mod
	 */
	public Logger getLogger() {
		return this.logger;
	}

	/**
	 * @return the mod id of the mod
	 */
	public String modId() {
		return this.modId;
	}

	/**
	 * @return the name of the mod
	 */
	public String modName() {
		return this.modName;
	}

	/**
	 * @return the version of the mod
	 */
	public String modVersion() {
		return this.modVersion;
	}

	/**
	 * Makes it so the default version info does not get printed.
	 */
	public void shouldNotPrintVersionMessage() {
		this.printVersionMessage = false;
	}

	@Override
	public void onInitialize() {
		if (this.printVersionMessage) {
			this.getLogger().info("You're now running {} v{} for {}", this.modName(), this.modVersion(),
					SharedConstants.getCurrentVersion().getName());
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
		if (this.printVersionMessage) {
			this.getLogger().info("You're now running {} v{} on Client-Side for {}", this.modName(), this.modVersion(),
					SharedConstants.getCurrentVersion().getName());
		}
	}
}
