

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ChatClientUserInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_sendMessage;
	private JTextField textField_newChatRoom;
	private String clientName;
	private String chatRoomName;
	private Long chatRoomId;
	private String sendMessage;
	private List<ChatRoom> chatRoomList;
	private JTextArea textArea_messaging;
	private JTextField textField_nickName;
	private JButton btnAvailableChatRooms;
	private JButton btn_joinChatRoom;
	private JButton btn_leaveChatRoom;
	private JButton btnCreateNewChat;
	private JButton btnSendMessage;
	private JButton btnCloseChatRoom;
	private JList list_AvailableChatRooms;
	private DefaultListModel<String> listAvailableChatRoomsModel;
	private DefaultListModel<String> listJoinedChatRoomsModel;
	private Set<Long> joinedChatRoomsSet;
	private ChatClient chatClient;
	private JList list_JoinedChatRooms;
	private JComboBox<String> ownedChatRoomList;
	private JComboBox<String> comboBox_JoinedChatRooms;

	public void setMessageToTextArea(String message) {
		textArea_messaging.append(message + "\n");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatClientUserInterface frame = new ChatClientUserInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 */
	public ChatClientUserInterface() throws RemoteException, MalformedURLException, NotBoundException {

		setTitle("My Chat Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 749, 685);
		contentPane = new JPanel();
		contentPane.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		textField_nickName = new JTextField();
		textField_nickName.setBounds(517, 14, 139, 20);
		contentPane.add(textField_nickName);
		textField_nickName.setColumns(10);

		JButton btnNewButton = new JButton("GO");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clientName = textField_nickName.getText();
				if (clientName != null & !clientName.isEmpty()) {

					btnCreateNewChat.setEnabled(true);
					btnAvailableChatRooms.setEnabled(true);
					try {
						chatClient = new ChatClientImpl(clientName, ChatClientUserInterface.this);
						chatRoomList = chatClient.findRegisteredChatRooms();
						listAvailableChatRoomsModel.clear();
						for (ChatRoom chatRoom : chatRoomList) {
							listAvailableChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
						}
						if (listAvailableChatRoomsModel.size() != 0) {

							btn_joinChatRoom.setEnabled(true);
						}
					} catch (RemoteException | MalformedURLException | NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		btnNewButton.setBounds(666, 11, 55, 25);
		contentPane.add(btnNewButton);

		btnAvailableChatRooms = new JButton("Refresh");
		btnAvailableChatRooms.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAvailableChatRooms.setEnabled(false);
		btnAvailableChatRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					chatRoomList = chatClient.findRegisteredChatRooms();
					listAvailableChatRoomsModel.clear();
					listJoinedChatRoomsModel.clear();
					comboBox_JoinedChatRooms.removeAllItems();

					for (ChatRoom chatRoom : chatRoomList) {
						listAvailableChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
					}
					if (listAvailableChatRoomsModel.size() != 0) {
						btn_joinChatRoom.setEnabled(true);
					} else {
						btn_joinChatRoom.setEnabled(false);
					}
					for (ChatRoom chatRoom : chatClient.getJoinedChatRooms().values()) {
						listJoinedChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
						comboBox_JoinedChatRooms.addItem(chatRoom.getName() + "_" + chatRoom.getId());
					}
					if (listJoinedChatRoomsModel.getSize() != 0) {
						btn_leaveChatRoom.setEnabled(true);
						btnSendMessage.setEnabled(true);
					} else {
						btn_leaveChatRoom.setEnabled(false);
						btnSendMessage.setEnabled(false);
					}

				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		btnAvailableChatRooms.setBounds(453, 192, 91, 23);
		contentPane.add(btnAvailableChatRooms);

		textArea_messaging = new JTextArea();
		textArea_messaging.setFont(new Font("Roboto", Font.BOLD, 14));
		textArea_messaging.setBounds(32, 73, 374, 477);
		textArea_messaging.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2, true), null));
		textArea_messaging.setEditable(false);

		textField_sendMessage = new JTextField();
		textField_sendMessage.setBounds(32, 561, 374, 31);
		textField_sendMessage.setColumns(10);

		btnSendMessage = new JButton("Send Message");
		btnSendMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSendMessage.setEnabled(false);
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String valueAtIndex = (String) comboBox_JoinedChatRooms.getSelectedItem();

					sendMessage = textField_sendMessage.getText();
					System.out.println("value selected in live chat " + valueAtIndex);
					String[] joinedChatRoomId;
					joinedChatRoomId = valueAtIndex.split("_");
					Long id = Long.valueOf(joinedChatRoomId[1].trim());
					System.out.println("ID retrieved :" + id);
					String message = textField_sendMessage.getText();
					textField_sendMessage.setText("");
					chatClient.talk(id,chatClient.getName()+"::"+message);

				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSendMessage.setBounds(256, 603, 150, 25);

		JLabel lblAvailableChatRooms = new JLabel("Live Chat Rooms");
		lblAvailableChatRooms.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAvailableChatRooms.setBounds(437, 52, 186, 15);

		btn_joinChatRoom = new JButton("Join Chat Room");
		btn_joinChatRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btn_joinChatRoom.setBounds(562, 191, 136, 25);
		btn_joinChatRoom.setEnabled(false);
		btn_joinChatRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int selectedIndex = list_AvailableChatRooms.getSelectedIndex();
					System.out.println("index of value selected in live chat " + selectedIndex);
					String valueAtIndex = listAvailableChatRoomsModel.getElementAt(selectedIndex);
					System.out.println("value selected in live chat " + valueAtIndex);
					String[] joinedChatRoomId;
					joinedChatRoomId = valueAtIndex.split("_");
					Long id = Long.valueOf(joinedChatRoomId[1].trim());
					System.out.println("ID retrieved :" + id);
					chatClient.join(id);
					listJoinedChatRoomsModel.clear();
					comboBox_JoinedChatRooms.removeAllItems();
					for (ChatRoom chatRoom : chatClient.getJoinedChatRooms().values()) {
						System.out.println("Joined ChatRoom Name : " + chatRoom.getName());
						listJoinedChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
						comboBox_JoinedChatRooms.addItem(chatRoom.getName() + "_" + chatRoom.getId());
					}

					if (comboBox_JoinedChatRooms.getItemCount() != 0) {
						btnSendMessage.setEnabled(true);
						btn_leaveChatRoom.setEnabled(true);
					} else {
						btnSendMessage.setEnabled(false);
						btn_leaveChatRoom.setEnabled(false);
					}
					System.out.println("Size : " + listJoinedChatRoomsModel.size());
					if (listJoinedChatRoomsModel.size() != 0) {
						btn_leaveChatRoom.setEnabled(true);
					}

				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		btn_leaveChatRoom = new JButton("Leave ChatRoom");
		btn_leaveChatRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btn_leaveChatRoom.setEnabled(false);
		btn_leaveChatRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					int selectedIndex = list_JoinedChatRooms.getSelectedIndex();
					System.out.println("index of value selected in live chat " + selectedIndex);
					String valueAtIndex = listJoinedChatRoomsModel.getElementAt(selectedIndex);
					System.out.println("value selected in live chat " + valueAtIndex);
					String[] joinedChatRoomId;
					joinedChatRoomId = valueAtIndex.split("_");
					Long id = Long.valueOf(joinedChatRoomId[1].trim());
					System.out.println("ID retrieved :" + id);
					chatClient.leave(id);
					listJoinedChatRoomsModel.clear();
					comboBox_JoinedChatRooms.removeAllItems();
					for (ChatRoom chatRoom : chatClient.getJoinedChatRooms().values()) {
						System.out.println("Joined ChatRoom Name : " + chatRoom.getName());
						listJoinedChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
						comboBox_JoinedChatRooms.addItem(chatRoom.getName() + "_" + chatRoom.getId());
					}
					
					if(listJoinedChatRoomsModel.getSize() != 0){
						btn_leaveChatRoom.setEnabled(true);
					}else{
						btn_leaveChatRoom.setEnabled(false);
					}

				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btn_leaveChatRoom.setBounds(562, 403, 136, 25);
		contentPane.setLayout(null);
		contentPane.add(textArea_messaging);
		contentPane.add(textField_sendMessage);
		contentPane.add(btnSendMessage);
		contentPane.add(lblAvailableChatRooms);
		contentPane.add(btn_joinChatRoom);
		contentPane.add(btn_leaveChatRoom);

		JPanel panel_createNewChatRoom = new JPanel();
		panel_createNewChatRoom.setBackground(Color.LIGHT_GRAY);
		panel_createNewChatRoom.setBounds(443, 451, 255, 108);
		contentPane.add(panel_createNewChatRoom);
		panel_createNewChatRoom.setLayout(null);

		JLabel lblCreateANew = new JLabel("Create a new chat room");
		lblCreateANew.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCreateANew.setBounds(52, 11, 178, 15);
		panel_createNewChatRoom.add(lblCreateANew);

		JLabel lblName = new JLabel("Chat Room Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblName.setBounds(10, 37, 111, 15);
		panel_createNewChatRoom.add(lblName);

		textField_newChatRoom = new JTextField();
		textField_newChatRoom.setBounds(101, 37, 135, 19);
		panel_createNewChatRoom.add(textField_newChatRoom);
		textField_newChatRoom.setColumns(10);

		btnCreateNewChat = new JButton("Create new Chat room");
		btnCreateNewChat.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCreateNewChat.setEnabled(false);
		btnCreateNewChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatRoomName = textField_newChatRoom.getText();
				textField_newChatRoom.setText("");
				try {
					chatClient.createChatRoom(chatRoomName);
					chatRoomList = chatClient.findRegisteredChatRooms();
					listAvailableChatRoomsModel.clear();
					listJoinedChatRoomsModel.clear();
					ownedChatRoomList.removeAllItems();
					comboBox_JoinedChatRooms.removeAllItems();
					for (ChatRoom chatRoom : chatRoomList) {
						listAvailableChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
					}
					for (ChatRoom chatRoom : chatClient.getJoinedChatRooms().values()) {
						listJoinedChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
						comboBox_JoinedChatRooms.addItem(chatRoom.getName() + "_" + chatRoom.getId());
					}
					for (ChatRoom chatRoom : chatClient.getOwnedChatRooms().values()) {
						ownedChatRoomList.addItem(chatRoom.getName() + "_" + chatRoom.getId());
					}
					if (comboBox_JoinedChatRooms.getItemCount() != 0) {
						btnSendMessage.setEnabled(true);
						btn_joinChatRoom.setEnabled(true);

					}

					if (ownedChatRoomList.getItemCount() != 0) {
						btnCloseChatRoom.setEnabled(true);
					} else {
						btnCloseChatRoom.setEnabled(false);
					}

					if (listAvailableChatRoomsModel.size() != 0) {
						btnAvailableChatRooms.setEnabled(true);
						btn_joinChatRoom.setEnabled(true);
					} else{
						btn_joinChatRoom.setEnabled(false);
					}
					
					if (listJoinedChatRoomsModel.size() != 0) {
						btn_leaveChatRoom.setEnabled(true);
					} else {
						btn_joinChatRoom.setEnabled(false);
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		btnCreateNewChat.setBounds(73, 67, 163, 25);
		panel_createNewChatRoom.add(btnCreateNewChat);

		JLabel lblListOfJoined = new JLabel("List Of Joined Chat Rooms");
		lblListOfJoined.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblListOfJoined.setBounds(444, 248, 212, 14);
		contentPane.add(lblListOfJoined);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(456, 179, 242, -81);
		contentPane.add(scrollPane);

		listAvailableChatRoomsModel = new DefaultListModel<String>();
		list_AvailableChatRooms = new JList(listAvailableChatRoomsModel);
		// list_AvailableChatRooms.setModel();
		list_AvailableChatRooms.setBounds(443, 77, 255, 103);
		contentPane.add(list_AvailableChatRooms);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(456, 572, 252, -117);
		contentPane.add(scrollPane_1);

		listJoinedChatRoomsModel = new DefaultListModel<String>();
		list_JoinedChatRooms = new JList(listJoinedChatRoomsModel);
		list_JoinedChatRooms.setBounds(446, 273, 252, 119);
		contentPane.add(list_JoinedChatRooms);

		JLabel lblAliasName = new JLabel("Alias Name");
		lblAliasName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAliasName.setBounds(453, 17, 63, 14);
		contentPane.add(lblAliasName);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(32, 14, 375, 31);
		contentPane.add(panel);

		JLabel lblNewLabel_1 = new JLabel("MY CHAT WINDOW");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel = new JLabel("List of Owned Chat Rooms");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(443, 584, 255, 14);
		contentPane.add(lblNewLabel);

		ownedChatRoomList = new JComboBox<String>();
		ownedChatRoomList.setBounds(443, 606, 131, 20);
		contentPane.add(ownedChatRoomList);

		btnCloseChatRoom = new JButton("Close Chat Room");
		btnCloseChatRoom.setEnabled(false);
		btnCloseChatRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					int selectedIndex = ownedChatRoomList.getSelectedIndex();
					String valueAtIndex = (String) ownedChatRoomList.getSelectedItem();
					if (ownedChatRoomList.getItemCount() != 0) {
						ownedChatRoomList.removeItemAt(selectedIndex);
					}

					System.out.println("value selected in live chat " + valueAtIndex);
					String[] joinedChatRoomId;
					joinedChatRoomId = valueAtIndex.split("_");
					Long id = Long.valueOf(joinedChatRoomId[1].trim());
					System.out.println("ID retrieved :" + id);
					chatClient.terminateChatRoom(id);
					chatRoomList = chatClient.findRegisteredChatRooms();
					listAvailableChatRoomsModel.clear();
					listJoinedChatRoomsModel.clear();
					ownedChatRoomList.removeAllItems();

					comboBox_JoinedChatRooms.removeAllItems();
					for (ChatRoom chatRoom : chatRoomList) {
						listAvailableChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
					}
					for (ChatRoom chatRoom : chatClient.getJoinedChatRooms().values()) {
						listJoinedChatRoomsModel.addElement(chatRoom.getName() + "_" + chatRoom.getId());
						comboBox_JoinedChatRooms.addItem(chatRoom.getName() + "_" + chatRoom.getId());
					}
					for (ChatRoom chatRoom : chatClient.getOwnedChatRooms().values()) {
						ownedChatRoomList.addItem(chatRoom.getName() + "_" + chatRoom.getId());
					}

					if (ownedChatRoomList.getItemCount() != 0) {
						btnCloseChatRoom.setEnabled(true);
					} else {
						btnCloseChatRoom.setEnabled(false);
					}

					if (listAvailableChatRoomsModel.size() != 0) {
						btnAvailableChatRooms.setEnabled(true);
					} 

				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCloseChatRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCloseChatRoom.setBounds(592, 604, 129, 23);
		contentPane.add(btnCloseChatRoom);

		comboBox_JoinedChatRooms = new JComboBox<String>();
		comboBox_JoinedChatRooms.setBounds(105, 605, 136, 20);
		contentPane.add(comboBox_JoinedChatRooms);

		JLabel lblNewLabel_2 = new JLabel("Chat Room");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(32, 608, 63, 14);
		contentPane.add(lblNewLabel_2);
	}

}
