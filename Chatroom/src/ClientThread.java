import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ClientThread thread;
    public ClientThread(Socket socket) {
        this.socket = socket;
        this.thread=this;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setIn(BufferedReader reader) {
        this.reader = reader;
    }

    public void setOut(PrintWriter out) {
        this.writer = out;
    }

    public void setThread(ClientThread thread) {
        this.thread = thread;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return reader;
    }

    public PrintWriter getOut() {
        return writer;
    }

    public ClientThread getThread() {
        return thread;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
