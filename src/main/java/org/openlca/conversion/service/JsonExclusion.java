package org.openlca.conversion.service;

import java.util.Objects;

import org.openlca.core.model.descriptors.BaseDescriptor;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

class JsonExclusion implements ExclusionStrategy {

	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes atts) {
		if (!BaseDescriptor.class.isAssignableFrom(atts.getDeclaringClass()))
			return false;
		String[] excludedFields = new String[] { "id", "category",
				"infrastructureProcess", "location", "quantitativeReference",
				"refFlowPropertyId" };
		for (String excludedField : excludedFields) {
			if (Objects.equals(excludedField, atts.getName()))
				return true;
		}
		return false;
	}

}
