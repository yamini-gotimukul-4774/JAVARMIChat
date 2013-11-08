

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: Yamini Date: 11/4/13 Time: 9:22 AM To
 * change this template use File | Settings | File Templates.
 */
public class ChatRegistryImpl extends UnicastRemoteObject implements ChatRegistry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ChatRoom> registeredChatRoomList = new ArrayList<ChatRoom>();
	private List<ChatClient> registeredChatClientList = new ArrayList<ChatClient>();

	public ChatRegistryImpl() throws RemoteException, MalformedURLException {
		super();
		System.setSecurityManager(new RMISecurityManager());
		Naming.rebind("ChatServer", this);
		System.out.println("In the Chat registry constructor");
	}

	@Override
	public Boolean register(Entity entity) throws RemoteException {
		if (entity instanceof ChatRoom) {
			Boolean isAlreadyRegistered = registeredChatRoomList.contains(entity);
			if (!isAlreadyRegistered) {
				registeredChatRoomList.add((ChatRoom) entity);
				return true;
			}
		} else {
			Boolean isAlreadyRegistered = registeredChatClientList.contains(entity);
			if (!isAlreadyRegistered) {
				registeredChatClientList.add((ChatClient) entity);
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean deRegister(Entity entity) throws RemoteException {
		Boolean status = false;
		if (entity instanceof ChatRoom) {
			status = registeredChatRoomList.contains(entity);
			if (status) {
				registeredChatRoomList.remove(entity);
			}
		} else {
			status = registeredChatClientList.contains(entity);
			if (status) {
				registeredChatClientList.remove(entity);
			}
		}
		return status;
	}

	@Override
	public Entity getInfo(long id) throws RemoteException {

		for (Entity entity : registeredChatRoomList) {
			if (entity.getId().equals(id)) {
				return entity;
			}
		}
		for (Entity entity : registeredChatClientList) {
			if (entity.getId().equals(id)) {
				return entity;
			}
		}
		return null;
	}

	@Override
	public List<ChatRoom> findRegisteredChatRooms() throws RemoteException {
		return registeredChatRoomList;
	}

	public static void main(String args[]) throws Exception {
		ChatRegistry chatRegistry = new ChatRegistryImpl();

	}

}
