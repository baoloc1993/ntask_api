package io.github.ntask_api.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The helper class to get the path to folders in application
 */
@Component
@Log4j2
public class FolderProvider {

	@Value("${app.folder.location}")
	private String folderLoc;

	private Path folder;

	@PostConstruct
	public void init() {
		File tempFolder = Path.of(folderLoc).normalize().toFile();
		if (!tempFolder.exists() || !tempFolder.isDirectory()) {
			tempFolder.mkdir();
		}

		if (!tempFolder.isDirectory()) {
			throw new RuntimeException("Template folder is not existed. Please check temporary folder at " + folderLoc);
		}

		folder = Paths.get(folderLoc).toAbsolutePath().normalize();
	}

	/**
	 * Get temporary folder
	 * 
	 * @return Path
	 */
	public Path getFolder() {
		return folder;
	}

	public Path resolveDirectory(String child, boolean create) {
		try {
			Path path = folder.resolve(child);
			if(Files.exists(path) && !Files.isDirectory(path)) {
				throw new RuntimeException("Not a directory");
			}

			if(create && Files.notExists(path)) {
				Files.createDirectories(path);
			}

			return path;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

//	/**
//	 * Get path of file or folder
//	 *
//	 * @param filePath The given file path
//	 * @return Path
//	 */
//	public Path from(String filePath) {
//		try {
//			return Paths.get(filePath).toAbsolutePath().normalize();
//		} catch (Exception ex) {
//			throw new RuntimeException("Could not create the directory: " + filePath, ex);
//		}
//	}
//
//	/**
//	 * Get path of file or folder
//	 *
//	 * @param filePath The given file path
//	 * @return Path
//	 */
//	public Path from(String filePath, boolean createNewIfNotExists) {
//		try {
//			Path path = Paths.get(filePath).toAbsolutePath().normalize();
//			if (createNewIfNotExists) {
//				Files.createDirectories(path);
//			}
//			return path;
//		} catch (Exception ex) {
//			throw new RuntimeException("Could not create the directory: " + filePath, ex);
//		}
//	}
}
