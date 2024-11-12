public class CircularQueue <E> {

    private E[] theData;
    private int front;
    private int rear;
    private int size;
    private int capacity;
    private	static	final	int	DEFAULT_CAPACITY = 10;

    public CircularQueue()
    {
        this(DEFAULT_CAPACITY);
    }

    public CircularQueue(int cap)
    {
        capacity = cap;
        theData = (E[]) new Object[capacity];
        front = 0;
        rear = capacity - 1;
        size = 0;
    }

    //maybe remove
    public  CircularQueue(CircularQueue<E> other)
    {
        capacity = other.capacity;
        size = other.size;
        front = other.front;
        rear = other.rear;
        theData = (E[]) new Object[capacity];
        int index = front;
        for (int i=0; i<size; i++)
        {
            theData[index]= other.theData[index];
            index = (index + 1) % capacity;
        }
    }

    //maybe remove
    private void reallocate()
    {
        int newCapacity = 2 * capacity;
        E[] newData = (E[]) new Object[newCapacity];
        int j = front;
        for (int i=0; i<size; i++)
        {
            newData[i] = theData[j];
            j = (j + 1) % capacity;
        }
        front = 0;
        rear = size-1;
        capacity = newCapacity;
        theData = newData;
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return (size == 0);
    }

    public boolean offer(E item)
    {
        if (size == capacity)
            reallocate();
        rear = (rear + 1) % capacity;
        theData[rear] = item;
        size++;
        return true;
    }

    public E poll()
    {
        if (size == 0)
            return null;
        E result = theData[front];
        front = (front + 1) % capacity;
        size--;
        return result;
    }


    public E peek()
    {
        if (size == 0)
            return null;
        return theData[front];
    }



}
