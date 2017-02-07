package net.amygdalum.testrecorder.customdeserializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import net.amygdalum.testrecorder.DeserializationException;
import net.amygdalum.testrecorder.SerializedValue;
import net.amygdalum.testrecorder.values.SerializedArray;
import net.amygdalum.testrecorder.values.SerializedLiteral;

public class ArrayManager {

	public static int[][] readIntArray(String filename) {
		try (ObjectInputStream o = new ObjectInputStream(Files.newInputStream(Paths.get(filename)))) {
			int[][] readObject = (int[][]) o.readObject();
			return readObject;
		} catch (IOException|ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static Matcher<int[][]> matchingIntArray(String filename) {
		int[][] array = readIntArray(filename);
		return new TypeSafeMatcher<int[][]>() {

			@Override
			public void describeTo(Description description) {
			}

			@Override
			protected boolean matchesSafely(int[][] item) {
				return Arrays.deepEquals(array, item);
			}
		};
	}

	public static String storeIntArray(SerializedArray value) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ObjectOutputStream o = new ObjectOutputStream(out)) {
			int[][] deserialized = getIntArrayValue(value);
			o.writeObject(deserialized);

			byte[] data = out.toByteArray();

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] mdbytes = md.digest(data);
			StringBuilder buffer = new StringBuilder();
			for (int i = 0; i < mdbytes.length; i++) {
				buffer.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			String fileName = buffer.toString() + ".array";

			Path path = Paths.get(fileName);
			Files.write(path, data);

			return path.toAbsolutePath().toString().replace("\\", "\\\\");
		} catch (IOException e) {
			throw new DeserializationException("failed writing array to file");
		} catch (NoSuchAlgorithmException e) {
			throw new DeserializationException("failed hashing data to produce unique file name");
		}
	}

	public static int[][] getIntArrayValue(SerializedArray value) {
		SerializedValue[] array = value.getArray();
		int[][] deserialized = new int[array.length][];
		for (int i = 0; i < deserialized.length; i++) {
			SerializedArray item = (SerializedArray) array[i];
			SerializedValue[] subarray = item.getArray();
			deserialized[i] = new int[subarray.length];
			for (int j = 0; j < deserialized[i].length; j++) {
				SerializedLiteral subItem = (SerializedLiteral) subarray[j];
				deserialized[i][j] = (Integer) subItem.getValue();
			}
		}
		return deserialized;
	}
}
