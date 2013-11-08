

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Yamini
 * Date: 11/4/13
 * Time: 8:09 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ChatRegistry extends Remote {

    Boolean register(Entity entity) throws RemoteException;

    Boolean deRegister(Entity entity) throws RemoteException;

    Entity getInfo(long id) throws RemoteException;

    List<ChatRoom> findRegisteredChatRooms() throws RemoteException;
}
