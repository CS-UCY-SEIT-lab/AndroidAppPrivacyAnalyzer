package privacyanalyzer.virustotal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VirusTotalReportResponse {

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
	@SerializedName("scan_date")
	@Expose
	private String scanDate;
	@SerializedName("permalink")
	@Expose
	private String permalink;
	@SerializedName("verbose_msg")
	@Expose
	private String verboseMsg;
	@SerializedName("total")
	@Expose
	private Integer total;
	@SerializedName("positives")
	@Expose
	private Integer positives;
	@SerializedName("sha256")
	@Expose
	private String sha256;
	@SerializedName("md5")
	@Expose
	private String md5;

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

	public String getScanDate() {
		return scanDate;
	}

	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getVerboseMsg() {
		return verboseMsg;
	}

	public void setVerboseMsg(String verboseMsg) {
		this.verboseMsg = verboseMsg;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPositives() {
		return positives;
	}

	public void setPositives(Integer positives) {
		this.positives = positives;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("\nscanId", scanId).append("\nsha1", sha1)
				.append("\nresource", resource).append("\nresponseCode", responseCode).append("\nscanDate", scanDate)
				.append("\npermalink", permalink).append("\nverboseMsg", verboseMsg).append("\ntotal", total)
				.append("\npositives", positives).append("\nsha256", sha256).append("\nmd5", md5).toString();
	}
}