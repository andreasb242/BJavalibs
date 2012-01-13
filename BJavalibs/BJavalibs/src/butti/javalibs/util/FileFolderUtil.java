package butti.javalibs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileFolderUtil {
	public static boolean deleteFolders(String folderName)
			throws FileNotFoundException {
		return deleteFolder(new File(folderName));
	}

	public static boolean deleteFolder(File folder) {
		File[] folders = folder.listFiles();
		if (folders == null) {
			return folder.delete();
		}

		for (File f : folders) {
			if (f.isFile()) {
				if (!f.delete()) {
					return false;
				}
			} else {
				if (!deleteFolder(f)) {
					return false;
				}
			}
		}

		return folder.delete();
	}

	public static boolean copyfile(String src, String target,
			boolean createFolder, boolean writable, boolean deleteFirst) {
		try {
			File f1 = new File(src);
			File f2 = new File(target);

			if (createFolder) {
				File folder = f2.getParentFile();
				if (!folder.exists()) {
					folder.mkdirs();
				}
			}

			if(deleteFirst && f2.exists()) {
				f2.delete();
			}
			
			InputStream in = new FileInputStream(f1);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			
			f2.setWritable(writable);
			
			return true;
		} catch (FileNotFoundException ex) {
			System.err.println("B-FileFolderUtil:" + ex.getMessage() + " in the specified directory.");
		} catch (IOException e) {
			System.err.println("B-FileFolderUtil:" + e.getMessage());
		}
		return false;
	}
}