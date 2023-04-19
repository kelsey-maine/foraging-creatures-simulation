package simfinal;

public class Event
{
    private int index;
    private int occursAt;

    //constructor
    public Event(int index, int occursAt)
    {
        setIndex(index);
        setOccursAt(occursAt);
    }

    public String toString()
    {
        String toPrint = "Adding food to square " + getIndex() + " at " + getOccursAt();
        return toPrint;
    }

    public String testingEventQueueToString()
    {
        String toPrint = "Event occurs at " + getOccursAt() + " in square " + getIndex();
        return toPrint;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public void setOccursAt(int occursAt)
    {
        this.occursAt = occursAt;
    }

    public int getOccursAt()
    {
        return occursAt;
    }
}
