package victor.training.ddd;

import java.io.File;
import java.sql.SQLException;

public class StartDatabase {
	public static void main(String[] args) throws SQLException {
		deletePreviousDBContents();

		System.out.println("Started DB...");
		System.out.println("Connecting to 'jdbc:h2:tcp://localhost:9092/~/test' will auto-create a database file 'test.mv.db' in user home (~)...");

		// Allow auto-creating new databases on disk at first connection
		org.h2.tools.Server.createTcpServer("-ifNotExists").start();
	}


	private static void deletePreviousDBContents() {
		File userHomeFolder = new File(System.getProperty("user.home"));
		if (!userHomeFolder.isDirectory()) {
			throw new IllegalArgumentException("Could not locate userHome");
		}
		File databaseFile = new File(userHomeFolder, "test.mv.db");
		System.out.println("Database file: " + databaseFile.getAbsolutePath());
		if (databaseFile.isFile()) {
			System.out.println("Deleting previous db contents...");
			boolean ok = databaseFile.delete();
			if (!ok) {
				System.err.println("Could not delete database file: in use?");
			} else {
				System.out.println("SUCCESS");
			}
		}
	}
}
