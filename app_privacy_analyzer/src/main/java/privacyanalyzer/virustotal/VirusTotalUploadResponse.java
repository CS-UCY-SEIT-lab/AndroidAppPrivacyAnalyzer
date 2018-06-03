package privacyanalyzer.virustotal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VirusTotalUploadResponse {

	@SerializedName("scan_id")
	@Expose
	private String scanId;
	@SerializedName("sha1")
	@Expose
	private String sha1;
	@SerializedName("resource")
	@Expose
	private String resource;
	@SerializedName("response_code")
	@Expose
	private Integer responseCode;
	@SerializedName("sha256")
	@Expose
	private String sha256;
	@SerializedName("permalink")
	@Expose
	private String permalink;
	@SerializedName("md5")
	@Expose
	private String md5;
	@SerializedName("verbose_msg")
	@Expose
	private String verboseMsg;

	public String getScanId() {
		return scanId;
	}

	public void setScanId(String scanId) {
		this.scanId = scanId;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getVerboseMsg() {
		return verboseMsg;
	}

	public void setVerboseMsg(String verboseMsg) {
		this.verboseMsg = verboseMsg;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("\nscanId", scanId).append("\nsha1", sha1).append("\nresource", resource)
				.append("\nresponseCode", responseCode).append("\nsha256", sha256).append("\npermalink", permalink)
				.append("\nmd5", md5).append("\nverboseMsg", verboseMsg).toString();
	}

}