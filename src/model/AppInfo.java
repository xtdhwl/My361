package model;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * 应用信息 
 * @author along
 */
public class AppInfo {
	
	private Drawable icon;
	private String name;
	private String packageName;
	private ComponentName bootComponent;
	private List<ComponentName> bootServiceComponent;
	
	public AppInfo() {
		super();
	}
	
	public AppInfo(Context context,ResolveInfo info){
		PackageManager pm = context.getPackageManager();
//		this.name = info.activityInfo.applicationInfo.name;
		this.name = pm.getApplicationLabel(info.activityInfo.applicationInfo).toString();
		this.icon = pm.getApplicationIcon(info.activityInfo.applicationInfo);
		this.packageName = info.activityInfo.applicationInfo.packageName;
		
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public ComponentName getBootComponent() {
		return bootComponent;
	}

	public void setBootComponent(ComponentName bootComponent) {
		this.bootComponent = bootComponent;
	}

	public List<ComponentName> getBootServiceComponent() {
		return bootServiceComponent;
	}

	public void setBootServiceComponent(List<ComponentName> bootServiceComponent) {
		this.bootServiceComponent = bootServiceComponent;
	}

}
