package ch.kbw.update;

import org.junit.jupiter.api.Test;

public class UpdateLoopTest
{
	@Test
	public void testGameLoopThread()
	{
		UpdateLoop.getInstance().start();
	}
}