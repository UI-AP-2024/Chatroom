import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static String name;
    private static String ID;
    private static boolean run=true;
    private static long startTime;
    private static boolean enterPV=true;
    private static boolean rightInfo=true;
    private static Thread main;
    public static void main(String[] args) throws IOException {
        Client.socket=new Socket("127.0.0.1",1234);
        main=Thread.currentThread();
        new Thread(){
            public void run()
            {
                while (run)
                {
                    try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));)
                    {
                        String mess;
                        while ((mess=bufferedReader.readLine())!=null)
                        {
                            System.out.println(mess);
                            if(mess.compareTo("connected")==0)
                            {
                                System.out.println("ping: "+(System.currentTimeMillis()-startTime)+"ms");
                                rightInfo=true;
                                synchronized (main)
                                {
                                    main.notifyAll();
                                }
                            }
                            else if(mess.compareTo("Wrong Information")==0)
                            {
                                rightInfo=false;
                                synchronized (main)
                                {
                                    main.notifyAll();
                                }
                            }
                            else if(mess.compareTo("username Not Found Or is offline")==0)
                            {
                                enterPV=false;
                                synchronized (main)
                                {
                                    main.notifyAll();
                                }
                            }
                            else if(mess.compareTo("pv started")==0)
                            {
                                enterPV=true;
                                synchronized (main)
                                {
                                    main.notifyAll();
                                }
                            }
                        }
                    }catch (Exception exception){}
                }
            }
        }.start();
        ClientView.getView().start();
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Client.socket = socket;
    }

    public static void setID(String ID) {
        Client.ID = ID;
    }

    public static void setName(String name) {
        Client.name = name;
    }

    public static String getName() {
        return name;
    }

    public static String getID() {
        return ID;
    }

    public static void setRun(boolean run) {
        Client.run = run;
    }

    public static boolean isRun() {
        return run;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        Client.startTime = startTime;
    }

    public static boolean isEnterPV() {
        return enterPV;
    }

    public static void setEnterPV(boolean enterPV) {
        Client.enterPV = enterPV;
    }

    public static Thread getMain() {
        return main;
    }

    public static void setMain(Thread main) {
        Client.main = main;
    }

    public static boolean isRightInfo() {
        return rightInfo;
    }

    public static void setRightInfo(boolean rightInfo) {
        Client.rightInfo = rightInfo;
    }
}