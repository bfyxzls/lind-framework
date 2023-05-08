package com.lind.minio.model;

import lombok.ToString;

public class FileModel {

	String filename;

	Boolean directory;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Boolean getDirectory() {
		return directory;
	}

	public void setDirectory(Boolean directory) {
		this.directory = directory;
	}

	@Override
	public String toString() {
		return "FileModel{" + "filename='" + filename + '\'' + ", directory=" + directory + '}';
	}

}
