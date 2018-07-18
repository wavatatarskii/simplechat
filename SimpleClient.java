import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {
    JTextArea incoming;
    JTextField outgoing;
    JTextField name;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    JLabel nam1;
    JLabel text2;



    public static void main(String[] args) {
        SimpleClient client = new SimpleClient();
        client.go();
    }

    public void go() {

        JFrame frame = new JFrame("Chat");
        frame.setResizable(false);
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15,50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroler = new JScrollPane(incoming);
        qScroler.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroler.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nam1 = new JLabel("Имя");
        name = new JTextField(10);
        text2 = new JLabel("Текст");
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Отправить сообщение");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroler);
        mainPanel.add(nam1);
        mainPanel.add(name);
        mainPanel.add(text2);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        setUpNetworking();
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(600,400);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }



    private void setUpNetworking() {

        try {
            sock = new Socket("127.0.0.1",5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer =  new PrintWriter(sock.getOutputStream());
            System.out.println("networking estabished");
        }
        catch (IOException ex) {
            ex.printStackTrace();

        }
    }


    public class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(!name.getText().toString().trim().isEmpty()&&name.getText().toString().matches("(?i).*[a-zа-я].*")){ name.setEditable(false);
                    writer.println(name.getText() + ": " + outgoing.getText());
                    writer.flush();
                    outgoing.setText("");
                    outgoing.requestFocus();
                }
                else {JOptionPane.showMessageDialog(null,
                        "Введите корректное имя!",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
                        name.setText("");
                        name.requestFocus();
                }

            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public class IncomingReader implements Runnable {
        @Override
        public void run() {
            String message;
                    try {

                        while ((message=reader.readLine())!=null) {
                            System.out.println("read " + message);
                            for (String retval : message.split(" ")) {
                                incoming.append(retval+ ' ');
                            }
                            incoming.append("\n");

                        }
                    }
                    catch(Exception ex) {ex.printStackTrace();}
        }

    }
}
