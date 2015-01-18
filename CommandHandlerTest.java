import static org.junit.Assert.*;

import org.junit.Test;


public class CommandHandlerTest {

	@Test
	public void testhandleInput() {
		CommandHandler ch = new CommandHandler();
		assertEquals("Error, invalid input",ch.handleInput(""));
		assertEquals("stat",ch.handleInput("STAT"));
		assertEquals("identify",ch.handleInput("IDEN"));
		assertEquals("list in handler",ch.handleInput("LIST"));
		assertEquals("message",ch.handleInput("MESG"));
		assertEquals("broadcast",ch.handleInput("HAIL"));
		assertEquals("+OK client signing out",ch.handleInput("QUIT"));
	}

}
