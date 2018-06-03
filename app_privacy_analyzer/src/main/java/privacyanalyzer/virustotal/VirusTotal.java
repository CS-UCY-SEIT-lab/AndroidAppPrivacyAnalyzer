package privacyanalyzer.virustotal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class VirusTotal {

	private final String requestReportUrl = "https://www.virustotal.com/vtapi/v2/file/report";
	private final String uploadAndScanUrl = "https://www.virustotal.com/vtapi/v2/file/scan";
	private final String apiKey;

	/**
	 * @param apiKey
	 */
	public VirusTotal(String apiKey) {
		this.apiKey = apiKey;
	}

	public VirusTotalReportResponse requestReportBySHA256(String sha256) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(requestReportUrl);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("apikey", this.apiKey));
		params.add(new BasicNameValuePair("resource", sha256));
		params.add(new BasicNameValuePair("allinfo", "false"));
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		CloseableHttpResponse response = client.execute(httpPost);
		// System.out.println(response.getStatusLine().getStatusCode());
		int responseCode = response.getStatusLine().getStatusCode();
		if (responseCode == 200) {

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			// System.out.println(result.toString());
			client.close();

			Gson gson = new Gson();
			VirusTotalReportResponse vtr = gson.fromJson(result.toString(), VirusTotalReportResponse.class);

			return vtr;
		} else if (responseCode == 204 || responseCode >= 400) {
			VirusTotalReportResponse vtr = new VirusTotalReportResponse();
			vtr.setResponseCode(responseCode);
			return vtr;
		}
		return null;
	}

	public VirusTotalUploadResponse uploadAndScanAPK(String apkPath) throws ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(uploadAndScanUrl);

		FileBody bin = new FileBody(new File(apkPath));
		// the API key here
		// StringBody comment = new
		// StringBody("4a71f3d4c1ba14831e8edce8b32261d2f7f61757761e2e838e84ec911a018e37",
		// ContentType.TEXT_PLAIN);

		HttpEntity reqEntity = MultipartEntityBuilder.create()
				.addPart("apikey", new StringBody(this.apiKey, ContentType.TEXT_PLAIN)).addPart("file", bin).build();

		httppost.setEntity(reqEntity);

		// System.out.println("executing request " + httppost.getRequestLine());
		CloseableHttpResponse response = httpclient.execute(httppost);
		VirusTotalUploadResponse vtpr = null;
		// System.out.println("----------------------------------------");
		// System.out.println(response.getStatusLine());
		int responseCode = response.getStatusLine().getStatusCode();
		if (responseCode == 200) {

			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				// System.out.println("ToString:" + EntityUtils.toString(resEntity));
			}
			Gson gson = new Gson();
			vtpr = gson.fromJson(EntityUtils.toString(resEntity), VirusTotalUploadResponse.class);
			EntityUtils.consume(resEntity);
		} else if (responseCode == 204 || responseCode >= 400) {
			vtpr= new VirusTotalUploadResponse();
			vtpr.setResponseCode(responseCode);
			
		}

		response.close();
		httpclient.close();
		return vtpr;
	}

}
