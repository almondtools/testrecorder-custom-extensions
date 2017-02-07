package net.amygdalum.testrecorder.customdeserializers;

public class LargeArrays {

	public static void main(String[] args) {
		new LargeArrays().run();
	}

	public void run() {
		LargeIntArrays arrays = new LargeIntArrays(400);

		arrays.sum();

	}
}