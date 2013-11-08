

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Yamini Date: 11/4/13 Time: 5:36 PM To
 * change this template use File | Settings | File Templates.
 */
public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	private ChatRegistry chatRegistry;
	private ChatRoomProvider chatRoomProvider;

	private Map<Long, ChatRoom> joinedChatRooms = new HashMap<Long, ChatRoom>();
	private Map<Long, ChatRoom> ownedChatRooms = new HashMap<Long, ChatRoom>();
	private ChatClientUserInterface chatClientUserInterface;

	public ChatClientImpl(String name, ChatClientUserInterface chatClientUserInterface) throws RemoteException, NotBoundException, MalformedURLException {
		setName(name);
		id = new Date().getTime();
		System.setSecurityManager(new RMISecurityManager());
		Naming.rebind("ChatClient_" + id, this);
		chatRegistry = (ChatRegistry) Naming.lookup("rmi://localhost/ChatServer");
		chatRegistry.register(this);
		chatRoomProvider = (ChatRoomProvider) Naming.lookup("rmi://localhost/ChatRoomProvider");
		this.chatClientUserInterface = chatClientUserInterface;
		System.out.println("In the Chat Client Constructor");
	}

	@Override
	public void listen(ChatRoom chatRoom, String message) throws RemoteException {
		System.out.println("Chat Message with Prefix :: " + chatRoom.getName() + ":" + message);
		chatClientUserInterface.setMessageToTextArea(chatRoom.getName()+ ":" + message);
	}

	public List<ChatRoom> findRegisteredChatRooms() throws RemoteException {
		return chatRegistry.findRegisteredChatRooms();
	}

	public ChatRoom createChatRoom(String name) throws RemoteException {
		ChatRoom chatRoom = chatRoomProvider.createChatRoom(name);

		ownedChatRooms.put(chatRoom.getId(), chatRoom);
		/* The creator of the chat room should implicitly join the chat room */
		chatRoom.join(this);
		joinedChatRooms.put(chatRoom.getId(), chatRoom);
		System.out.println("Chat room created with an ID :" + chatRoom.getId() + " ChatRoom Name " + chatRoom.getName());
		return chatRoom;
	}

	public void join(Long id) throws RemoteException {
		System.out.println("Number of ChatRooms before joining : " + joinedChatRooms.size());
		ChatRoom chatRoom = findChatRoomById(id);
		System.out.println("joined id : " + id);
		if (chatRoom == null) {
			for (ChatRoom registeredChatRoom : findRegisteredChatRooms()) {
				if (registeredChatRoom.getId().equals(id)) {
					joinedChatRooms.put(id, registeredChatRoom);
					registeredChatRoom.join(this);
					break;
				}
			}
		}
		if (chatRoom != null) {
			System.out.println("Chat Room joined " + chatRoom.getName());
		}
		System.out.println("Number of ChatRooms after joining : " + joinedChatRooms.size());

	}

	public void talk(Long id, String message) throws RemoteException {
		ChatRoom chatRoom = findChatRoomById(id);
		if (chatRoom != null) {
			chatRoom.talk(message);
		}
	}

	public void leave(Long id) throws RemoteException {
		System.out.println("Number of ChatRooms before leaving : " + joinedChatRooms.size());
		ChatRoom chatRoom = findChatRoomById(id);
		if (chatRoom != null) {
			chatRoom.leave(id);
			removeChatRoom(id);
		}
		System.out.println("Number of ChatRooms after leaving : " + joinedChatRooms.size());
	}

	public void terminateChatRoom(Long id) throws RemoteException {
		ChatRoom chatRoom = findChatRoomById(id);
		System.out.println("In terminate the chatroom with id :" + id + " :" + chatRoom);

		if (chatRoom != null) {
			chatRoomProvider.terminateChatRoom(id);
			removeChatRoom(id);
		}
	}

	private ChatRoom findChatRoomById(Long id) {
		ChatRoom chatRoom = joinedChatRooms.get(id);
		if (chatRoom == null) {
			chatRoom = ownedChatRooms.get(id);
		}

		return chatRoom;
	}

	private void removeChatRoom(Long id) {
		ChatRoom chatRoom = joinedChatRooms.get(id);
		if (chatRoom != null) {
			joinedChatRooms.remove(id);
		}

		chatRoom = ownedChatRooms.get(id);
		if (chatRoom != null) {
			ownedChatRooms.remove(id);
		}
	}

	public Map<Long, ChatRoom> getJoinedChatRooms() throws RemoteException {
		return joinedChatRooms;
	}

	public void setJoinedChatRooms(Map<Long, ChatRoom> joinedChatRooms) {
		this.joinedChatRooms = joinedChatRooms;
	}

	public Map<Long, ChatRoom> getOwnedChatRooms() throws RemoteException {
		return ownedChatRooms;
	}

	public void setOwnedChatRooms(Map<Long, ChatRoom> ownedChatRooms) {
		this.ownedChatRooms = ownedChatRooms;
	}

	@Override
	public Long getId() throws RemoteException {
		return id;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ChatClientImpl that = (ChatClientImpl) o;

		if (!id.equals(that.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public static void main(String args[]) throws Exception {
		Long id = null;
		ChatClientImpl chatClient = new ChatClientImpl(args[0], null);
		ChatRoom chatRoom = null;
		System.out.println("Enter a name for the chat room : ");
		String name = System.console().readLine();
		try {
			id = Long.valueOf(name);
			chatClient.join(id);
		} catch (NumberFormatException e) {
			chatRoom = chatClient.createChatRoom(name);
		}

		//chatClient.talk(id, System.console().readLine());
		//chatClient.listen(chatRoom, System.console().readLine());

	}

}
