package au.com.vocus.ipfix;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class Main {

	public static IPFixProperty prop = new IPFixProperty();
	private static Date lastFileDate;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String login = "Basic " + new String(Base64.encodeBase64((prop.getUsername() + ":" + prop.getPassword()).getBytes()));
		Element dirListing = getFilenameList(prop.getServerURL(), login);
		
		lastFileDate = prop.getLastFileDate();
		List<RemoteFile> fileList = getRemoteFileList(dirListing);
				
		for(RemoteFile file : fileList) {
			
			System.out.println(file.getFilename() + " | " + file.getFilesize() + " | " + file.getCreatedDateStr());
			downloadFile(prop.getServerURL(), prop.getLocalPath(), file, login);
			lastFileDate = lastFileDate==null || file.getCreatedDate().after(lastFileDate) ? file.getCreatedDate() : lastFileDate;
		}
		
		logLastFileDate(lastFileDate);
		System.out.println("DONE!");
	}
		
	
	static Element getFilenameList(String url, String login) {
		
		Element element = null;
		try {
			Document doc = Jsoup.connect(url)
								.header("Authorization", login)
								.get();
			
			element = doc.body().getElementsByTag("table").get(0).getElementsByTag("tbody").get(0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return element;
	}
	
	
	static List<RemoteFile> getRemoteFileList(Element element) {
		List<RemoteFile> fileList = new ArrayList<RemoteFile>();
		
		for(Node row : element.childNodes()) {
			if(row.childNodes().size() < 5)
				continue;
			
			RemoteFile remoteFile = new RemoteFile(prop.getDateFormat());
			remoteFile.setFilelink(row.childNodes().get(RemoteFile.OrderSequence.LINK.getValue()).childNode(0));
			remoteFile.setCreatedDateStr(row.childNodes().get(RemoteFile.OrderSequence.DATE.getValue()).childNode(0).toString());
			remoteFile.setFilesize(row.childNodes().get(RemoteFile.OrderSequence.SIZE.getValue()).childNode(0).toString());
			
			//Skip header
			if(remoteFile.getCreatedDate() == null)
				continue;
			
			if(lastFileDate == null || remoteFile.getCreatedDate().after(lastFileDate)) {
				fileList.add(remoteFile);
			}
		}
		
		return fileList;
	}
	
	static void downloadFile(String remotePath, String localPath, RemoteFile fileInfo, String login) {
		
		try {
			
			String filepath = remotePath+fileInfo.getFilename();
			byte[] bytes = Jsoup.connect(filepath)
			        .header("Accept-Encoding", "gzip, deflate")
			        .header("Authorization", login)
			        .referrer(remotePath)
			        .ignoreContentType(true)
			        .maxBodySize(0)
			        .timeout(600000)
			        .execute()
			        .bodyAsBytes();
			
			FileOutputStream fos = new FileOutputStream(localPath + fileInfo.getFilename());
            fos.write(bytes);
            fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void logLastFileDate(Date datetime) {
		prop.setLastFileDate(datetime);
		prop.save();
	}
}
