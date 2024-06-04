import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ClientThread thread;
    private User user;

    public ClientThread(Socket socket, User user) {
        this.socket = socket;
        this.user = user;
        this.thread = this;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public Socket getSocket() {
        return socket;
    }

    public ClientThread getThread() {
        return thread;
    }

    public void setThread(ClientThread thread) {
        this.thread = thread;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerExecution.chatRoom(new Massage((user.getUsername()+" has joined the chat"),"Server","Chatroom",MassageType.PUBLIC),user);

    }

    public void sendMessage(Massage message) {
        writer.println(message.getText());
    }


}
