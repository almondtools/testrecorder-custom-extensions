package net.amygdalum.testrecorder.customdeserializers;

import java.nio.file.Paths;

import net.amygdalum.testrecorder.generator.ScheduledTestGenerator;
import net.amygdalum.testrecorder.profile.AgentConfiguration;

public class TestGenerator extends ScheduledTestGenerator {

	public TestGenerator(AgentConfiguration config) {
		super(config);
		this.counterInterval = 2;
		this.classNameTemplate = "${class}${counter}Test";
		this.counterMaximum = 1000;
		this.generateTo = Paths.get("target/generated");
		this.dumpOnShutdown(true);
	}
}
