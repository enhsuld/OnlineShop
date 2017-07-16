package com.macro.dev.models;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the FILE_UPLOAD database table.
 * 
 */
@Entity
@Table(name="file_upload")
@NamedQuery(name="FileUpload.findAll", query="SELECT f FROM FileUpload f")
public class FileUpload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String filename;
	private String name;

	private long filesize;
	
	private int aan;

	private String fileurl;

	private String mimetype;



    public FileUpload(long id,String filename,long filesize, String mimetype, String fileurl, int aan) {
    	this.id = id;
        this.filesize = filesize;
        this.filename = filename;
        this.mimetype = mimetype;
        this.fileurl = fileurl;
        this.aan=aan;
    }
    
    public FileUpload() {
        // Default Constructor
    }
	  
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFilesize() {
		return this.filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getFileurl() {
		return this.fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getMimetype() {
		return this.mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAan() {
		return aan;
	}

	public void setAan(int aan) {
		this.aan = aan;
	}
	
}