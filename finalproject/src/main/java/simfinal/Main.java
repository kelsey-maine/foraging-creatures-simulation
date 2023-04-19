package simfinal;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.*;

public class Main
{
    public static int maxCreatures;
    public static int maxCreaturesTimeStep;
    public static ArrayList<Creature> creatureList = new ArrayList<Creature>();

    public static void main( String[] args )
    {
        //NUMBER OF TIME STEPS
        int maxTimeSteps = Integer.parseInt(args[0]);
        //RANDOM NUMBER SEED
        long randomSeed = Long.parseLong(args[1]);
        //RANDOM NUMBER GENERATOR FOR FOOD PRODUCTION
        Random randomFood = new Random(randomSeed);
        //RANDOM NUMBER GENERATOR FOR CREATURE INITIALIZATION
        Random randomCreaturePosition = new Random();

        //ARRAY OF SQUARES REPRESENTING ENVIRONMENT
        Squares[] environment = new Squares[10];
        for(int i = 0; i < environment.length; i++)
        {
            //start with no food or creatures
            environment[i] = new Squares(0,0);
            //create the first event for adding food
            newAddFoodEvent(i, 0, randomFood);
        }

        //ARRAYLIST OF CREATURES
        maxCreatures = 0;
        maxCreaturesTimeStep = 0;
        //randomly distribute 10 creatures to start
        for(int i = 0; i < 10; i++)
        {
            int creaturePos = randomCreaturePosition.nextInt(10);
            Creature newCreature = new Creature(creaturePos);
            //add creature to creature list, and update count in environment
            creatureList.add(newCreature);
            environment[newCreature.getCurrPos()].gainCreature();
        }
        //set max counts
        setMaxCounts(environment, 0);

        /*
        DEBUG PRINTING***********
        System.out.println("\nTIMESTEP 0\n");
        EventQueue.printAll();
        printEnvironment(environment);
        */

        //SIMULATION
        for(int i = 1; i < maxTimeSteps+1; i++)
        {
            //System.out.println("\nTIMESTEP " + i + "\n");
            //decay all food
            decay(environment);
            //peek in eventqueue to add food if necessary
            while(i == EventQueue.peek().getOccursAt())
            {
                Event processingEvent = EventQueue.pop();
                environment[processingEvent.getIndex()].addFood();
                newAddFoodEvent(processingEvent.getIndex(),i,randomFood);
            }
            //activate creatures
            activateCreatures(environment);
            //check for new max
            setMaxCounts(environment,i);

            /*
            DEBUG PRINTING***********
            EventQueue.printAll();
            printEnvironment(environment);
            */
        }
        printMaxCounts(environment);
    }

    public static int getNextTimeStepToAdd(Random randomGenerator)
    {
        //Generate r, a uniformly distributed random number between 0 and 1 by using nextDouble
        double r = randomGenerator.nextDouble();
        //Convert r into x, an exponentially distributed random number with mean (avgTime) by using equation x=-ln(1-r)/avgTime
        double x = (-Math.log(1.0-r)) / 0.2;
        int toAdd = (int)ceil(x);
        return toAdd;
    }

    public static void newAddFoodEvent(int index, int currTimeStep, Random random)
    {
        //create event and queue it
        Event addFoodEvent = new Event(index, currTimeStep + getNextTimeStepToAdd(random));
        EventQueue.push(addFoodEvent);
    }

    public static void decay(Squares[] environment)
    {
        for(int i = 0; i < environment.length; i++)
        {
            environment[i].decay();
        }
    }

