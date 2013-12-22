/**
 * An exception used by SimpleSortedMap to indicate when trying to insert
 * a key that is already in the map.
 */
@SuppressWarnings("serial")
public class KeyFoundException extends RuntimeException {
    /**
     * Creates an exception with a null cause.
     * @see RuntimeException#RuntimeException()
     */
    public KeyFoundException( ) {
	super( );
    }

    /**
     * Creates an exception with cause message.
     * @see RuntimeException#RuntimeException(String message)
     */
    public KeyFoundException( String message ) {
	super( message );
    }
}
