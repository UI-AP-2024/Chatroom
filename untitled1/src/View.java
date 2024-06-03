
import java.io.PrintWriter;
import java.util.Scanner;

public class View
{
    private static View view;
    private View(){}

    public static View getView() {
        if(view==null)
            view=new View();
        return view;
    }
    public void start()
    {
        try(PrintWriter writer=new PrintWriter(Client.getSocket().getOutputStream());)
        {
            Scanner sc=new Scanner(System.in);
            writer.println(Client.getID());
            writer.println(Client.getName());
            writer.flush();
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
                    ;
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