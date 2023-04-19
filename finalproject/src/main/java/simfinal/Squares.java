package simfinal;

import static java.lang.Math.ceil;

public class Squares
{
    private int foodAvailable;
    private int numCreatures;
    private int maxCreatures;
    private int maxCreatureTimeStep;

    //constructor
    public Squares(int foodAvailable, int numCreatures)
    {
        setFoodAvailable(foodAvailable);
        setNumCreatures(numCreatures);
        setMaxCreatures(numCreatures);
        setMaxCreatureTimeStep(0);
    }

    public String toString()
    {
        String toPrint = "Currently contains " + getFoodAvailable() + " pieces of food and " + getNumCreatures() + " creatures.";
        return toPrint;
    }

    private void setNumCreatures(int numCreatures)
    {
        this.numCreatures = numCreatures;
    }

    private void setFoodAvailable(int foodAvailable)
    {
        this.foodAvailable = foodAvailable;
    }

    public void setMaxCreatures(int maxCreatures)
    {
        this.maxCreatures = maxCreatures;
    }

    public void setMaxCreatureTimeStep(int maxCreatureTimeStep)
    {
        this.maxCreatureTimeStep = maxCreatureTimeStep;
    }

    public int getNumCreatures()
    {
        return numCreatures;
    }

    public int getFoodAvailable()
    {
        return foodAvailable;
    }

    public int getMaxCreatures()
    {
        return maxCreatures;
    }

    public int getMaxCreatureTimeStep()
    {
        return maxCreatureTimeStep;
    }

    public void addFood()
    {
        foodAvailable += 10;
    }

    public void decay()
    {
        int subtract = (int)ceil(foodAvailable*0.1);
        foodAvailable -= subtract;
    }

    public void foodEaten()
    {
        foodAvailable--;
    }

    public void gainCreature()
    {
        numCreatures++;
    }

    public void loseCreature()
    {
        numCreatures--;
    }
}
