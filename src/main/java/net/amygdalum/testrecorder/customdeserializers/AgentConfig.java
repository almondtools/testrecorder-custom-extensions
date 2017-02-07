package net.amygdalum.testrecorder.customdeserializers;

import static java.util.Arrays.asList;

import java.nio.file.Paths;
import java.util.List;

import net.amygdalum.testrecorder.DefaultTestRecorderAgentConfig;
import net.amygdalum.testrecorder.ScheduledTestGenerator;
import net.amygdalum.testrecorder.SnapshotConsumer;

public class AgentConfig extends DefaultTestRecorderAgentConfig {

	@Override
	public SnapshotConsumer getSnapshotConsumer() {
		return new ScheduledTestGenerator(getInitializer())
			.withDumpOnCounterInterval(2)
			.withClassName("${class}${counter}Test")
			.withDumpOnShutDown(true)
			.withDumpTo(Paths.get("target/generated"));
	}
	
	@Override
	public long getTimeoutInMillis() {
		return 100_000;
	}

	@Override
	public List<String> getPackages() {
		return asList("net.amygdalum.testrecorder.customdeserializers");
	}
	
	@Override
	public Class<? extends Runnable> getInitializer() {
		return null;
	}
}
