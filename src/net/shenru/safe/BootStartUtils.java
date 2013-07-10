package net.shenru.safe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class BootStartUtils {
	
	private static final String BOOT_START_PERMISSION = "android.permission.RECEIVE_BOOT_COMPLETED";
	
	
	public static List<ApplicationInfo> fetchInstalledApps(Context context){
		PackageManager pm = context.getPackageManager();
		//XXX 0代表什么
		List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
		Iterator<ApplicationInfo> appInfoIterator = appInfo.iterator();
		List<ApplicationInfo> appList = new ArrayList<ApplicationInfo>(appInfo.size());
		while(appInfoIterator.hasNext()){
			ApplicationInfo app = appInfoIterator.next();
			int flag = pm.checkPermission(BOOT_START_PERMISSION, app.packageName);
			if(flag == PackageManager.PERMISSION_GRANTED){
				if((app.flags & ApplicationInfo.FLAG_SYSTEM) > 0){
					
				}else{
					appList.add(app);
				}
			}
		}
		return appList;
	}
}



























