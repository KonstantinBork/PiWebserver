package utils.interfaces;

public interface Interaction<E> {

	/**
	 * Sends a message of type E.
	 * @param message
	 * @return true if message was sent successfully
	 */
	public boolean send(E message);
	
	/**
	 * Gets a message of type E.
	 * @return object of type E
	 */
	public E receive();
	
}
