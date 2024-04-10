import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Claw {

    static int getLeftStack(int pos, int[] boxes)
    {
        if (pos == 0)
            return boxes.length -1;
        else
            return pos - 1;
    }
    static int getRightStack(int pos, int[] boxes)
    {
        if(pos == boxes.length - 1)
            return 0;
        else
            return pos + 1;
    }
    static boolean canPickFromPos(int clawPos, int[] boxes)
    {
        return boxes[clawPos] > 2;
    }
    static boolean canPlaceInPos(int clawPos, int[] boxes)
    {
        return boxes[clawPos] < 5;
    }
    static int searchForStack(int clawPos, int pos, int[] boxes, Predicate<Integer> check , Predicate<Integer> comparison,int direction) {
        if (check.test(pos)) {
            return -1;
        }
        if (comparison.test(pos)) {
            return pos;
        }
        if(direction == 1)
            return searchForStack(clawPos, getLeftStack(pos, boxes), boxes,check, comparison,direction);
        else
            return searchForStack(clawPos, getRightStack(pos, boxes), boxes,check, comparison,direction);

    }
    static int searchForEmptierLeftStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, clawPos, boxes,pos-> pos == getRightStack(clawPos, boxes) ,pos -> boxes[clawPos] > boxes[getLeftStack(pos, boxes)],1);
    }

    static int searchForFullerRightStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos,clawPos, boxes,pos -> pos == getLeftStack(clawPos, boxes) ,pos -> boxes[clawPos] < boxes[getRightStack(pos, boxes)],0);
    }

    static int searchForEmptierRightStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, clawPos, boxes, pos -> pos == getLeftStack(clawPos, boxes),pos ->boxes[clawPos] > boxes[getRightStack(pos, boxes)],0);
    }

    static int searchForEmptierOrEqualRightStack(int clawPos, int[] boxes) {
        int leftOfClaw = getLeftStack(clawPos,boxes);
        return searchForStack(clawPos, clawPos, boxes, pos -> pos == leftOfClaw,pos->  boxes[clawPos] >= boxes[getRightStack(pos, boxes)],0);
    }


    public static Command solve(int clawPos, int[] boxes, int boxInClaw)
    {
        if(clawPos < 0 || clawPos > boxes.length -1|| boxes.length > 8 || boxes.length < 2 || Arrays.stream(boxes).anyMatch(b -> b > 5 || b < 1) || Arrays.stream(boxes).sum() > 16)
            return Command.WARNING;
        if( boxInClaw == 1)
        {
            if(searchForEmptierLeftStack(clawPos,boxes) != -1)
            {
                if (clawPos != 0)
                    return Command.LEFT;
            }

            if(searchForEmptierRightStack(clawPos,boxes) != -1)
            {
                if (clawPos != boxes.length - 1)
                    return Command.RIGHT;
            }


            if(clawPos == 0)
            {
                if(canPlaceInPos(clawPos,boxes))
                    return Command.PLACE;
            }
            else
                return Command.LEFT;


        }
        if(boxInClaw == 0)
        {
            if(searchForFullerRightStack(clawPos,boxes) != -1)
            {
                if (clawPos != boxes.length -1 )
                    return Command.RIGHT;
                else
                    return Command.WARNING;
            }
            if (canPickFromPos(clawPos,boxes))
                return Command.PICK;
            System.out.println("en pos "+ clawPos + "buscar izquierda mas vacios me dio" + searchForEmptierLeftStack(clawPos,boxes));
            if(searchForEmptierLeftStack(clawPos,boxes) != -1)
            {
                System.out.println("en pos"+ clawPos + "buscar derecha  mas vacios o iguales me dio" + searchForEmptierOrEqualRightStack(clawPos,boxes));
                if(searchForEmptierOrEqualRightStack(clawPos,boxes) != -1)
                {
                    if (clawPos != boxes.length -1 )
                        return Command.RIGHT;
                    else
                        return Command.WARNING;
                }
                else if (canPickFromPos(clawPos,boxes))
                    return Command.PICK;
                else
                    return Command.WARNING;
            }
            return Command.VICTORY;
        }

        return Command.WARNING;
    }
    public static void main(String[] args) {
        int clawPos = 0;
        int[] boxes = new int[]{1,2,3,4,5};
        int boxInClaw = 0;
        boolean gane = false;
        for (int i = 0;i <100;i++)
        {
            Command command = solve(clawPos,boxes,boxInClaw);
            if (command.equals(Command.LEFT))
                clawPos = clawPos -1;
            if(command.equals(Command.RIGHT))
                clawPos = clawPos +1;
            if(command.equals(Command.PICK))
            {
                boxInClaw = 1;
                boxes[clawPos] --;
            }
            if(command.equals(Command.PLACE))
            {
                boxInClaw = 0;
                boxes[clawPos] ++;
            }
            if(command.equals(Command.WARNING))
            {
                System.out.println("perdi");
                break;
            }
            if(command.equals(Command.VICTORY))
            {
                System.out.println("llegue " + i);
                gane = true;
                break;
            }
        }
        System.out.println("asi quedaron las cajas "+ Arrays.toString(boxes) + " y la garra esta en" + clawPos);
        if(gane)
            System.out.println("GANE");
        else
            System.out.println("alta luser");
    }
}

