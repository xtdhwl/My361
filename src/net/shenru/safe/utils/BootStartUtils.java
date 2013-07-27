package net.shenru.safe.utils;

import java.util.ArrayList;
import java.util.List;

import model.AppInfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class BootStartUtils {
	private static final String BOOT_START_PERMISSION = "android.permission.RECEIVE_BOOT_COMPLETED";

	// public static List<ApplicationInfo> fetchInstalledApps(Context
	// context) {
	// PackageManager pm = context.getPackageManager();
	// List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
	// Iterator<ApplicationInfo> appInfoIterator = appInfo.iterator();
	//
	// List<ApplicationInfo> appList = new
	// ArrayList<ApplicationInfo>(appInfo.size());
	//
	// while (appInfoIterator.hasNext()) {
	// ApplicationInfo app = appInfoIterator.next();
	// int flag = pm.checkPermission(BOOT_START_PERMISSION,
	// app.packageName);
	// if (flag == PackageManager.PERMISSION_GRANTED) {
	// if ((app.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
	// // 系统应用
	// } else {
	// appList.add(app);
	// }
	// }
	// }
	// return appList;
	// }
	
	public static List<String> getBootAction(){
		
		return null;
	}

//	/**
//	 * 获取开机启动列表
//	*COMPONENT_ENABLED_STATE_DEFAULT = 0;
//	*COMPONENT_ENABLED_STATE_ENABLED = 1;
//	*COMPONENT_ENABLED_STATE_DISABLED = 2;
//	 */
//	public static List<AppInfo> getBootComponentNames(Context context, int statu) {
//		List<AppInfo> appInfos = new ArrayList<AppInfo>();
//		
//		
//		PackageManager pm = context.getPackageManager();
//		Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
//		List<ResolveInfo> resolveInfoList = pm.queryBroadcastReceivers(intent,
//				PackageManager.GET_DISABLED_COMPONENTS);
//
//		for (ResolveInfo info : resolveInfoList) {
//
//			if ((info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
//				// 系统应用
//				//判断系统应用也可以根据uid来判断
//			} else {
//				//用户应用
//				if (info.activityInfo.enabled) {
//					boolean isContain = false;
//					for (AppInfo app : appInfos) {
//						if (app.getPackageName().equals(info.activityInfo.applicationInfo.packageName)) {
//							isContain = true;
//							break;
//						}
//					}
//					if (!isContain) {
//						AppInfo appInfo = new AppInfo();
//						appInfo.setName(info.activityInfo.applicationInfo.packageName);
//						appInfo.setPackageName(packageName);
//						appInfos.add(info);
//					}
//				}
//
//			}
//		}
//
//		return null;
//	}

	public static List<ResolveInfo> bootInstalledApps(Context context) {
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
		List<ResolveInfo> resolveInfoList = pm.queryBroadcastReceivers(intent,
				PackageManager.GET_DISABLED_COMPONENTS);

		List<ResolveInfo> appList = new ArrayList<ResolveInfo>();
		for (ResolveInfo info : resolveInfoList) {

			if ((info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// 系统应用
				//判断系统应用也可以根据uid来判断
			} else {
				//用户应用
				if (info.activityInfo.enabled) {
					boolean isContain = false;
					for (ResolveInfo app : appList) {
						if (app.activityInfo.applicationInfo.packageName
								.equals(info.activityInfo.applicationInfo.packageName)) {
							isContain = true;
							break;
						}
					}
					if (!isContain) {
						appList.add(info);
					}
				}

			}
		}
		return appList;
	}

}
