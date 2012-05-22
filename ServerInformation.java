/**
  * ServerInformation used to keep uniformity of ports and ip addresses
  * @author Nicholas Joerger
  * @author Tim Chisholm
  * @author Shane Rand
  * @author Hassan Robinson
 */

public interface ServerInformation {
	/** Port for chat commuinication */
	int CHAT_PORT = 16789;
	/** Port for game communication */
	int BOARD_PORT = 49653;
	/** uses local host for server ip */
	String SERVERIP="localhost";
}