    public static void activateCreatures(Squares[] environment)
    {
        //ACTIVATE IN RANDOM ORDER
        Collections.shuffle(creatureList);
        for(int i = 0; i < creatureList.size(); i++)
        {
            //CREATURES AT 0 ENERGY DIE
            if(creatureList.get(i).getEnergy() == 0)
            {
                //die
                //remove from environment
                environment[creatureList.get(i).getCurrPos()].loseCreature();
                //remove from creature list
                creatureList.remove(i);
                //System.out.println("Someone has died");
            }
            //CREATURES EAT IF FOODS AVAILABLE AND ENERGY NOT AT MAX
            else if(creatureList.get(i).getEnergy() != 10 && environment[creatureList.get(i).getCurrPos()].getFoodAvailable() > 0)
            {
                //eat
                //-1 food in environment
                environment[creatureList.get(i).getCurrPos()].foodEaten();
                //+1 energy in creature
                creatureList.get(i).eat();
                //System.out.println("Someone has eaten and now has energy level: " + creatureList.get(i).getEnergy());
            }
            //CREATURES AT FULL ENERGY REPRODUCE
            else if(creatureList.get(i).getEnergy() == 10)
            {
                //reproduce
                //cut energy in half
                creatureList.get(i).reproduce();
                //create new creature
                Creature newCreature = new Creature(creatureList.get(i).getCurrPos());
                //add creature to creature list, and update count in environment
                creatureList.add(newCreature);
                environment[newCreature.getCurrPos()].gainCreature();
                //System.out.println("Someone has reproduced");
            }
            else
            {
                //travel
                //check left and right for food
                int leftIndex = creatureList.get(i).getCurrPos();
                int rightIndex = creatureList.get(i).getCurrPos();
                //find left and right squares with the first instance of food
                while(leftIndex >= 0 && environment[leftIndex].getFoodAvailable() == 0)
                    leftIndex--;
                while(rightIndex <= 9 && environment[rightIndex].getFoodAvailable() == 0)
                    rightIndex++;
                //decide which is closer
                int leftDifference = creatureList.get(i).getCurrPos() - leftIndex;
                int rightDifference = rightIndex - creatureList.get(i).getCurrPos();
                //left = -1 means no food on left, right = 10 means no food on right
                if(leftIndex == -1 && rightIndex == 10)
                {
                    //no food at all, so check if first or last square, if not, choose randomly
                    if(creatureList.get(i).getCurrPos() == 0)
                        moveRight(environment, i);
                    else if (creatureList.get(i).getCurrPos() == 9)
                        moveLeft(environment, i);
                    else
                    {
                        Random leftOrRight = new Random();
                        int go = leftOrRight.nextInt(2);
                        if(go == 0)
                        {
                            moveLeft(environment, i);
                        }
                        else
                        {
                            moveRight(environment, i);
                        }
                    }
                }
                else if(leftIndex == -1)
                {
                    moveRight(environment, i);
                }
                else if(rightIndex == 10)
                {
                    moveLeft(environment, i);
                }
                else if(leftDifference < rightDifference)
                {
                    moveLeft(environment, i);
                }
                else if(rightDifference < leftDifference)
                {
                    moveRight(environment, i);
                }
                else if(leftDifference == rightDifference)
                {
                    Random leftOrRight = new Random();
                    int go = leftOrRight.nextInt(2);
                    if(go == 0)
                    {
                        moveLeft(environment, i);
                    }
                    else
                    {
                        moveRight(environment, i);
                    }
                }
                //System.out.println("Someone should have travelled");
            }
        }
    }

    public static void moveRight(Squares[] environment, int i)
    {
        //update original square creature count
        environment[creatureList.get(i).getCurrPos()].loseCreature();
        //move creature to right
        creatureList.get(i).travelRight();
        //update new square creature count
        environment[creatureList.get(i).getCurrPos()].gainCreature();
    }

    public static void moveLeft(Squares[] environment, int i)
    {
        //update original square creature count
        environment[creatureList.get(i).getCurrPos()].loseCreature();
        //move creature to right
        creatureList.get(i).travelLeft();
        //update new square creature count
        environment[creatureList.get(i).getCurrPos()].gainCreature();
    }

    public static void setMaxCounts(Squares[] environment, int currTimeStep)
    {
        //set max creatures
        if(creatureList.size() > maxCreatures)
        {
            maxCreatures = creatureList.size();
            maxCreaturesTimeStep = currTimeStep;
        }
        //set max creatures per square
        for(int i = 0; i < environment.length; i++)
        {
            if(environment[i].getNumCreatures() > environment[i].getMaxCreatures())
            {
                environment[i].setMaxCreatures(environment[i].getNumCreatures());
                environment[i].setMaxCreatureTimeStep(currTimeStep);
            }
        }
    }

    public static void printMaxCounts(Squares[] environment)
    {
        System.out.printf("The maximum number of living agents in the simulation was: %d at time-step %d%n",maxCreatures,maxCreaturesTimeStep);
        for(int i = 0; i < environment.length; i++)
        {
            System.out.printf("The maximum number of living agents in square %d was %d at time-step %d%n",i, environment[i].getMaxCreatures(), environment[i].getMaxCreatureTimeStep());
        }
    }

    public static void printEnvironment(Squares[] environment)
    {
        System.out.println("_____________________");
        for(int i = 0; i < environment.length; i++)
        {
            System.out.println("Square: " + i);
            System.out.println("Food available: " + environment[i].getFoodAvailable());
            System.out.println("Creatures: " + environment[i].getNumCreatures());
            System.out.println("_____________________");
        }
    }

}
