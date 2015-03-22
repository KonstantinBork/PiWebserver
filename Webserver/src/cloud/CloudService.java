package cloud;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.*;

import java.util.Arrays;
import java.util.List;

// Model
public class CloudService implements Runnable, FileServer {

	private static final File homeDir  = new File(System.getProperty("user.home") + "/cloud");;
	private List<File> homeDirFiles;
	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private char[] buffer;
	
	public CloudService(InputStreamReader reader, OutputStreamWriter writer, char[] buffer) {
		if(!homeDir.exists()) {
			homeDir.mkdirs();
		}
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.getName().equals(".DS_Store"))
					return false;
				return true;
			}
		};
		homeDirFiles = Arrays.asList(homeDir.listFiles(filter));
		this.reader = reader;
		this.writer = writer;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		CloudController controller = new CloudController(reader, writer, buffer, this);
		Thread controllerThread = new Thread(controller);
		controllerThread.start();
		try {
			controllerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addFile(File file) {
		String fileName = file.getName();
		File newFile = new File(homeDir.getAbsolutePath() + "/" + fileName);
		try {
			Files.copy(file.toPath(), newFile.toPath(), REPLACE_EXISTING);
			homeDirFiles.add(newFile);
			return true;
		} catch (IOException e) {
			System.err.println("Copying the file failed!");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteFile(File file) {
		if(!homeDirFiles.contains(file))
			return false;
		homeDirFiles.remove(file);
		file.delete();
		return true;
	}

	@Override
	public boolean move(File file, String newPath) {
		homeDirFiles.remove(file);
		File movedFile = new File(homeDir + "/" + file.getName());
		try {
			movedFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		homeDirFiles.add(movedFile);
		try {
			Files.move(file.toPath(), movedFile.toPath(), REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			System.err.println("Moving the file failed!");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public File getFile(File file) {
		if(homeDirFiles.contains(file))
			return file;
		return null;
	}

	@Override
	public List<File> getFiles() {
		return homeDirFiles;
	}
	
}