package com.sundayfactory.testwizet.core;

public class appInfo {
	private String _ID;
	private String Package;
	private long startTime;
	private long count;
	public appInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public appInfo(String paString , long start , long count) {
		this.Package = paString;
		this.startTime = start;
		this.count = count;
	}
	
	public String get_ID() {
		return _ID;
	}
	public void set_ID(String _ID) {
		this._ID = _ID;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
}
