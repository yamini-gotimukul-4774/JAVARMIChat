

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Yamini Date: 11/4/13 Time: 8:10 AM To
 * change this template use File | Settings | File Templates.
 */
public interface ChatClient extends Entity, Remote {

	void listen(ChatRoom chatRoom,  String message) throws RemoteException;

	List<ChatRoom> findRegisteredChatRooms() throws RemoteException;

	ChatRoom createChatRoom(String name) throws RemoteException;

	void join(Long id) throws RemoteException;

	void talk(Long id, String message) throws RemoteException;

	void leave(Long id) throws RemoteException;

	void terminateChatRoom(Long id) throws RemoteException;

	Map<Long, ChatRoom> getJoinedChatRooms() throws RemoteException;

	Map<Long, ChatRoom> getOwnedChatRooms() throws RemoteException;
}
