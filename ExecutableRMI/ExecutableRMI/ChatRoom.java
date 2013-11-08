

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA. User: Yamini Date: 11/4/13 Time: 8:10 AM To
 * change this template use File | Settings | File Templates.
 */
public interface ChatRoom extends Entity, Remote {
	void join(ChatClient chatClient) throws RemoteException;

	void talk(String message) throws RemoteException;

	void leave(Long id) throws RemoteException;

	void destroy() throws RemoteException;
}
