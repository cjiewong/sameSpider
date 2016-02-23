package com.stone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.swing.JTextArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author c
 *2016-02-20 17:29:40
 *抓取same频道中的留言并存储到数据库
 */
public class SameImplBean
{
	//private static Parameter para = ParameterFactory.getParameter(true);
	static String serviceUrl = "http://v2.same.com/channel/{chanelId}/senses";//same域名
	static String domain = "http://v2.same.com";
	static String searchChannelIdUrl = "http://search.ohsame.com/s.php?keyword={channelName}&limit=10&realtime=1&type=1&user_id=";
	
	public static void main(String[] args) throws Exception
	{
		

	}


	


	/**
	 * @throws Exception 
	 */
	/**
	 * @param channel_name 频道名称
	 * @param nextUrl 下一页的地址
	 * @param path 图片存放路径
	 * @throws Exception
	 */
	public static void spiderSame(String channel_name,String nextUrl,String path,JTextArea ja) throws Exception {	
			int channel_id = getChannelId(channel_name);
			if(channel_id==-1)
				throw new Exception("频道未找到!请重新输入");
			String theUrl;
			if(nextUrl==null || "".equals(nextUrl)){
				theUrl= serviceUrl.replace("{chanelId}", channel_id+"");
			}else{
				theUrl = nextUrl;
			}
			System.out.println(theUrl);
			URL url = new URL(theUrl);
			String str = getJsonStr(url);
			JSONObject data =  JSONObject.fromObject(str).getJSONObject("data");
			//next为滑动查看更多留言时候刷新的网页地址(带了补偿参数offset)，当没有下一页时候没有该参数，即next=""
			String next = "";
			if(data.containsKey("next")){
				next= data.getString("next");
			}
			
			JSONArray results = data.getJSONArray("results");
			for(int i = 0;i<results.size();i++){
				JSONObject jsonObj = results.getJSONObject(i);
				//留言的id
				int messageId = jsonObj.getInt("id");
				//txt为该频道下发表的留言的文本，当文本不存在时为空字符串""
				String txt = jsonObj.getString("txt");
				//photo为该频道下发表的图片的地址
				String photo="";
				if(jsonObj.containsKey("photo")){
					photo = jsonObj.getString("photo");
					dowloadPic(photo, messageId,path,ja);
				}
				long created_at = jsonObj.getLong("created_at");
				JSONObject channelObj = jsonObj.getJSONObject("channel");
				String channelName = "";//频道名字
				String channelId = "";//频道ID
				if(i==0){
					channelName = channelObj.getString("name");
					channelId = channelObj.getString("id");
				}
				//该频道下该条留言发布者的ID,HQQ的id为1120542，cjie的id为3165927
				JSONObject user = jsonObj.getJSONObject("user");
				int userId = user.getInt("id");
				String userName = "";
				if(user.containsKey("username")){
					userName = user.getString("username");
				}
				
			}
			
			//Thread.sleep(3000);
			if(!"".equals(next)){
				String nextUrl1 = domain+next;
				spiderSame(channel_name,nextUrl1,path,ja);
			}
	}



	private static int getChannelId(String channelName) throws ProtocolException, MalformedURLException, IOException{
		int channelId=-1;
		searchChannelIdUrl = searchChannelIdUrl.replace("{channelName}", urlEncodeUTF8(channelName));
		String json = getJsonStr(new URL(searchChannelIdUrl));
		JSONArray jsonArr = JSONArray.fromObject(json);
		//System.out.println(jsonArr);
		if (jsonArr.size()>0){
			
			JSONObject jsonObj = (JSONObject) jsonArr.get(0);
			channelId = jsonObj.getInt("channel_id")     ;
		}
		
		return channelId;
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private static String getJsonStr(URL url) throws IOException,
			ProtocolException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		InputStream is = conn.getInputStream();
		String str = IOUtils.toString(is,"utf-8");
		is.close();
		return str;
	}
	
    public static String urlEncodeUTF8(String source) {  
        String result = source;  
        try {  
            result = java.net.URLEncoder.encode(source, "utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
    
	private static void dowloadPic(final String photo,final int messageId,String path,JTextArea ja) {

		
		File file = null;
		InputStream is;
		FileOutputStream fos;
			try {
				URL url = new URL(photo);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(3000);
				conn.setReadTimeout(20000);
				conn.connect();
				if(conn.getResponseCode()==200){
					 is = conn.getInputStream();
				
					//String path = "D:/project/MyTasks/others/samePics/";
					String fileName = messageId+".jpg";
					file = new File(path+"/"+fileName);
					 fos = new FileOutputStream(file);
					IOUtils.copy(is, fos);
					ja.append("下载图片"+file.getName()+"成功.\r\n");
					is.close();
					fos.close();
}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					FileUtils.forceDelete(file);
					ja.append("下载图片"+file.getName()+"失败，准备删除该图片.\r\n");

				} catch (IOException e1) {
					e1.printStackTrace();
					ja.append("删除图片"+file.getName()+"失败.\r\n");

				}
			}  
	}
}
