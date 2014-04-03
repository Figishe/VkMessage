package eltech.vkmessage.connection;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VkApiConnection {
	private final String accessToken;
	
	private final int appId; // id приложения на сайте vk.com 
	private final int userId; // id пользователя, от имени которого будут выполняться методы API
	private final String apiURI; 
	
	private final HttpClient httpClient;
	
	private long lastMethodCallTime;
	private final int delayBetweenMethodCalls = 334;

	VkApiConnection(int appId, int userId, String accessToken, String apiURI) {
		this.appId = appId;
		this.accessToken = accessToken;
		this.apiURI = apiURI;
		this.userId = userId;
		lastMethodCallTime = 0;
		this.httpClient = new DefaultHttpClient();
	}
	
	public synchronized JSONObject executeApiMethod(String method, String args) throws VkApiMethodException {
		HttpPost post = new HttpPost(apiURI + method + "?" + args + "&v=5.14&&access_token=" + accessToken);
		
		/*
		 * Обеспечивается задержка между вызовами методов API для избежания проблемы
		 * "Too many requests per second" - vk допускает не более 3 запросов в секунду.
		 */
		long lastCallCooldownValue = Calendar.getInstance().getTimeInMillis() - lastMethodCallTime;
		if (lastCallCooldownValue < delayBetweenMethodCalls) {
			try {
				Thread.sleep(delayBetweenMethodCalls - lastCallCooldownValue);
			} catch (InterruptedException e) {/* NOP */} 
		}
		
		HttpResponse response;
		try {
			response = httpClient.execute(post);
		} catch (ClientProtocolException cpe) {
			throw new VkApiMethodException("Can't get response from vk.com", cpe);
		} catch (IOException ioe) {
			throw new VkApiMethodException("Can't get response from vk.com", ioe);
		}
		lastMethodCallTime = Calendar.getInstance().getTimeInMillis();
		
		JSONObject result = null;
		try {
			try {
				result = new JSONObject(VkApi.converHttpEntityToString(response.getEntity()));
			} catch (IOException e) {
				throw new VkApiMethodException("Can't convert http entity to string: " + response.getEntity(), e);
			}
			if (result.has("error")) {
				JSONObject error = result.getJSONObject("error");
				throw new VkApiMethodException(error.getInt("error_code"), error.getString("error_msg"), error.getString("request_params"));
			}
		} catch (JSONException je) {
			throw new VkApiMethodException("Can't parse JSON object using response from vk.com", je);
		} catch (Error e) {
			System.err.println("Error context in VkApiConnection.executeApiMethod: method = " + method + ", args = " + args + ", time = " + Calendar.getInstance().getTime());
			throw e; 
		}
		
				
		return result;
	}
	
	public synchronized JSONObject executeApiMethod(String method) throws VkApiMethodException {
		try {
			return executeApiMethod(method, "");
		} catch (VkApiMethodException e) {
			throw e;
		}
	}

	public int getAppId() {
		return appId;
	}
	
	public int getUserId() {
		return userId;
	}
}
