package getWeibo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class GetWeibo {
	private  Timeline tm = null;

	
	public GetWeibo(String token) {
		String access_token = token;
		 tm = new Timeline();
		 tm.client.setToken(access_token);
	}

	public void getWeibo(long sinceId) {
		long sinceID = sinceId;
		Paging mp = new Paging();
		if (sinceID != 0) {
			mp.setSinceId(sinceID);
		}
		try {
			StatusWapper status = tm.getHomeTimeline(0, 0, mp);
			for (Status s : status.getStatuses()) {
				Log.logInfo(s.toString());
			}
			sinceID = getSinceID(status.getStatuses(),sinceId);	
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getWeibo(sinceID);
		} catch (WeiboException e) {
			e.printStackTrace();
			if(sinceID!=0){
			getWeibo(sinceID);
			}else{
				getWeibo(sinceId);
			}
		}
	}

	private long getSinceID(List<Status> ls,long oldSinceId) {
		long id = 0;
		if(ls.isEmpty()){
			return oldSinceId;
		}else{
			String s=ls.get(0).toString();
		String idch = s.substring(s.lastIndexOf("mid=") + 4,
				s.lastIndexOf("mid=") + 20);
		id = Long.parseLong(idch);
		return id;
		}
	}

	public static void appendContent(String fileName, String content) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
