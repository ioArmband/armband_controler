package org.tse.pri.ioarmband.armband.ia;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.tse.pri.ioarmband.armband.io.Client;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor.CommandExecutor;
import org.tse.pri.ioarmband.armband.protocol.ProtocolExecutor.CommandParam;
import org.tse.pri.ioarmband.io.message.MenuAppMessage;
import org.tse.pri.ioarmband.io.message.Message;
import org.tse.pri.ioarmband.io.message.enums.GestureType;

public class MenuClientInteligence extends InternalClientInteligence{
	
	private static final Logger logger = Logger.getLogger(MenuClientInteligence.class);
	
	private ArrayList<MenuData> registredApps;
	private Integer currentIndex;
	private Integer previousIndex;
	
	public MenuClientInteligence() {
		super();
		registredApps = new ArrayList<MenuData>();
		currentIndex = 0;
		previousIndex = -1;
	}

	@Override
	public void start() {
		sendMenuMessage();
	}

	public void registerApp(MenuData data){
		registredApps.add(data);
	}
	///////////////////////////////////////
	//             PROTOCOL              //
	///////////////////////////////////////
	
	@CommandExecutor("gesture")
	public void onAppGesture(Client client, @CommandParam("type") GestureType type, @CommandParam("sourceName") String sourceName)
	{
		if(type.equals(GestureType.SWIPE)){
			if(sourceName.equals("left")){
				navigateToPreviousApp();
			}
			else if(sourceName.equals("right")){
				navigateToNextApp();
			}
			else if(sourceName.equals("bottom")){
				goMain();
			}
		}
		if(type.equals(GestureType.TOUCH)){
			launchCurrentApp();
		}
	}
	
	
	private void launchCurrentApp(){
		logger.info("launchCurrentApp()");
		if(currentIndex == 0)
			return;
		MenuData data = registredApps.get(currentIndex-1);
		InternalClientInteligence ia;
		if(data.isSimpleApp()){
			ia = new SimpleAppClientInteligence(data.getAppID());
			ia.start();
		}else{
			try {
				ia = data.getIntelligenceClass().newInstance();
				ia.start();
			} catch (Exception e) {
				logger.error("Instanciation of intelligence " +  data.getIntelligenceClass().getCanonicalName() + " failled", e);
			}
		}
	}
	
	private void navigateToPreviousApp(){
		if(currentIndex != 0){
			--currentIndex;
			sendMenuMessage();
		}
	}
	
	private void navigateToNextApp(){
		if(currentIndex < registredApps.size()){
			++currentIndex;
			sendMenuMessage();
		}
	}

	private void goMain(){
		currentIndex = 0;
		sendMenuMessage();
	}
	
	private void sendMenuMessage(){
		Message menuMsg;
		if(currentIndex == previousIndex)
			return;
		
		if(currentIndex == 0){
			menuMsg = new MenuAppMessage(0, isLastApp());
		}
		else{
			MenuData data = registredApps.get(currentIndex-1);
			menuMsg = new MenuAppMessage(currentIndex, isLastApp(), data.getAppName());
		}
		sendMessage(menuMsg);
		previousIndex = currentIndex;
	}
	
	public Boolean isLastApp(){
		return currentIndex == registredApps.size();
	}
	

}
