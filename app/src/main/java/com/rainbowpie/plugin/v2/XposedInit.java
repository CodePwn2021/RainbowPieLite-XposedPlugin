package com.rainbowpie.plugin.v2;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.rainbowpie.plugin.v2.Utils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;


public class XposedInit implements IXposedHookLoadPackage {

	final int expireTime = Integer.parseInt(Utils.expireTime);
	final String expireTimeString = Utils.expireTimeString;
	final String rnMD5 = Utils.rnMD5;
	final int rnSupportVersion = Integer.parseInt(Utils.rnSupportVersion);
	// /files/games/com.netease/rn

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

		if (lpparam.packageName.equals("com.rainbowpie.plugin.v2")) {
			XposedHelpers.findAndHookMethod(lpparam.packageName + ".ui.activity.MainActivity", lpparam.classLoader, "isModuleActivate", XC_MethodReplacement.returnConstant(true));
			return;
		}
		if (lpparam.appInfo == null || TextUtils.isEmpty(lpparam.appInfo.nativeLibraryDir)) {
			return;
		}
		File file = new File(lpparam.appInfo.nativeLibraryDir, "libminecraftpe.so");
		if (!file.exists()) {
			return;
		}
		if (lpparam.processName.contains(":")) {
			return;
		}
		try {
			Class NotStub = XposedHelpers.findClassIfExists("com.mojang.minecraftpe.AppContext", lpparam.classLoader);
			Class QihooStub = XposedHelpers.findClassIfExists("com.stub.StubApp", lpparam.classLoader);

			if (QihooStub != null) {
				StartHook(lpparam, "com.stub.StubApp", true, "attachBaseContext");
				XposedBridge.log("RainbowPie_Hook-XposedPlugin | 360 Stub!");
			} else if (NotStub != null) {
				StartHook(lpparam, "com.mojang.minecraftpe.AppContext", false, "attachBaseContext");
			} else {
				StartHook(lpparam, "com.netease.android.protect.StubApp", true, "attachBaseContext");
			}
		} catch (Exception e) {
			XposedBridge.log("RainbowPie_Hook-XposedPlugin | Find Error: " + e);
		}
	}

	private void StartHook(XC_LoadPackage.LoadPackageParam lpparam, String methodName, Boolean isStub, String attach) throws Throwable {
		XposedBridge.log("RainbowPie_Hook-XposedPlugin | Starting Load MinecraftCN Client: " + lpparam.packageName);

		final boolean FinalStub = isStub;

		XposedHelpers.findAndHookMethod(methodName, lpparam.classLoader, attach, Context.class, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					super.afterHookedMethod(param);//获取到Context对象，通过这个对象来获取classloader
					Context context = (Context) param.args[0];//获取classloader，之后hook加固后的代码就使用这个classloader
					ClassLoader classLoader = context.getClassLoader();//替换classloader,hook加固后的真正代码
					final Context contextzz = context;
					final Class clazzz = classLoader.loadClass("com.facebook.react.ReactInstanceManagerBuilder");
					final int mcVersion = Utils.getAppVersionCode(context, context.getPackageName());

					// Hook真正开始
					try {
						try {
							Utils.writeConfigs(contextzz);
						} catch (Exception e) {
							Toast.makeText(context, "RainbowPieLite: Unexpected error, stopped loading.\n" + e, Toast.LENGTH_SHORT).show();
							XposedBridge.log("RainbowPie_Hook-XposedPlugin | Cannot write config!");
							return;
						}

						int nowTimes = new Long(System.currentTimeMillis() / 1000).intValue();
						if (nowTimes > expireTime) {
							Toast.makeText(context, "RainbowPieLite: This module version is out of date, please go to the release group to install the latest version, the module has stopped loading.\nExpire: " + expireTimeString, Toast.LENGTH_SHORT).show();
							return;
						}
						// McVersion.startsWith
						if (!(mcVersion >= rnSupportVersion)) {
							Toast.makeText(context, "RainbowPieLite: Your version is NOT\nMinecraftCN 2.0, stopped loading!!!", Toast.LENGTH_SHORT).show();
							XposedBridge.log("RainbowPie_Hook-XposedPlugin | Not 2.0!");
							return;
						}
						// 校验md5
						try {
							if (!Utils.getFileMD5Myself("index.bundle").toString().equals(rnMD5)) {
								Toast.makeText(context, "RainbowPieLite: Do not change resource files!!!" + Utils.getFileMD5Myself("index.bundle"), Toast.LENGTH_SHORT).show();
								XposedBridge.log("RainbowPie_Hook-XposedPlugin | resource files has been changed!");
								return;
							}
						} catch (Exception e) {
							Toast.makeText(context, "RainbowPieLite: Do not delete resource files!\n" + e, Toast.LENGTH_SHORT).show();
							XposedBridge.log("RainbowPie_Hook-XposedPlugin | resource files has been deleted!");
							return;
						}
						XposedBridge.log("RainbowPie_Hook-XposedPlugin | Extract JSBundle");
						try {
							Utils.beExtractFile(context);
							File NoMediaFile = new File("/sdcard/RainbowPieFiles/", ".nomedia");
							if (NoMediaFile.exists()) {
								if (NoMediaFile.isDirectory()) {
									NoMediaFile.delete();
								}
								NoMediaFile.createNewFile();
							} else {
								NoMediaFile.createNewFile();
							}
						} catch (Exception e) {
							Toast.makeText(context, "RainbowPieLite: \n无法释放并读取资源，拒绝加载\n" + e, Toast.LENGTH_SHORT).show();
							XposedBridge.log("RainbowPie_Hook-XposedPlugin | Cannot extract resource files!");
							return;
						}

						XposedBridge.log("RainbowPie_Hook-XposedPlugin | Extract JSBundle SUCCESS!!!");
						// 拿到传过来的isStub来判断是否加固，没被加固就提示一下
						if (!(FinalStub)) {
							Toast.makeText(context, "RainbowPieLite: Hooked RN!\nXposed: RainbowPie6945\n- 您正在使用未加固客户端 -\nExpire: " + expireTimeString, Toast.LENGTH_SHORT).show();
							XposedBridge.log("RainbowPie_Hook-XposedPlugin | Not Stub Client!");
						} else {
							Toast.makeText(context, "RainbowPieLite: Hooked RN!\nXposed: RainbowPie6945", Toast.LENGTH_SHORT).show();
						}
						XposedBridge.log("RainbowPie_Hook-XposedPlugin | Start Hook Path!");

						XposedHelpers.findAndHookMethod("com.facebook.react.ReactInstanceManagerBuilder", classLoader, "setJSBundleFile", String.class, new XC_MethodHook() {
								@Override
								protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
									super.beforeHookedMethod(param);
									/*
									File clientPrivatePath = new File(contextzz.getFilesDir() + "/games/com.netease/", "rn");
									File clientPrivateRN = new File(clientPrivatePath, "index.bundle.inject");
									param.args[0] = clientPrivateRN.toString();
									*/
									File externalRN = new File("/sdcard/RainbowPieFiles/com.netease.x19_XposedModule/ReactNativeFiles/", "index.bundle");
									param.args[0] = externalRN.toString();
									XposedBridge.log("RainbowPie_Hook-XposedPlugin | Hook JSBundle SUCCESS!!!!");
								}
							});



						// 去除本地敏感词数据
						XposedHelpers.findAndHookMethod("com.netease.environment.model.DefaultRegex", classLoader, "getRegexObject", new XC_MethodHook() {
								@Override
								protected void afterHookedMethod(MethodHookParam param) throws Throwable {
									param.setResult(null);
								}
							});
						// 删除原有的敏感词文件夹
						File SensitiveDir = new File(contextzz.getFilesDir() + "/com");
						// XposedBridge.log("RainbowPie_Hook-XposedPlugin | SensitiveDir is "+SensitiveDir);
						if (SensitiveDir.exists()) {
							Utils.deleteFile(SensitiveDir);
						}
						// 禁止生成文件
						XposedBridge.hookMethod(XposedHelpers.findMethodExact(XposedHelpers.findClass("com.netease.environment.utils.FileUtils", classLoader), "readFile", String.class), new XC_MethodHook() {
								@Override
								protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
									param.setResult("");
								}
							});
						// Hook检查名字并输出到log
						XposedBridge.hookMethod(XposedHelpers.findMethodExact(XposedHelpers.findClass("com.netease.environment.EnvManager", classLoader), "reviewNickname", String.class), new XC_MethodHook() {
								@Override
								protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
									// XposedBridge.log("RainbowPie_Hook-XposedPlugin | reviewNickname: " + param.args[0]);
								}
							});
						// Hook掉世界名称检查输出到log
						XposedBridge.hookMethod(XposedHelpers.findMethodExact(XposedHelpers.findClass("com.netease.environment.EnvManager", classLoader), "reviewWords", String.class, String.class, String.class), new XC_MethodHook() {
								@Override
								protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
									//XposedBridge.log("RainbowPie_Hook-XposedPlugin | reviewWords: " + param.args[0] + " " + param.args[1] + " " + param.args[2]);
								}
							});
						XposedBridge.log("RainbowPie_Hook-XposedPlugin | Load Success!");

					} catch (Throwable e) {
						XposedBridge.log("RainbowPie_Hook-XposedPlugin | Load Error! : " + e);
					}
				}
			});
	}
}
