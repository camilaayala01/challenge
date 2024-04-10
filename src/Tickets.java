import java.util.List;

public class Tickets {
    public static final int ticketPrice = 25;
    public static final List<Integer> validBills = List.of(25,50,100);

    public static String tickets(int[] dollarBills){
        int clerkBalance = 0;
        for (int bill : dollarBills) {
            if(!validBills.contains(bill) || clerkBalance < (bill - ticketPrice) )
                return("NO");
            else
                clerkBalance += ticketPrice - (bill - ticketPrice);
        }
        return("YES");
    }
    public static void main(String[] args) {
        System.out.println(tickets(new int[]{25,50,25,100,25,25,25,100,25,25,50,100,50,25}));
    }
}