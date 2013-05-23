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
	private Timeline tm = null;

	private Paging mp = new Paging();

	private String content = null;

	public GetWeibo(String token) {
		String access_token = token;
		tm = new Timeline();
		tm.client.setToken(access_token);
	}

	/**
	 * 
	 * @param sinceId
	 *            get weibo from the sinceId
	 * @param fileName
	 *            file to store weibo status
	 * @param watiTime
	 *            wait time
	 */
	public void getWeibo(long sinceId, String fileName, int watiTime) {
		content = null;
		if (sinceId != 0) {
			mp.setSinceId(sinceId);
		}
		mp.setCount(200);
		try {
			StatusWapper status = tm.getHomeTimeline(0, 0, mp);
			for (Status s : status.getStatuses()) {
				Log.logInfo(s.toString());
				content += s.toString() + "\n";
			}
			sinceId = getSinceID(status.getStatuses(), sinceId);

			if (content!=null) {
				appendContent(fileName, content);

			}
			try {
				Thread.sleep(watiTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			getWeibo(sinceId, fileName, watiTime);
		} catch (WeiboException e) {
			e.printStackTrace();
			if (sinceId != 0) {
				getWeibo(sinceId, fileName, watiTime);
			} else {
				getWeibo(sinceId, fileName, watiTime);
			}
		}
	}

	private long getSinceID(List<Status> ls, long oldSinceId) {
		long id = 0;
		if (ls.isEmpty()) {
			return oldSinceId;
		} else {
			String s = ls.get(0).toString();
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
