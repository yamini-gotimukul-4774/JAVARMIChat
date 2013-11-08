

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA. User: Yamini Date: 11/4/13 Time: 8:37 AM To
 * change this template use File | Settings | File Templates.
 */
public interface ChatRoomProvider extends Remote {

	ChatRoom createChatRoom(String name) throws RemoteException;

	boolean terminateChatRoom(Long id) throws RemoteException;
}
