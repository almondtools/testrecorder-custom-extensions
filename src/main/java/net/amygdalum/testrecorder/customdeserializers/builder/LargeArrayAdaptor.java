package net.amygdalum.testrecorder.customdeserializers.builder;

import static net.amygdalum.testrecorder.util.Types.baseType;
import static net.amygdalum.testrecorder.util.Types.mostSpecialOf;

import java.lang.reflect.Type;
import java.util.ArrayList;

import net.amygdalum.testrecorder.customdeserializers.ArrayManager;
import net.amygdalum.testrecorder.deserializers.Adaptor;
import net.amygdalum.testrecorder.deserializers.builder.DefaultArrayAdaptor;
import net.amygdalum.testrecorder.deserializers.builder.DefaultSetupGenerator;
import net.amygdalum.testrecorder.deserializers.builder.SetupGenerator;
import net.amygdalum.testrecorder.deserializers.builder.SetupGenerators;
import net.amygdalum.testrecorder.types.Computation;
import net.amygdalum.testrecorder.types.DeserializationException;
import net.amygdalum.testrecorder.types.DeserializerContext;
import net.amygdalum.testrecorder.values.SerializedArray;

public class LargeArrayAdaptor extends DefaultSetupGenerator<SerializedArray> implements SetupGenerator<SerializedArray> {
	
	@Override
	public Class<SerializedArray> getAdaptedClass() {
		return SerializedArray.class;
	}

	@Override
	public Class<? extends Adaptor<SerializedArray, SetupGenerators>> parent() {
		return DefaultArrayAdaptor.class;
	}
	
	@Override
	public Computation tryDeserialize(SerializedArray value, SetupGenerators generator, DeserializerContext context) {
		if (value.getArray().length > 100 && isIntArray(value.getComponentType())) {
			context.getTypes().staticImport(ArrayManager.class, "readIntArray");
			String fileName = ArrayManager.storeIntArray(value);
			return Computation.expression("readIntArray(\"" + fileName + "\")", mostSpecialOf(value.getUsedTypes()).orElse(Object.class), new ArrayList<>());
		}
		throw new DeserializationException("not large array");
	}

	private boolean isIntArray(Type type) {
		Class<?> baseType = baseType(type);
		return baseType.isArray() && baseType.getComponentType() == int.class;
	}

}
