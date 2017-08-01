package au.com.vocus.ipfix;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Node;

public class RemoteFile {

	private Node filelink;
	private String filename;
	private String createdDateStr;
	private Date createdDate;
	private String filesize;
	
	private SimpleDateFormat df;
	
	public enum OrderSequence {
		IMG(0),
		LINK(1),
		DATE(2),
		SIZE(3),
		DESC(4);
		
		private int value;
		
		private OrderSequence(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	public RemoteFile(String dateFormat) {
		df = new SimpleDateFormat(dateFormat);
	}
	
	/**
	 * @return the filelink
	 */
	public Node getFilelink() {
		return filelink;
	}
	/**
	 * @param filelink the filelink to set
	 */
	public void setFilelink(Node filelink) {
		this.filelink = filelink;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		if(filename == null) {
			filename = filelink.attr("href");
		}
		
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the createdDateStr
	 */
	public String getCreatedDateStr() {
		return createdDateStr;
	}
	/**
	 * @param createdDateStr the createdDateStr to set
	 */
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		if(createdDate == null) {
			try {
				createdDate = df.parse(createdDateStr);
			} catch (Exception e) {
				return null;
			}
		}
		
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the filesize
	 */
	public String getFilesize() {
		return filesize;
	}
	/**
	 * @param filesize the filesize to set
	 */
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	
	
}
