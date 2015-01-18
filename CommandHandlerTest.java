import static org.junit.Assert.*;

import org.junit.Test;


public class CommandHandlerTest {

	@Test
	public void testhandleInput() {
		CommandHandler ch = new CommandHandler();
		assertEquals("Error, invalid input",ch.handleInput(""));
	}

}
