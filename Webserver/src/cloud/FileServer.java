package cloud;

import java.io.File;
import java.util.List;

public interface FileServer {

	/**
	 * Adds a file to the server's path.
	 * @param file
	 * @return true if file is added
	 */
	public boolean addFile(File file);
	
	/**
	 * Deletes the given file from the server.
	 * @param file
	 * @return true if file is deleted
	 */
	public boolean deleteFile(File file);

	/**
	 * Moves the given file to another path.
	 * @param file
	 * @param newPath
	 * @return true if file is moved
	 */
	public boolean move(File file, String newPath);
	
	/**
	 * Gets the given file, e.g. for download.
	 * @param file
	 * @return given file
	 */
	public File getFile(File file);
	
	/**
	 * Returns all a list of all files saved on the server.
	 * @return
	 */
	public List<File> getFiles();
	
}