package team.hollow.abnormalib.modules.api;

import de.siphalor.tweed.config.ConfigCategory;
import de.siphalor.tweed.config.entry.ConfigEntry;
import net.minecraft.util.Pair;

import java.util.Collection;
import java.util.Collections;

public class SubModule extends Module {
	protected ConfigCategory configCategory;

	public SubModule(String name, String description) {
		super(name, description);
	}

	protected ConfigCategory getConfigCategory() {
		if(configCategory == null) {
			configCategory = new ConfigCategory();
			if(backgroundTexture != null)
				configCategory.setBackgroundTexture(backgroundTexture);
			configCategory.setComment(description);
			configEntries.forEach(pair -> configCategory.register(pair.getLeft(), pair.getRight()));
			features.forEach(feature -> feature.getConfigEntries().forEach(pair -> configCategory.register(pair.getLeft(), pair.getRight())));
		}
		return configCategory;
	}

	@Override
	public Collection<Pair<String, ConfigEntry>> getConfigEntries() {
        return Collections.singleton(new Pair<>(name, getConfigCategory()));
	}
}
