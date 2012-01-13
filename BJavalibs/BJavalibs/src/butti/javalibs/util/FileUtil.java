package butti.javalibs.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileUtil {
	public static String fileGetContents(String file) {
		return fileGetContents(new File(file));
	}

	public static String fileGetContents(File file) {
		StringBuffer content = new StringBuffer();

		try {
			if (!file.exists()) {
				System.out.println("fileGetContents: File don't exist: " + file.getAbsolutePath());
				return null;
			}

			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = br.readLine()) != null) {
				content.append(line);
				content.append("\n");
			}
			in.close();
		} catch (Exception e) {
			System.out.println("fileGetContents: " + file);
			e.printStackTrace();
			return null;
		}

		return content.toString();
	}

	public static boolean isDateFileInRange(String path, int seconds) {
		String date = FileUtil.fileGetContents(path);

		if (date != null) {
			date = date.trim();
			try {
				long d = Long.parseLong(date);
				long c = (System.currentTimeMillis() / 1000);

				if (c - seconds < d) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public static boolean filePutDate(String url) {
		return filePutContents(url, Long.toString(System.currentTimeMillis() / 1000));
	}

	public static boolean filePutContents(String url, String contents) {
		try {
			FileWriter outFile = new FileWriter(url);
			PrintWriter out = new PrintWriter(outFile);

			out.write(contents);

			out.close();
			return true;
		} catch (Exception e) {
			System.out.println("filePutContents: " + url);
			e.printStackTrace();
			return false;
		}
	}

	public static boolean filePutContents(File file, String contents) {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(file));

			out.write(contents);

			out.close();
			return true;
		} catch (Exception e) {
			System.out.println("filePutContents: " + file.getAbsolutePath());
			e.printStackTrace();
			return false;
		}
	}

	public static void copyFile(File in, File out) throws IOException {
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		try {
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
}
