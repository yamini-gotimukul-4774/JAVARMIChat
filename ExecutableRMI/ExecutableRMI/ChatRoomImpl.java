

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Yamini Date: 11/4/13 Time: 5:12 PM To
 * change this template use File | Settings | File Templates.
 */
public class ChatRoomImpl extends UnicastRemoteObject implements ChatRoom {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Map<Long, ChatClient> chatClientMap = new HashMap<Long, ChatClient>();

	public ChatRoomImpl(String name) throws MalformedURLException, RemoteException {
		setName(name);
		id = new Date().getTime();
		System.setSecurityManager(new RMISecurityManager());
		Naming.rebind("ChatRoom_" + id, this);
	}

	@Override
	public synchronized void join(ChatClient chatClient) throws RemoteException {
		System.out.println(chatClient.getName() + "has joined the chat Room");
		chatClientMap.put(chatClient.getId(), chatClient);
		talk(chatClient.getName() + " has joined the chat room");
	}

	@Override
	public synchronized void talk(String message) throws RemoteException {
		
		for (ChatClient chatClient : chatClientMap.values()) {
			chatClient.listen(this, message);
		}
	}

	@Override
	public synchronized void leave(Long id) throws RemoteException {
		chatClientMap.remove(id);
	}

	@Override
	public void destroy() throws RemoteException {
		for (ChatClient chatClient : chatClientMap.values()) {
			chatClient.leave(this.id);
		}
	}

	@Override
	public Long getId() {
		return id; // To change body of implemented methods use File | Settings
					// | File Templates.
	}

	@Override
	public String getName() {
		return name; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ChatRoomImpl chatRoom = (ChatRoomImpl) o;

		if (!id.equals(chatRoom.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
