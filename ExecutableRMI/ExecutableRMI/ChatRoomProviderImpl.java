

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: Yamini Date: 11/4/13 Time: 4:50 PM To
 * change this template use File | Settings | File Templates.
 */
public class ChatRoomProviderImpl extends UnicastRemoteObject implements ChatRoomProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChatRegistry chatRegistry = null;
	private Set<ChatRoom> chatRoomList = new HashSet<ChatRoom>();

	public ChatRoomProviderImpl() throws RemoteException, MalformedURLException, NotBoundException {
		System.setSecurityManager(new RMISecurityManager());
		chatRegistry = (ChatRegistry) Naming.lookup("rmi://localhost/ChatServer");
		Naming.rebind("ChatRoomProvider", this);
	}

	@Override
	public ChatRoom createChatRoom(String name) throws RemoteException {
		ChatRoom chatRoom = null;
		try {
			chatRoom = new ChatRoomImpl(name);
			if (chatRegistry.register(chatRoom)) {
				chatRoomList.add(chatRoom);
				return chatRoom;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return null;
	}

	@Override
	public boolean terminateChatRoom(Long id) throws RemoteException {
		ChatRoom entity = null;
		for(ChatRoom chatRoom: chatRoomList) {
			if(chatRoom.getId().equals(id)){
				chatRoom.destroy();
				entity = chatRoom;
			}
		}
		chatRoomList.remove(entity);
		return chatRegistry.deRegister(entity);
	}
	public static void main(String args[]) throws Exception {
		ChatRoomProviderImpl chatRoomProviderImpl = new ChatRoomProviderImpl();

	}

}
