package com.rainbowpie.plugin.v2;
import android.content.Context;
import android.content.pm.PackageInfo;
import de.robv.android.xposed.XposedBridge;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.nio.channels.FileChannel;

public class Utils {

    // 模块版本、RN支持版本定义
	public static String moduleVersionName = "1.2.41";
	public static String moduleVersionCode = "2000041";
	public static String rnSupportVersion = "840153450";
	
	// 过期时间定义
	public static String expireTime = "1644469628";
	public static String expireTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new Integer(Integer.parseInt(expireTime)).longValue() * 1000));
	
	// RN的md5校验定义
	public static String rnMD5 = "47524e093c5093351d0f6bfd016ec5ba";
	
    public static String newNetworkGet(String url) {
		StringBuffer buffer = new StringBuffer();
		InputStreamReader isr = null;
		try {
			URL urlObj = new URL(url);
			URLConnection uc = urlObj.openConnection();
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			isr = new InputStreamReader(uc.getInputStream(), "utf-8");
			BufferedReader reader = new BufferedReader(isr); //缓冲
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (Exception e) {
			XposedBridge.log("RainbowPie_Hook-XposedPlugin | HTTP&JSON Request Error!\n" + e);
		} finally {
			try {
				if (null != isr) {
					isr.close();
				}
			} catch (IOException e) {
				XposedBridge.log("RainbowPie_Hook-XposedPlugin | HTTP&JSON Request Error!\n" + e);
			}
		}
        if (buffer.length() == 0)return buffer.toString();
        buffer.delete(buffer.length() - 1, buffer.length());
        return buffer.toString();
	}

	public static int getAppVersionCode(Context context, String packageName) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            int version = pi.versionCode;
			return version;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

	// 遍历删除文件
	public static void deleteFile(File file) {
        try {
			if (file.exists()) {//判断文件是否存在
				if (file.isFile()) {//判断是否是文件
					file.delete();//删除文件
				} else if (file.isDirectory()) {//否则如果它是一个目录
					File[] files = file.listFiles();//声明目录下所有的文件 files[];
					for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
						deleteFile(files[i]);//把每个文件用这个方法进行迭代
                    }
					file.delete();//删除文件夹
                }
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取自己目录文件md5
    public static String getFileMD5Myself(String fileName) {
        try {
            BigInteger bi = null;
            byte[] buffer = new byte[1024 * 4];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            InputStream fis = XposedInit.class.getClassLoader()
                .getResourceAsStream("assets/" + fileName);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
            return bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

	// 获取文件md5
    public static String getMD5(File f) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi.toString(16);
    }
	// 写入模块版本配置文件
	public static void writeConfigs(Context ctx) throws IOException {
		File ConfigDir = new File(ctx.getFilesDir(), "RainbowPieFiles");
		File ConfigJSON = new File(ConfigDir, "module_version");
		FileWriter fwriter = null;
		try {
			if (!ConfigDir.isDirectory()) {
				if (ConfigDir.isFile()) {
					ConfigDir.delete();
				}
				ConfigDir.mkdirs();
			}
			if (!ConfigJSON.isDirectory()) {
				ConfigJSON.delete();
			}
			fwriter = new FileWriter(ConfigJSON, false);
			fwriter.write("{\"version_name\":\"" + moduleVersionName + "\",\"version_code\":" + moduleVersionCode + "}");
		} catch (Exception e) {
			throw new IOException("Cannot write config. Errors: \n" + e);
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void beExtractFile(Context ctx) {
		File externalDir = new File("/sdcard/RainbowPieFiles/com.netease.x19_XposedModule/", "ReactNativeFiles");
		File externalRN = new File("/sdcard/RainbowPieFiles/com.netease.x19_XposedModule/ReactNativeFiles/", "index.bundle");
		/*
		File clientPrivatePath = new File(ctx.getFilesDir() + "/games/com.netease/", "rn");
		File clientPrivateRN = new File(clientPrivatePath, "index.bundle.inject");
		*/
		try {
			// Extract external dir
			XposedBridge.log("RainbowPie_Hook-XposedPlugin | Start external dir check.");
			if (!externalDir.exists()) {
				XposedBridge.log("RainbowPie_Hook-XposedPlugin | External dir not found, starting make dir&extract.");
				externalDir.mkdirs();
				extractJSBundle(ctx, "index.bundle", externalDir);
			} else if (!externalRN.exists()) {
				XposedBridge.log("RainbowPie_Hook-XposedPlugin | External file not found, starting extract.");
				extractJSBundle(ctx, "index.bundle", externalDir);
			}
			/*
			// Extract private dir
			XposedBridge.log("RainbowPie_Hook-XposedPlugin | Start private dir check.");
			if (!clientPrivatePath.exists()) {
				XposedBridge.log("RainbowPie_Hook-XposedPlugin | Private dir not found, starting make dir&extract.");
				clientPrivatePath.mkdirs();
				copyFile(externalRN,clientPrivateRN);
			} else if (!clientPrivateRN.exists()) {
				XposedBridge.log("RainbowPie_Hook-XposedPlugin | Private file not found, starting extract.");
				copyFile(externalRN,clientPrivateRN);
			}
			XposedBridge.log("RainbowPie_Hook-XposedPlugin | Checking dir files.");
			if(!getMD5(externalRN).equals(getMD5(clientPrivateRN))) {
				XposedBridge.log("RainbowPie_Hook-XposedPlugin | Private file not latest, copied from external.");
				copyFile(externalRN,clientPrivateRN);
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 复制文件
	private static void copyFile(File source, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		}finally{
			sourceChannel.close();
			destChannel.close();
		}
	}
	
    //解压jsbundle
    public static File extractJSBundle(Context ctx, String fileName, File dir) throws IOException {
		String BundleName = fileName;
        if (!dir.isDirectory()) {
            if (dir.isFile()) {
                dir.delete();
            }
            dir.mkdirs();
        }
        File BundleFile = new File(dir, BundleName);
        if (BundleFile.exists()) {
            BundleFile.delete();
		}
		InputStream in = XposedInit.class.getClassLoader()
			.getResourceAsStream("assets/" + fileName);
		if (in == null) {
			throw new UnsatisfiedLinkError("File " + fileName + " not found. Are you deleted???");
		}
		//extract file
		BundleFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(BundleFile);
		byte[] buf = new byte[1024];
		int i;
		while ((i = in.read(buf)) > 0) {
			fout.write(buf, 0, i);
		}
		in.close();
		fout.flush();
		fout.close();
        return BundleFile;
    }

	/*
	 // 联网检查模块是否过期
	 public static Boolean isExpired(String moduleVersion) {
	 try {
	 String networkGets =Utils.newNetworkGet("https://wtf.wtf.com/RainbowPie/PluginExpire/?version=" + moduleVersion);
	 JSONObject obj = new JSONObject(networkGets);
	 int cloudExpireTime = obj.getInt("expire_time");
	 int nowTime = new Long(System.currentTimeMillis() / 1000).intValue();

	 if (nowTime > cloudExpireTime) {
	 return true;
	 } else {
	 return false;
	 }

	 } catch (Exception e) {
	 XposedBridge.log("RainbowPie_Hook-XposedPlugin | HTTP&JSON Request Error!\n" + e);
	 return true;
	 }
	 }
	 */

}
