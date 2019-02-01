package ch.kbw.render;

import org.junit.jupiter.api.Test;

public class WindowRendererTest
{
	@Test
	public void testRendererInitialization()
	{
		WindowRenderer.getInstance().initialize();
	}
}