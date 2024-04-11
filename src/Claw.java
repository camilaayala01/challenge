import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.Arrays.sort;

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
    static int searchForStack(int pos, int[] boxes, Predicate<Integer> comparison, int direction) {
        if((direction == 1 && pos == 0) || (direction == 0 && pos == boxes.length -1))
            return -1;
        if (comparison.test(pos)) {
            return pos;
        }
        if(direction == 1)
            return searchForStack(getLeftStack(pos, boxes), boxes, comparison,direction);
        else
            return searchForStack(getRightStack(pos, boxes), boxes, comparison,direction);

    }
    static int searchForEmptierLeftStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, boxes, pos -> boxes[clawPos] > boxes[getLeftStack(pos, boxes)],1);
    }
    static int searchForEmptierOrEqualLeftStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, boxes, pos -> boxes[clawPos] >= boxes[getLeftStack(pos, boxes)],1);
    }

    static int searchForFullerRightStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, boxes , pos -> boxes[clawPos] <= boxes[getRightStack(pos, boxes)],0);
    }

    static int searchForEmptierRightStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, boxes, pos ->boxes[clawPos] > boxes[getRightStack(pos, boxes)],0);
    }
    static boolean checkIfOrdered(int[] boxes)
    {
        return Arrays.equals(boxes,IntStream.of(boxes).sorted().toArray());
    }

    public static Command solve(int clawPos, int[] boxes, int boxInClaw)
    {
        System.out.println("cajas:"+ Arrays.toString(boxes));
        System.out.println("la garra esta en " + clawPos + "y " + boxInClaw);
        if(clawPos < 0 || clawPos > boxes.length -1|| boxes.length > 8 || boxes.length < 2 || Arrays.stream(boxes).anyMatch(b -> b > 5 || b < 1) || Arrays.stream(boxes).sum() > 16)
            return Command.WARNING;
        if( boxInClaw == 1)
        {
            if(clawPos != 0 && searchForEmptierOrEqualLeftStack(clawPos,boxes) != -1)
                return Command.LEFT;
            if(canPlaceInPos(clawPos,boxes))
                return Command.PLACE;

            if(clawPos != boxes.length -1 && searchForEmptierRightStack(clawPos,boxes) != -1)
            {
                if (clawPos != boxes.length - 1)
                    return Command.RIGHT;
            }


        }
        if(boxInClaw == 0)
        {
            if(clawPos != boxes.length -1 && (searchForFullerRightStack(clawPos,boxes) != -1))
                return Command.RIGHT;
            if(clawPos != 0 && searchForEmptierLeftStack(clawPos,boxes) != -1)
            {
                if (canPickFromPos(clawPos,boxes))
                    return Command.PICK;
                else if(clawPos != boxes.length -1  && searchForEmptierRightStack(clawPos,boxes) != -1)
                    return Command.RIGHT;
            }
            if(!checkIfOrdered(boxes))
            {
                if(clawPos == 0)
                    return Command.RIGHT;
                if(clawPos == boxes.length -1)
                    return Command.LEFT;
            }
            else
            {
                return Command.VICTORY;
            }

        }

        return Command.WARNING;
    }
    public static void main(String[] args) {
        int clawPos = 0;
        int[] boxes = new int[]{3,5,3,2,2};
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
        System.out.println("asi quedaron las cajas "+ Arrays.toString(boxes) + " y la garra esta en" + clawPos +" y esta asi box in claw " + boxInClaw);
        if(gane)
            System.out.println("GANE");
        else
            System.out.println("alta luser");
    }
}

