package simfinal;

public class Creature
{
    private int energy;
    private int currPos;

    //constructor
    public Creature(int currPos)
    {
        setEnergy(5);
        setCurrPos(currPos);
    }

    private void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy()
    {
        return energy;
    }

    private void setCurrPos(int currPos)
    {
        this.currPos = currPos;
    }

    public int getCurrPos()
    {
        return currPos;
    }

    public void eat()
    {
        energy++;
    }

    public void reproduce()
    {
        energy /= 2;
    }

    public void travelRight()
    {
        currPos++;
        energy--;
    }

    public void travelLeft()
    {
        currPos--;
        energy--;
    }
}
