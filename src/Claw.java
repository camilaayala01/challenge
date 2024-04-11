import java.util.Arrays;
import java.util.function.Predicate;

public class Claw {
    static int RIGHT = 0;
    static int LEFT= 1;
    static int NOT_FOUND = - 1;
    static int MAX_BOXES = 16;
    static int MAX_BOXES_IN_STACK = 5;
    static int MIN_BOXES_IN_STACK = 1;
    static int MIN_STACKS = 2;
    static int MAX_STACKS = 8;
    static int AMOUNT_OF_TURNS = 100;
    static int getLeftStack(int pos)
    {
            return pos - 1;
    }
    static int getRightStack(int pos)
    {
            return pos + 1;
    }
    static boolean isFirstStack(int pos)
    {
        return pos == 0;
    }
    static boolean isLastStack(int pos, int[] boxes)
    {
        return pos == boxes.length -1;
    }
    static boolean hasReachedEdge(int pos, int direction, int[] boxes)
    {
        if(direction == LEFT)
            return isFirstStack(pos);
        if(direction ==  RIGHT)
            return isLastStack(pos,boxes);
        return true;
    }

    static boolean canPickFromPos(int clawPos, int[] boxes)
    {
        return boxes[clawPos]  > MIN_BOXES_IN_STACK + 1;
    }
    static boolean canPlaceInPos(int clawPos, int[] boxes)
    {
        return boxes[clawPos] < MAX_BOXES_IN_STACK;
    }
    static int searchForStack(int pos, int[] boxes, Predicate<Integer> comparison, int direction) {
        if(hasReachedEdge(pos,direction,boxes))
            return NOT_FOUND;
        if (comparison.test(pos)) {
            return pos;
        }
        if(direction == LEFT)
            return searchForStack(getLeftStack(pos), boxes, comparison,direction);
        else if (direction == RIGHT)
            return searchForStack(getRightStack(pos), boxes, comparison,direction);
        else
            return NOT_FOUND;
    }
    static int searchForEmptierLeftStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, boxes, pos -> boxes[clawPos] > boxes[getLeftStack(pos)],LEFT);
    }
    static int searchForEmptierOrEqualLeftStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, boxes, pos -> boxes[clawPos] >= boxes[getLeftStack(pos)],LEFT);
    }

    static int searchForFullerRightStack(int clawPos, int[] boxes) {
        return searchForStack(clawPos, boxes , pos -> boxes[clawPos] <= boxes[getRightStack(pos)],RIGHT);
    }


    public static Command solve(int clawPos, int[] boxes, int boxInClaw)
    {
        System.out.println("BOXES: "+ Arrays.toString(boxes) + " CLAW POS: " + clawPos +" BOX IN CLAW= " + boxInClaw);
        if(clawPos < 0 || clawPos > boxes.length -1|| boxes.length > MAX_STACKS || boxes.length < MIN_STACKS || Arrays.stream(boxes).anyMatch(b -> b > MAX_BOXES_IN_STACK || b < MIN_BOXES_IN_STACK) || Arrays.stream(boxes).sum() > MAX_BOXES)
            return Command.WARNING;
        if( boxInClaw == 1)
        {
            if(!isFirstStack(clawPos) && searchForEmptierOrEqualLeftStack(clawPos,boxes) != NOT_FOUND)
                return Command.LEFT;
            if(canPlaceInPos(clawPos,boxes))
                return Command.PLACE;

        }
        if(boxInClaw == 0)
        {
            if(!isLastStack(clawPos,boxes) && searchForFullerRightStack(clawPos,boxes) != NOT_FOUND)
                return Command.RIGHT;
            if(searchForEmptierLeftStack(clawPos,boxes) != NOT_FOUND || isFirstStack(clawPos))
            {
                if (!isFirstStack(clawPos))
                {
                    if (canPickFromPos(clawPos,boxes))
                        return Command.PICK;
                    else
                        return Command.LEFT;
                }
                else
                    return Command.RIGHT;
            }
            return Command.VICTORY;
        }

        return Command.WARNING;
    }
    public static void main(String[] args) {
        //SET YOUR GAME VARIABLES HERE!
        int clawPos = 0;
        int[] boxes = new int[]{4,3,3,2};
        int boxInClaw = 0;
        boolean VICTORY = false;
        for (int i = 0;i < AMOUNT_OF_TURNS;i++)
        {
            Command command = solve(clawPos,boxes,boxInClaw);
            if (command.equals(Command.LEFT))
                clawPos = getLeftStack(clawPos);
            if(command.equals(Command.RIGHT))
                clawPos = getRightStack(clawPos);
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
                break;
            if(command.equals(Command.VICTORY))
            {
                VICTORY = true;
                break;
            }
        }
        if(VICTORY)
            System.out.println("VICTORY");
        else
            System.out.println("YOU LOST");
    }
}

