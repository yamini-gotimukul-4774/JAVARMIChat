

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: Yamini
 * Date: 11/4/13
 * Time: 8:19 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Entity extends Serializable {

    Long getId() throws RemoteException;

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

}
