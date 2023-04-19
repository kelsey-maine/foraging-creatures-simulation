package simfinal;

import java.util.LinkedList;

public class EventQueue
{
    private static Node<Event> head = null;

    private static class Node<Event>
    {
        private Event event;
        private Node<Event> next;


        //constructor
        public Node(Event event, Node<Event> next)
        {
            this.event = event;
            this.next = next;
        }
    }

    //Add to queue
    public static void push(Event event)
    {
        //create the node
        Node<Event> newNode = new Node<Event>(event, null);

        //if head is null, make node head
        if(head == null)
        {
            head = newNode;
            return;
        }

        //if heads not null, check if new node should be head
        if(head.event.getOccursAt() > newNode.event.getOccursAt())
        {
            Node<Event> tmp = head;
            head = newNode;
            newNode.next = tmp;
            return;
        }

        //if heads not null, go by priority
        Node<Event> prev = null;
        Node<Event> curr = head;

        while(curr != null && curr.event.getOccursAt() <= newNode.event.getOccursAt())
        {
            prev = curr;
            curr = curr.next;
        }
        if(curr == null)
        {
            prev.next = newNode;
        }
        else
        {
            prev.next = newNode;
            prev.next.next = curr;
        }
    }

    //Remove from queue
    public static Event pop()
    {
        //check if possible
        if(head == null)
        {
            System.out.println("No nodes to delete, continuing on.\n");
            return null;
        }
        //return head and replace it
        Event event = head.event;
        head = head.next;
        return event;
    }

    //Peek at head of queue
    public static Event peek()
    {
        //check if possible
        if(head == null)
        {
            System.out.println("No events in queue, continuing on.\n");
            return null;
        }
        //return time event occurs at
        return head.event;
    }

    public static void printAll()
    {
        Node<Event> curr = head;
        System.out.println("START EVENT QUEUE");
        while(curr != null)
        {
            System.out.println(curr.event.testingEventQueueToString());
            curr = curr.next;
        }
        System.out.println("END EVENT QUEUE\n");
    }
}

