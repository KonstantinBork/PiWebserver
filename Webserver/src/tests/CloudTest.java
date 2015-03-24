package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.Test;

import utils.FileList;
import cloud.CloudService;

public class CloudTest {
	
	private static final InputStreamReader reader = new InputStreamReader(System.in);
	private static final OutputStreamWriter writer = new OutputStreamWriter(System.out);
	private static final char[] buffer = new char[1024];
	private static final CloudService service = new CloudService(reader, writer, buffer);

	@Test
	public void testGettingAllFiles() {
		FileList files = service.getFiles();
		for(File f: files.toArray())
			System.out.println(f);
		assertFalse(files == null);
		assertTrue(files.size() >= 0);
	}
	
	@Test
	public void testAddingFile() {
		File file = new File(System.getProperty("user.home") + "/test");
		FileList files = service.getFiles();
		assertEquals(0, files.size());
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.addFile(file);
		assertEquals(1, files.size());
	}

	@Test
	public void testRemovingFile() {
		File file = new File(System.getProperty("user.home") + "/cloud/test");
		FileList files = service.getFiles();
		assertEquals(1, files.size());
		service.deleteFile(file);
		assertEquals(0, files.size());
	}
	
}