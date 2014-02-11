package org.ioarmband.controler.ia;

public class MenuData{
	private String appName;
	private String appID;
	private Class<? extends InternalClientInteligence> intelligenceClass;
	
	public MenuData(String appName, Class<? extends InternalClientInteligence> intelligenceClass) {
		super();
		this.appName = appName;
		this.intelligenceClass = intelligenceClass;
		this.appID = "";
	}
	public MenuData(String appName, String appID) {
		super();
		this.appName = appName;
		this.intelligenceClass = SimpleAppClientInteligence.class;
		this.appID = appID;
	}
	public String getAppName() {
		return appName;
	}
	public Class<? extends InternalClientInteligence> getIntelligenceClass() {
		return intelligenceClass;
	}
	public String getAppID() {
		return appID;
	}
	public boolean isSimpleApp(){
		return !appID.equals("");
	}
	
}