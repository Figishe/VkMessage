package eltech.vkmessage.connection;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class VkApi { 
	
	private VkApi() {
	}
	
	
	public static class ConnectionBuilder {
		//required parameters
		private final int clientId;
		private final String login;
		private final String password;
		
		//optional parameters
		private String display = "mobile";
		private String redirectUri = "http://oauth.vk.com/blank.html";
		private String responseType = "token";
		private String scope = "";
		private String apiUri = "https://api.vk.com/method/";
		private String accessToken;
		
		
		public ConnectionBuilder(int clientId, String login, String password) {
			this.clientId = clientId;
			this.login = login;
			this.password = password;
		}
		
		public VkApiConnection build() throws IOException, VkNoConnectionExcepion  {
			
			HttpClient client = new DefaultHttpClient();

			/*
			 * now form and execute first post. Response will give the login form
			 */
			HttpPost post = new HttpPost("http://oauth.vk.com/authorize?" + "client_id="
					+ clientId + "&v=5.5&scope=" + scope + "&redirect_uri="
					+ redirectUri + "&display=" + display + "&response_type="
					+ responseType);
			HttpResponse response = client.execute(post);
			post.abort();
			/*
			 * parse POST-form from response
			 */
			String location = converHttpEntityToString(response.getEntity());
			/*
			 * parse "ip_h" ant "to" required for log in
			 */
			String ip_h = findKey(location, "name=\"ip_h\" value=\"", "\"");
			String to = findKey(location, "name=\"to\" value=\"", "\"");

			/*
			 * fill the login form and post it. The response will redirect to
			 * ACCESS_TOKEN obtaining; or to the permissions granting, if user runs
			 * the app for the first time, or the permissions have changed; or to the
			 * login form if either login or password are incorrect
			 */
			post = new HttpPost("https://login.vk.com/?act=login&soft=1&utf8=1"
					+ "&q=1" + "&ip_h=" + ip_h + "&_origin=http://oauth.vk.com"
					+ "&to=" + to + "&email=" + login + "&pass="
					+ URLEncoder.encode(password, "UTF-8"));
			response = client.execute(post);
			post.abort();
			location = response.getFirstHeader("location").getValue();
			post = new HttpPost(location);
			response = client.execute(post);
			post.abort();

			/*
			 * if it is redirect (response contains header "location") we got
			 * redirect to ACCESS_TOKEN obtaining; if not, it is POST form for
			 * permissions granting or the login form
			 */
			if (!response.containsHeader("location")) {
				try {
					location = converHttpEntityToString(response.getEntity());
				} catch (IOException ioe) {
					throw new VkAuthorizationException("Can't authorize: can't convert http entity \n"
							+ response.getEntity() 
							+ "\n to string");
				}
				location = findKey(location, " action=\"", "\"");
				/*
				 * recognize login form and throw exception
				 */
				if (location.startsWith("https://login.vk.com/?act=login&soft=1")) {
					throw new VkIncorrectLoginPasswordException("Can't authorize: incorrect login or password");
				}
			} else {
				location = response.getFirstHeader("location").getValue();
			}

			post = new HttpPost(location);
			
			response = client.execute(post);
			post.abort();
			
			// saving token
			location = response.getFirstHeader("location").getValue();
			accessToken = location.split("#")[1]
									.split("&")[0]
									.split("=")[1];
			int userId;
			try {
				userId = Integer.parseInt(location.split("#")[1]
						.split("&")[2]
						.split("=")[1]);
			} catch (NumberFormatException nfe) {
				System.err.println("location = " + location);
				throw new VkAuthorizationException("Can't authorize: can't parse user_id from response", nfe);
			}
			
			
			return new VkApiConnection(clientId, userId, accessToken, apiUri);
		}
		
//		public VkApiConnection buildManually() throws IOException, URISyntaxException  {
//			String url = "http://oauth.vk.com/authorize?" +
//			        "client_id="+clientId+
//			        "&scope="+scope+
//			        "&redirect_uri="+redirectUri+
//			        "&display="+display+
//			        "&v=5.5"+
//			        "&response_type="+responseType;
//			
//			Desktop.getDesktop().browse(new URI(url));
//			
//			JFrame tokenInputFrame = new JFrame();
//			tokenInputFrame.setSize(600, 80);
//			tokenInputFrame.setLocation(300, 540);
//			tokenInputFrame.setTitle("¬вод access_token");
//			final JTextField tokenInputTextField = new JTextField();
//			tokenInputTextField.addKeyListener(new KeyListener() {
//				@Override
//				public void keyTyped(KeyEvent e) {
//				}
//				@Override
//				public void keyReleased(KeyEvent e) {
//				}
//				@Override
//				public void keyPressed(KeyEvent e) {
//					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//						tokenInputTextField.setEditable(false);
//					}
//				}
//			});
//			tokenInputFrame.add(tokenInputTextField);
//			tokenInputFrame.setVisible(true);
//			
//			while (tokenInputTextField.isEditable()) {
//				try {
//					System.out.println("waiting for user input...");
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//				}
//			}
//			
//			String accessToken = tokenInputTextField.getText();
//			tokenInputFrame.dispose();
//			return new VkApiConnection(clientId, 1894810, accessToken, apiUri);
//		}
		
		public ConnectionBuilder setDisplay(String display) {
			this.display = display;
			return this;
		}

		public ConnectionBuilder setRedirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
			return this;
		}

		public ConnectionBuilder setResponseType(String responseType) {
			this.responseType = responseType;
			return this;
		}

		public ConnectionBuilder setScope(String scope) {
			this.scope = scope;
			return this;
		}

		public ConnectionBuilder setApiUri(String apiUri) {
			this.apiUri = apiUri;
			return this;
		}
	}
	
	private static String findKey(String source, String patternbegin, String patternend) {
		int startkey = source.indexOf(patternbegin);
		if (startkey > -1) {
			int stopkey = source.indexOf(patternend,
					startkey + patternbegin.length());
			if (stopkey > -1) {
				String key = source.substring(startkey + patternbegin.length(),
						stopkey);
				return key;
			}
		}
		return null;
	}
	
	static String converHttpEntityToString(HttpEntity ent) throws IOException {
		if (ent == null) {
			throw new IllegalArgumentException("entity cant be null");
		}
		BufferedInputStream bis = null;
		StringBuilder sb = new StringBuilder();
		try {
			bis = new BufferedInputStream(ent.getContent());
			byte[] buffer = new byte[1024];
			int count;
			while ((count = bis.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, count, "utf-8"));
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
		return sb.toString();
	}
}
