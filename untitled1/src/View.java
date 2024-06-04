
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class View
{
    private static View view;
    private View(){}

    public static View getView() {
        if(view==null)
            view=new View();
        return view;
    }
    private PrintWriter writer;
    public void start()
    {
        try
        {
            writer=new PrintWriter(Client.getSocket().getOutputStream());
            Scanner sc=new Scanner(System.in);
            writer.println(Client.getID());
            writer.println(Client.getName());
            writer.flush();
            Client.setStartTime(System.currentTimeMillis());
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
                    ;
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
}