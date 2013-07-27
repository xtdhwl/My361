package net.shenru.safe;

import java.util.Arrays;
import java.util.List;

import net.shenru.safe.utils.BootStartUtils;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;
import com.stericson.RootTools.execution.Shell;

/**
 * 优化 360功能： 1漏洞检测 2：后台软件 3：垃圾文件 4：自动启动项 5：自身安全项检测
 * 
 * @author along
 */
public class OptimaActivity extends BaseActivity {

	private static final String TAG = OptimaActivity.class.getSimpleName();

	private TextView mMarkView;
	private TextView mDesView;
	private Button mRepairView;
	private ProgressBar mProgressview;
	private TextView mSizeView;
	private ListView mResultView;
	private MyAdapter mBaseAdapter;

	private PackageManager mPM;

	private MyBroadcastReceiver myBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optima_activity);
		mPM = getPackageManager();
		myBroadcastReceiver = new MyBroadcastReceiver();

		mMarkView = (TextView) findViewById(R.id.mark);
		mDesView = (TextView) findViewById(R.id.des);
		mRepairView = (Button) findViewById(R.id.repair);
		mProgressview = (ProgressBar) findViewById(R.id.pb);
		mSizeView = (TextView) findViewById(R.id.size);
		mResultView = (ListView) findViewById(R.id.list);

		scan();
		List<ResolveInfo> apps = BootStartUtils.bootInstalledApps(getContext());
		mSizeView.setText("开机启动" + apps.size() + "个");
		mBaseAdapter = new MyAdapter(apps);
		mResultView.setAdapter(mBaseAdapter);
		mResultView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ResolveInfo info = (ResolveInfo) mResultView.getAdapter().getItem(position);
				showAppInfo(info);
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
		filter.addAction(Intent.ACTION_PACKAGE_INSTALL);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
		registerReceiver(myBroadcastReceiver, filter, null, null);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(myBroadcastReceiver);
	}

	private void showAppInfo(ResolveInfo app) {
		CharSequence name = mPM.getApplicationLabel(app.activityInfo.applicationInfo);
		String acitivityPackageName = app.activityInfo.name;
		String applicationPackageName = app.activityInfo.applicationInfo.packageName;

		String sub = "应用名称:%s\n包名:%s\n类名:%s";
		String baseStr = String.format(sub, name, acitivityPackageName, applicationPackageName);

		AlertDialog.Builder dialong = new AlertDialog.Builder(this);
		dialong.setTitle("应用信息").setMessage(baseStr).setPositiveButton("确定", null).create().show();

	}

	private class MyAdapter extends BaseAdapter {

		private List<ResolveInfo> lists;

		public MyAdapter(List<ResolveInfo> apps) {
			this.lists = apps;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setData(List<ResolveInfo> infos) {
			lists = infos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ResolveInfo app = lists.get(position);

			View view = View.inflate(getContext(), R.layout.app_boot_item, null);
			ImageView iv = (ImageView) view.findViewById(R.id.icon);
			TextView nameView = (TextView) view.findViewById(R.id.name);
			TextView modeView = (TextView) view.findViewById(R.id.mode);
			CheckBox seleView = (CheckBox) view.findViewById(R.id.select);

			CharSequence label = mPM.getApplicationLabel(app.activityInfo.applicationInfo);
			Drawable icon = mPM.getApplicationIcon(app.activityInfo.applicationInfo);

			iv.setImageDrawable(icon);
			nameView.setText(label);
			modeView.setText("[开机启动]");

			ComponentName componentName = new ComponentName(app.activityInfo.applicationInfo.packageName,
					app.activityInfo.name);
			int componentEnabledSetting = getPackageManager().getComponentEnabledSetting(componentName);
			
//			System.out.println("getView:position:" + position);
//			System.out.println("getView:position:" + position);
//			System.out.println("getView:componentEnabledSetting:" + componentEnabledSetting);
			
			seleView.setChecked(componentEnabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED);

			seleView.setOnCheckedChangeListener(new MyOnCheckedChangeListener(app));
			
			
			return view;
		}

	}

	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {

		ResolveInfo _app;

		public MyOnCheckedChangeListener(ResolveInfo app) {
			this._app = app;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				// 选中
				try {
					String acitivityPackageName = _app.activityInfo.name;
					String applicationPackageName = _app.activityInfo.packageName;
					String subcmd = "pm enable %s/%s";
					String cmd = String
							.format(subcmd, applicationPackageName, acitivityPackageName);

					CommandCapture command = new CommandCapture(0, "su",
							"export LD_LIBRARY_PATH=/vendor/lib;/system/lib/", cmd);
					Shell.runRootCommand(command);
					SystemClock.sleep(500);
					mBaseAdapter.setData(BootStartUtils.bootInstalledApps(getContext()));
					mBaseAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 取消
				// 选中
				try {
					String acitivityPackageName = _app.activityInfo.name;
					String applicationPackageName = _app.activityInfo.packageName;

					String subcmd = "pm disable  %s/%s";
					String cmd = String
							.format(subcmd, applicationPackageName, acitivityPackageName);

					CommandCapture command = new CommandCapture(0, "su",
							"export LD_LIBRARY_PATH=/vendor/lib;/system/lib/", cmd);
					Shell.runRootCommand(command);
					SystemClock.sleep(500);
					mBaseAdapter.setData(BootStartUtils.bootInstalledApps(getContext()));
					mBaseAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class MyBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("MyBroadcastReceiver:" + intent.getAction());
		}
		
	}

	private void scan() {
		// 漏洞
		// 后台软件
		// 自动启动项
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> rApps = am.getRunningAppProcesses();
		List<RunningServiceInfo> runingServices = am.getRunningServices(30);
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(30);

		// ps USER PID PPID VSIZE RSS WCHAN PC NAME
		// USER: 进程所有者
		// PID: 进程ID
		// PPID：父进程ID
		// VSZ: 占用的虚拟内存大小
		// RSS: 占用的内存大小
		// WCHAN 正在等待的进程资源；
		// appProcessInfo
		Log.i(TAG, "--------------RunningAppProcessInfo-----" + rApps.size() + "-------------");
		// 查看360进程为
		// pid:446
		// uid:10034
		// processName:com.huawei.inputmethod.hwpal
		// pkgList:[com.huawei.inputmethod.hwpal]
		// importance:130
		// lru:0
		for (RunningAppProcessInfo appInfo : rApps) {
			// 进程描述符pid
			Log.i(TAG, "pid:" + appInfo.pid);
			// 用户id 通过adb shell->ps查看进程列表，我们可以看到用户有root shell system
			// wifi r t 等
			// 启动apk用户名为app_数字 如app_34
			Log.i(TAG, "uid:" + appInfo.uid);
			// 进程的名字
			Log.i(TAG, "processName:" + appInfo.processName);
			Log.i(TAG, "pkgList:" + Arrays.asList(appInfo.pkgList));
			// importance翻译为重要性。app进程分为五个等级1前台 2可见 3后台服务 4后台进程 5空进程
			// http://www.cnblogs.com/angeldevil/archive/2013/05/21/3090872.html
			// 360设置等级为IMPORTANCE_PERCEPTIBLE，如何实现进程为130呢？当调用Service的startForeground后
			// 待验证
			Log.i(TAG, "importance:" + appInfo.importance);
			Log.i(TAG, "lru:" + appInfo.lru);
			Log.i(TAG, "------------------------------");
		}

		Log.i(TAG, "----------------RunningTaskInfo---" + runningTasks.size() + "------------------");
		for (RunningTaskInfo task : runningTasks) {
			Log.i(TAG, "id:" + task.id);
			Log.i(TAG, "baseActivity:" + task.baseActivity.getClassName());
			Log.i(TAG, "topActivity:" + task.topActivity.getClassName());
			Log.i(TAG, "numActivities:" + task.numActivities);
			Log.i(TAG, "numRunning:" + task.numRunning);
			Log.i(TAG, "description:" + task.description);
			Log.i(TAG,
					"thumbnail bitmap rowbytes:"
							+ (task.thumbnail == null ? "null" : task.thumbnail
									.getRowBytes()));
			Log.i(TAG, "------------------------------");
		}

		Log.i(TAG, "----------------RunningServiceInfo------" + runingServices.size() + "----------------");
		// pid:1288
		// uid:10149
		// activeSince:50620
		// clientCount:3
		// clientLabel:0
		// clientPackage:null
		// crashCount:0
		// flags:3
		// lastActivityTime:132668666
		// process:com.qihoo360.mobilesafe
		// restarting:0
		// foreground:true
		// service ComponentName
		// name:com.qihoo360.mobilesafe.service.SafeManageService
		// started:true
		for (RunningServiceInfo service : runingServices) {
			Log.i(TAG, "pid:" + service.pid);
			Log.i(TAG, "uid:" + service.uid);
			// 当服务是第一次活动的时间，或者有人启动或绑定到它。
			Log.i(TAG, "activeSince:" + service.activeSince);
			Log.i(TAG, "clientCount:" + service.clientCount);
			Log.i(TAG, "clientLabel:" + service.clientLabel);
			Log.i(TAG, "clientPackage:" + service.clientPackage);
			Log.i(TAG, "crashCount:" + service.crashCount);
			Log.i(TAG, "flags:" + service.flags);
			// 当有在服务上活动的时间（无论是 明确的要求来启动它，或者客户结合）
			Log.i(TAG, "lastActivityTime:" + service.lastActivityTime);
			// 该服务运行在那个进程中
			Log.i(TAG, "process:" + service.process);
			// 如果非零，此服务当前没有运行，但计划 在给定的时间重新启动。这里应该为重新运行时间
			Log.i(TAG, "restarting:" + service.restarting);
			// 设置为true，如果服务要求运行作为前台进程。
			Log.i(TAG, "foreground:" + service.foreground);
			// 这里比较重要我们可以通过包名结束她
			Log.i(TAG, "service ComponentName name:" + service.service.getClassName());
			// 是否通过started 来判断是否在运行
			// 设置为true，如果该服务已被明确地开始。
			Log.i(TAG, "started:" + service.started);
			Log.i(TAG, "------------------------------");
		}
	}

	private OptimaActivity getContext() {
		return this;
	}
}
