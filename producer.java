import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.codec.binary.Base64;

public class producer extends JPanel{
	JPanel th;
	File file;
	boolean flag = false;
	Publish pb;
	producer(){
		pb = new Publish();
		th = this;
		setLayout(new GridBagLayout());
		JLabel title = new JLabel("PUBLISHER");
        title.setFont(new Font("Century Schoolbook L", Font.BOLD, 32));
        
        JTextField topic = new JTextField();
        topic.setPreferredSize(new Dimension(300,25));
        topic.setToolTipText("topic");
 
        JTextArea text = new JTextArea();
        text.setToolTipText("enter data");
        JScrollPane textpane = new JScrollPane(text);
        textpane.setPreferredSize(new Dimension(300,200));
        
        
        JFileChooser fc = new JFileChooser();
        
        JLabel filename = new JLabel();
        filename.setPreferredSize(new Dimension(300, 25));
  
        JButton choose = new JButton("choose");
        choose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fc.showOpenDialog(th);
				file = fc.getSelectedFile();
				flag = true;
				filename.setText(file.getName());
			}
		});
		
        JButton submit = new JButton("submit");
        submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				if(flag){
					if(file.length() >= 1000000)System.err.println("File too large");
					else{
						byte[] bytes = ByteBuffer.allocate(4).putInt(file.getName().length()).array();
						msg = "F";
						msg += new String(bytes);
						msg += file.getName();
						try {
							byte[] fb = Files.readAllBytes(file.toPath());
							msg += Base64.encodeBase64URLSafeString(fb);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						System.out.println("sending encrypted file ...");
						try {
							pb.send(topic.getText(), msg);
						} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
								| NoSuchAlgorithmException | NoSuchPaddingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println("file sent successfully ...");
					}
				}else{
					msg = "M";
					msg += text.getText();
					System.out.println("sending encrypted message ...");
					try {
						pb.send(topic.getText(), msg);
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
							| NoSuchAlgorithmException | NoSuchPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("message sent successfully ...");
				}
			}
		});
        
        int gap = 30;
		addComponent(title,0,0,3,1);
		addGap(0,2,0,100);
		addComponent(new JLabel("Enter topic:"),1,3,1,1);
		addComponent(topic,2,3,2,1);
		addGap(0,4,0,gap);
		addComponent(new JLabel("Enter data:"),1,5,1,1);
		addComponent(textpane,2,5,2,1);
		addGap(0,6,0,gap);
		addComponent(new JLabel("OR chose file: "),1,7,1,1);
		addComponent(filename,3,7,1,1);
		addComponent(choose,2,7,1,1);
		addGap(0,8,0,gap);
		addComponent(submit,2,9,2,1);
		
        
	}
	void addComponent(Component c,int x,int y,int w,int h){
        GridBagConstraints gbc = new GridBagConstraints();
          gbc.fill = GridBagConstraints.NONE;
          gbc.gridx = x;
          gbc.gridy = y;
          gbc.gridwidth = w;
          gbc.gridheight = h;
          add(c,gbc);
    }
    void addGap(int x,int y,int w,int h){
        GridBagConstraints gbc = new GridBagConstraints();
          gbc.fill = GridBagConstraints.NONE;
          gbc.gridx = x;
          gbc.gridy = y;
          gbc.insets = new Insets(h,w,0,0);
          add(new JLabel(""),gbc);
    }
}
