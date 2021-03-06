package net.amygdalum.testrecorder.customdeserializers.matcher;

import static net.amygdalum.testrecorder.util.Types.baseType;
import static net.amygdalum.testrecorder.util.Types.parameterized;
import static net.amygdalum.testrecorder.util.Types.wildcard;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.hamcrest.Matcher;

import net.amygdalum.testrecorder.customdeserializers.ArrayManager;
import net.amygdalum.testrecorder.deserializers.Adaptor;
import net.amygdalum.testrecorder.deserializers.Deserializer;
import net.amygdalum.testrecorder.deserializers.matcher.DefaultArrayAdaptor;
import net.amygdalum.testrecorder.deserializers.matcher.DefaultMatcherGenerator;
import net.amygdalum.testrecorder.deserializers.matcher.MatcherGenerator;
import net.amygdalum.testrecorder.types.Computation;
import net.amygdalum.testrecorder.types.DeserializationException;
import net.amygdalum.testrecorder.types.DeserializerContext;
import net.amygdalum.testrecorder.types.TypeManager;
import net.amygdalum.testrecorder.values.SerializedArray;

public class LargeArrayAdaptor extends DefaultMatcherGenerator<SerializedArray> implements MatcherGenerator<SerializedArray> {

	@Override
	public Class<SerializedArray> getAdaptedClass() {
		return SerializedArray.class;
	}

	@Override
	public Class<? extends Adaptor<SerializedArray>> parent() {
		return DefaultArrayAdaptor.class;
	}

	@Override
	public Computation tryDeserialize(SerializedArray value, Deserializer generator) {
		DeserializerContext context = generator.getContext();
		if (value.getArray().length > 100 && isIntArray(value.getComponentType())) {
			TypeManager types = context.getTypes();
			types.staticImport(ArrayManager.class, "matchingIntArray");

			String fileName = ArrayManager.storeIntArray(value);
			return Computation.expression("matchingIntArray(\"" + fileName + "\")", parameterized(Matcher.class, null, wildcard()), new ArrayList<>());
		}
		throw new DeserializationException("matcher");
	}

	private boolean isIntArray(Type type) {
		Class<?> baseType = baseType(type);
		return baseType.isArray() && baseType.getComponentType() == int.class;
	}

}
