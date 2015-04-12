package utils;

import java.io.File;

import utils.interfaces.List;

public class FileList implements List<File> {

	private File[] fileList;
	private int size;
	
	public FileList() {
		fileList = new File[1024];
		for(int i = 0; i < fileList.length; i++)
			fileList[i] = null;
		size = 0;
	}

	@Override
	public boolean add(File f) {
		int position = hash(f);
		while(fileList[position] != null) {
			position = (position + 1) % fileList.length;
		}
		fileList[position] = f;
		size++;
		if(size == fileList.length)
			growFileList();
		return true;
	}

	@Override
	public boolean remove(File f) {
		if(!contains(f))
			return false;
		int position = calculatePosition(f);
		fileList[position] = null;
		size--;
		return true;
	}

	@Override
	public boolean contains(File f) {
		if(calculatePosition(f) == -1)
			return false;
		return true;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		if(size == 0)
			return true;
		return false;
	}
	
	@Override
	public File[] toArray() {
		int length = fileList.length;
		File[] result = new File[size];
		int pos = 0;
		for(int i = 0; i < length; i++) {
			if(fileList[i] != null)
				result[pos++] = fileList[i];
		}
		return result;
	}
	
	private int hash(File f) {
		int position = f.getName().hashCode() % fileList.length;
		if(position < 0)
			position = -position;
		return position;
	}
	
	private void growFileList() {
		int newSize = fileList.length * 2;
		File[] temp = new File[newSize];
		for(int i = 0; i < fileList.length; i++) {
			int pos = hash(fileList[i]);
			temp[pos] = fileList[i];
		}
		fileList = temp;
	}
	
	private int calculatePosition(File f) {
		int position = hash(f);
		int hashValue = position;
		// if f is not at the calculated position iterate through the whole array
		if(fileList[position] == null || !fileList[position].getPath().equals(f.getPath())) {
			position = (position + 1) % fileList.length;
			while(fileList[position] == null || !fileList[position].getPath().equals(f.getPath())) {
				position = (position + 1) % fileList.length;
				if(position == hashValue)
					return -1;
			}
		}
		return position;
	}
	
}