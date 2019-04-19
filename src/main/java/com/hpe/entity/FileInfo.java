package com.hpe.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hpe.utils.HDFSUtils;
import com.hpe.utils.filetype.CheckExcelFileTypeUtil;
import org.apache.hadoop.fs.FileStatus;

import com.hpe.utils.StringTools;

public class FileInfo {

	/**
	 * 文件或目录的名字
	 */
	private String name;

	/**
	 * 文件的后缀名
	 */
	private String suffix;

	/**
	 * 绝对路径
	 */
	private String absolutePath;

	/**
	 * 大小
	 */
	private String length;

	/**
	 * 是否为目录 ,true为目录
	 */
	private boolean isdir;

	/**
	 * 副本数
	 */
	private short block_replication;

	/**
	 * block大小
	 */
	private long blocksize;

	/**
	 * 修改时间
	 */
	private String modification_time;
	/**
	 * 访问时间
	 */
	private long access_time;
//	private FsPermission permission;

	/**
	 * 属主
	 */
	private String owner;
	/**
	 * 属组
	 */
	private String group;
	/**
	 * 是否为修改状态
	 */
	private boolean rename = false;
	public FileInfo() {
		super();
	}
	public FileInfo(FileStatus fs) {
		super();
		if (fs.isDirectory()){
			this.suffix = "dir";
		}else if (fs.getPath().getName().contains(".")){
			this.suffix = CheckExcelFileTypeUtil.getOfficeType(fs.getPath().getName());
		}else {
			this.suffix = "none";
		}
		absolutePath = StringTools.changePath(fs.getPath().toString());
		this.name = fs.getPath().getName();
		this.length = HDFSUtils.setSize((double)fs.getLen());
		this.isdir = fs.isDirectory();
		if(isdir){
			absolutePath=absolutePath+"/";
		}
		this.block_replication = fs.getReplication();
		this.blocksize = fs.getBlockSize();
		Date date = new Date(fs.getModificationTime());
		this.modification_time = StringTools.getCurrentTime(date);
		this.access_time = fs.getAccessTime();
		this.owner = fs.getOwner();
		this.group = fs.getGroup();
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		return "FileInfo{" +
				"name='" + name + '\'' +
				", suffix='" + suffix + '\'' +
				", absolutePath='" + absolutePath + '\'' +
				", length=" + length +
				", isdir=" + isdir +
				", block_replication=" + block_replication +
				", blocksize=" + blocksize +
				", modification_time='" + modification_time + '\'' +
				", access_time=" + access_time +
				", owner='" + owner + '\'' +
				", group='" + group + '\'' +
				", rename=" + rename +
				'}';
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAbsolutePath() {
		return absolutePath;
	}


	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}


	public String getLength() {
		return length;
	}


	public void setLength(String length) {
		this.length = length;
	}


	public boolean isIsdir() {
		return isdir;
	}


	public void setIsdir(boolean isdir) {
		this.isdir = isdir;
	}


	public short getBlock_replication() {
		return block_replication;
	}


	public void setBlock_replication(short block_replication) {
		this.block_replication = block_replication;
	}


	public long getBlocksize() {
		return blocksize;
	}


	public void setBlocksize(long blocksize) {
		this.blocksize = blocksize;
	}


	public String getModification_time() {
		return modification_time;
	}


	public void setModification_time(String modification_time) {

		this.modification_time = modification_time;
	}


	public long getAccess_time() {
		return access_time;
	}


	public void setAccess_time(long access_time) {
		this.access_time = access_time;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getGroup() {
		return group;
	}


	public void setGroup(String group) {
		this.group = group;
	}
	public boolean isRename() {
		return rename;
	}
	public void setRename(boolean rename) {
		this.rename = rename;
	}



}
