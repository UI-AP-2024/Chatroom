import database.Database;

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
            Database.getDatabase().makeConnection();
            Scanner sc=new Scanner(System.in);
            Database.getDatabase().login(Client.getName(),Client.getID());
            writer.println(Client.getID()+"-"+Client.getName());
            while (true)
            {
                String input=sc.nextLine();
                if(input.compareTo("exit")==0)
                {
                    Database.getDatabase().finish();
                    writer.println("exit");
                    Client.getSocket().close();
                    break;
                }
                else if(input.compareTo("pv")==0)
                    ;
                else if(input.compareTo("search")==0)
                    ;
                else if(input.compareTo("ping")==0)
                    ;
                else
                    writer.println(input);
            }
        }catch (Exception exception)
        {
            System.out.println("error "+exception.getMessage());
        }
    }
}