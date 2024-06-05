
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientView
{
    private static ClientView clientView;
    private ClientView(){}

    public static ClientView getView() {
        if(clientView ==null)
            clientView =new ClientView();
        return clientView;
    }
    private PrintWriter writer;
    public void start()
    {
        try
        {
            writer=new PrintWriter(Client.getSocket().getOutputStream());
            Scanner sc=new Scanner(System.in);
            Client.setStartTime(System.currentTimeMillis());
            while (true)
            {
                System.out.println("pls enter your ID");
                Client.setID(sc.nextLine());
                writer.println(Client.getID());
                System.out.println("pls enter your name");
                Client.setName(sc.nextLine());
                writer.println(Client.getName());
                writer.flush();
                synchronized (Client.getMain())
                {
                    Client.getMain().wait();
                }
                if(Client.isRightInfo())
                    break;
            }
            while (true)
            {
                String input=sc.nextLine();
                if(input.compareTo("exit")==0)
                {
                    writer.println("exit");
                    writer.flush();
                    Client.getSocket().close();
                    Client.setRun(false);
                    break;
                }
                else if(input.compareTo("pv")==0)
                {
                    writer.println("pv");
                    writer.flush();
                    writer.println(sc.nextLine());
                    writer.flush();
                    synchronized (Client.getMain())
                    {
                        Client.getMain().wait();
                    }
                    if(Client.isEnterPV())
                        pv();
                    System.out.println("----------------------------------");
                }
                else if(input.compareTo("search")==0)
                {
                    String function=sc.nextLine();
                    String[] orders=function.split(" ");
                    if(orders.length>1)
                    {
                        String regex="^(0[0-9]|1[0-9]|2[0-4]):[0-5][0-9]:[0-5][0-9]$";
                        Pattern pattern= Pattern.compile(regex);
                        if(!(pattern.matcher(orders[0]).matches() && pattern.matcher(orders[2]).matches()))
                            System.out.println("wrong time");
                        else
                        {
                            writer.println("search");
                            writer.println(function);
                            writer.flush();
                        }
                    }
                    else
                    {
                        writer.println("search");
                        writer.println(function);
                        writer.flush();
                    }
                }
                else if(input.compareTo("ping")==0)
                {
                    Client.setStartTime(System.currentTimeMillis());
                    writer.println("");
                    writer.flush();
                }
                else
                {
                    writer.println(input);
                    writer.flush();
                }
            }
        }catch (Exception exception)
        {
            System.out.println("error "+exception.getMessage());
        }
    }
    public void pv()
    {
        while (true)
        {
            Scanner sc=new Scanner(System.in);
            String message=sc.nextLine();
            writer.println(message);
            writer.flush();
            if(message.compareTo("finish")==0)
            {
                System.out.println("private chat ended");
                break;
            }
        }
    }
}