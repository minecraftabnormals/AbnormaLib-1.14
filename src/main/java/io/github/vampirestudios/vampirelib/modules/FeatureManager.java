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

package io.github.vampirestudios.vampirelib.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import io.github.vampirestudios.vampirelib.init.VRegistries;
import io.github.vampirestudios.vampirelib.modules.api.ClientFeature;
import io.github.vampirestudios.vampirelib.modules.api.CommonFeature;
import io.github.vampirestudios.vampirelib.modules.api.ServerFeature;
import io.github.vampirestudios.vampirelib.modules.utils.ConsoleUtils;

public final class FeatureManager {
	public static final Registry<CommonFeature> COMMON_FEATURES = FabricRegistryBuilder.createSimple(
			CommonFeature.class, ResourceLocation.parse("vampirelib:common_features")).buildAndRegister();
	public static final Registry<ClientFeature> CLIENT_FEATURES = FabricRegistryBuilder.createSimple(
			ClientFeature.class, ResourceLocation.parse("vampirelib:client_features")).buildAndRegister();
	public static final Registry<ServerFeature> SERVER_FEATURES = FabricRegistryBuilder.createSimple(
			ServerFeature.class, ResourceLocation.parse("vampirelib:server_features")).buildAndRegister();
	private final List<CommonFeature> commonFeatures = new ArrayList<>();
	private final List<ClientFeature> clientFeatures = new ArrayList<>();
	private final List<ServerFeature> serverFeatures = new ArrayList<>();

	private FeatureManager() {
	}

	public static FeatureManager createFeatureManager(ResourceLocation modIdentifier) {
		return Registry.register(VRegistries.FEATURE_MANAGERS, modIdentifier, new FeatureManager());
	}

	public static FeatureManager getFeatureManager(ResourceLocation modIdentifier) {
		return VRegistries.FEATURE_MANAGERS.getValue(modIdentifier);
	}

	public void registerCommonFeature(CommonFeature module) {
		if (COMMON_FEATURES.getOptional(module.getRegistryName()).isEmpty()) {
			Registry.register(COMMON_FEATURES, module.getRegistryName(), module);
		}
	}

	public void registerClientFeature(ClientFeature module) {
		if (CLIENT_FEATURES.getOptional(module.getRegistryName()).isEmpty()) {
			Registry.register(CLIENT_FEATURES, module.getRegistryName(), module);
		}
	}

	public void registerServerFeature(ServerFeature module) {
		if (SERVER_FEATURES.getOptional(module.getRegistryName()).isEmpty()) {
			Registry.register(SERVER_FEATURES, module.getRegistryName(), module);
		}
	}

	public void initCommonFeature(CommonFeature feature) {
		feature.initCommon();
		this.commonFeatures.add(feature);
	}

	public void initCommonFeature(CommonFeature... features) {
		for (CommonFeature feature : features) {
			feature.initCommon();
			this.commonFeatures.add(feature);
		}
	}

	public void initCommon(String modId) {
		COMMON_FEATURES.forEach(feature -> {
			if (!this.commonFeatures.contains(feature))
				if (feature.getRegistryName().getNamespace().equals(modId)) feature.initCommon();
		});
		ConsoleUtils.logCommonFeatures();
	}

	public void initClientFeature(ClientFeature clientFeature) {
		clientFeature.initClient();
		this.clientFeatures.add(clientFeature);
	}

	public void initClientFeature(ClientFeature... features) {
		for (ClientFeature feature : features) {
			feature.initClient();
			this.clientFeatures.add(feature);
		}
	}

	@Environment(EnvType.CLIENT)
	public void initClient(String modId) {
		CLIENT_FEATURES.forEach(feature -> {
			if (!this.clientFeatures.contains(feature)) {
				if (feature.getRegistryName().getNamespace().equals(modId)) feature.initClient();
			}
		});

		ConsoleUtils.logClientFeatures();
	}

	public void initServerFeature(ServerFeature serverFeature) {
		serverFeature.initServer();
		this.serverFeatures.add(serverFeature);
	}

	public void initServerFeature(ServerFeature... features) {
		for (ServerFeature feature : features) {
			feature.initServer();
			this.serverFeatures.add(feature);
		}
	}

	@Environment(EnvType.SERVER)
	public void initServer(String modId) {
		SERVER_FEATURES.forEach(feature -> {
			if (!this.serverFeatures.contains(feature)) {
				if (feature.getRegistryName().getNamespace().equals(modId)) feature.initServer();
			}
		});

		ConsoleUtils.logServerFeatures();
	}

	public boolean doesCommonFeatureExist(CommonFeature module) {
		return COMMON_FEATURES.containsKey(module.getRegistryName());
	}

	public boolean doesClientFeatureExist(ClientFeature module) {
		return CLIENT_FEATURES.containsKey(module.getRegistryName());
	}

	public boolean doesServerFeatureExist(ServerFeature module) {
		return SERVER_FEATURES.containsKey(module.getRegistryName());
	}

	public boolean isFeatureEnabled(ResourceLocation name) {
		if (COMMON_FEATURES.containsKey(name)) {
			CommonFeature module = COMMON_FEATURES.getValue(name);
			return Objects.requireNonNull(module).isEnabled();
		}

		if (CLIENT_FEATURES.containsKey(name)) {
			ClientFeature module = CLIENT_FEATURES.getValue(name);
			return Objects.requireNonNull(module).isEnabled();
		}

		if (SERVER_FEATURES.containsKey(name)) {
			ServerFeature module = SERVER_FEATURES.getValue(name);
			return Objects.requireNonNull(module).isEnabled();
		}

		return false;
	}
}
