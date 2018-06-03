package privacyanalyzer.functionalities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.io.LineIterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import privacyanalyzer.backend.data.ApplicationPermissionsModel;
import privacyanalyzer.backend.data.LibraryModel;
import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.PermissionMethodCallModel;
import privacyanalyzer.ui.util.Paths;

public class APKAnalyzer {

	public LibraryModel[] getLibrariesPermissions(String apkPath) throws IOException {
		String run = "python " + Paths.liteRadarPath + " -f " + apkPath + "";
		Process p = Runtime.getRuntime().exec(run);
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder sb = new StringBuilder();

		String line = null;

		while ((line = in.readLine()) != null) {
			// System.out.println(line);
			sb.append(line + "\n");
		}

		String x = sb.toString();
		// System.out.println(x);

		LibraryModel[] libModels = null;
		Gson g = new Gson();
		libModels = g.fromJson(x, LibraryModel[].class);

		// System.out.println("PRINT "+libModels[0].Library);
		return libModels;
	}

	public ApplicationPermissionsModel getAPKPermissions(String apkPath) throws IOException, InterruptedException {

		String run = "java -jar " + Paths.permissionCheckerPath + " " + apkPath + "";

		Process p = Runtime.getRuntime().exec(run);

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder sb = new StringBuilder();

		BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line = null;

		/*
		 * while ((line = err.readLine()) != null) {
		 * System.out.println("ERROR LINE: *"+line+"*"); // sb.append(line + "\n"); }
		 */
		line = null;

		while ((line = in.readLine()) != null) {

			sb.append(line + "\n");
		}

		String x = sb.toString();
		// System.out.println("Permissions from .jar:\n"+data);
		Gson g = new Gson();

		ApplicationPermissionsModel ret = g.fromJson(x, ApplicationPermissionsModel.class);
		// System.out.println(p.declared.toString());

		return ret;

	}

	public ApkModel getApkInformation(String apkPath) throws IOException {

		String run = "python " + Paths.pythonScript + " -m analyze -f " + apkPath + "";
		//System.out.println(run);
		Process p = Runtime.getRuntime().exec(run);
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder sb = new StringBuilder();

		BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line = null;
		while ((line = err.readLine()) != null) {
			 //System.out.println("ERROR= "+line);
			// sb.append(line + "\n");
		}
		line = null;

		while ((line = in.readLine()) != null) {
			 //System.out.println("LINE= "+line);
			sb.append(line + "\n");
		}

		String x = sb.toString();
		// System.out.println(x);
		Gson g = new Gson();
		ApkModel apk = g.fromJson(x, ApkModel.class);
		return apk;
	}

	public ArrayList<PermissionMethodCallModel> getCalls(String apkPath, ArrayList<String> permissionList)
			throws IOException, InterruptedException {

		String pList = "";
		for (int i = 0; i < permissionList.size(); i++) {
			pList = pList + permissionList.get(i) + ",";
		}

		String run = "python " + Paths.pythonScript + " -m call -f " + apkPath + " -p " + pList;
		//System.out.println(run);
		Process p = Runtime.getRuntime().exec(run);

		/*
		 * BufferedReader in = new BufferedReader(new
		 * InputStreamReader(p.getInputStream())); StringBuilder sb = new
		 * StringBuilder();
		 * 
		 * BufferedReader err = new BufferedReader(new
		 * InputStreamReader(p.getErrorStream())); String line = null;
		 * System.out.println("1"); while (err.ready() &&(line = err.readLine()) !=
		 * null) { // System.out.println("*"+line+"*"); // sb.append(line + "\n"); }
		 * 
		 * 
		 * 
		 * p.waitFor(); line = null; System.out.println("11"); while (in.ready() &&(line
		 * = in.readLine()) != null) {
		 * 
		 * // System.out.println(line); sb.append(line + "\n"); }
		 * 
		 * String x = sb.toString(); System.out.println("111");
		 */
		StringBuilder sb = new StringBuilder();

		Thread errThread = new Thread(() -> {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
				String line = null;
				while ((line = reader.readLine()) != null) {

					// System.out.println("*ERROR LINE* "+line);
					// sb.append(line + "\n");
				}
			} catch (Exception e) {
			}
		});
		errThread.start();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			String line = null;
			while ((line = reader.readLine()) != null) {

				// System.out.println(line);
				sb.append(line + "\n");
			}
		} catch (Exception e) {
		}
		// we got an end of file, so there can't be any more input. Now we need to wait
		// for stderr/process exit.

		int exitCode = -1;
		try {
			exitCode = p.waitFor();
			errThread.join();
		} catch (Exception e) {
		}
		String x = sb.toString();
		Gson g = new Gson();
		ArrayList<PermissionMethodCallModel> calls = g.fromJson(x,
				new TypeToken<ArrayList<PermissionMethodCallModel>>() {
				}.getType());

		if (calls == null) {
			//System.out.println("null");
			return null;
		}
		// System.out.println("ret1");
		return uniqueList(calls);

	}

	private ArrayList<PermissionMethodCallModel> uniqueList(ArrayList<PermissionMethodCallModel> list) {
		if (list == null)
			return null;
		ArrayList<PermissionMethodCallModel> x = new ArrayList<PermissionMethodCallModel>();
		for (PermissionMethodCallModel pmc : list) {
			if (!x.contains(pmc)) {
				x.add(pmc);
			}

		}
		return x;

	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * // ArrayList<String> permissionList= new ArrayList<String>(); //
	 * permissionList.add("android.permission.CHANGE_WIFI_STATE"); //
	 * permissionList.add("android.permission.ACCESS_WIFI_STATE"); //
	 * ArrayList<PermissionMethodCallModel> x=new //
	 * APKAnalyzer().getCalls("apks/app2.apk", permissionList);
	 * 
	 * // System.out.println(x.toString());
	 * 
	 * //new APKAnalyzer().getApkInformation("apks/app2.apk");
	 * 
	 * }
	 */
}