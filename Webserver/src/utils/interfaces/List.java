package utils.interfaces;

import java.io.File;

public interface List<E> {
	
	/**
	 * Adds a file to the list.
	 * @param f file to add
	 * @return true if adding was successful
	 */
	public boolean add(File f);
	
	/**
	 * Removes a file from the list.
	 * @param f file to remove
	 * @return true if removing was successful
	 */
	public boolean remove(File f);
	
	/**
	 * Returns if the given file is in the list.
	 * @param f file to look up
	 * @return the position if f is in the list, otherwise -1
	 */
	public boolean contains(File f);
	
	/**
	 * Returns the size of the list.
	 * @return number of elements in the list
	 */
	public int size();
	
	/**
	 * Returns if the list is empty or not.
	 * @return true if the list is empty
	 */
	public boolean isEmpty();
	
	/**
	 * Returns the list as an array.
	 * @return an array with all elements of the list
	 */
	public E[] toArray();
	
}
