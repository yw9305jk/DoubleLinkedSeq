// Kou Vang
// ICS 240.01

// File: DoubleLinkedSeq.java from the package edu.colorado.collections
// This is an assignment for students to complete after reading Chapter 4 of
// "Data Structures and Other Objects Using Java" by Michael Main.
// Check with your instructor to see whether you should put this class in
// a package. At the moment, it is declared as part of edu.colorado.collections:


/******************************************************************************
* This class is a homework assignment;
* A DoubleLinkedSeq</CODE> is a collection of double</CODE> numbers.
* The sequence can have a special "current element," which is specified and 
* accessed through four methods that are not available in the sequence class 
* (start, getCurrent, advance and isCurrent).
*
* <dl><dt><b>Limitations:</b>
*   Beyond Int.MAX_VALUE</CODE> elements, the size</CODE> method
*   does not work.
*
* <dt><b>Note:</b><dd>
*   This file contains only blank implementations ("stubs")
*   because this is a Programming Project for my students.
*
* <dt><b>Outline of Java Source Code for this class:</b><dd>
*   <A HREF="../../../../edu/colorado/collections/DoubleLinkedSeq.java">
*   http://www.cs.colorado.edu/~main/edu/colorado/collections/DoubleLinkedSeq.java
*   </A>
*   </dl>
*
* @version
*   Jan 24, 1999
******************************************************************************/
public class DoubleLinkedSeq implements Cloneable
{

	private DoubleNode head;//pointer to first element in sequence
	private DoubleNode tail;//pointer to last element in sequence
	private DoubleNode cursor;//pointer to current element
	private DoubleNode precursor;//pointer to element just before current element
	private int manyNodes;//number of elements in sequence
   
   /**
   * Initialize an empty sequence.
   * @param - none
   * <dt><b>Postcondition:</b><dd>
   *   This sequence is empty.
   **/   
   public DoubleLinkedSeq( )
   {
	   head = null;
       tail = null;
       cursor = head;
       precursor = head;
       manyNodes = 0;
   }
    
 
   /**
   * Add a new element to this sequence, after the current element. 
   * @param element</CODE>
   *   the new element that is being added
   * <dt><b>Postcondition:</b><dd>
   *   A new copy of the element has been added to this sequence. If there was
   *   a current element, then the new element is placed after the current
   *   element. If there was no current element, then the new element is placed
   *   at the end of the sequence. In all cases, the new element becomes the
   *   new current element of this sequence. 
   * @exception OutOfMemoryError
   *   Indicates insufficient memory for a new node.
   **/
   public void addAfter(double element)
   {
		
	   if(isCurrent())
       {//if there is a current element, then add this element after
           cursor.addNodeAfter(element);//create new node
           precursor = cursor;//move precursor
           cursor = cursor.getLink();//move cursor
        }
        else
        {
            if(tail == null)
            {//when there is not a tail reference, there's no head reference either
                tail = new DoubleNode(element, null);//create new node and point
                // tail to this new node
                cursor = tail;//move cursor to the new node
                precursor = tail;//move precursor to tail
                head = tail;//move head to the same node as tail
            }
            else
            {//when tail is not null, there is a head, so do nothing there
                tail.addNodeAfter(element);//create new node and point tail to
                // this new node
                precursor = tail;//point precursor to original tail
                tail = tail.getLink();//tail gets updated link to new node
                cursor = tail;//update cursor to the new node
            }
       manyNodes++;//update invariant
    }
   }


   /**
   * Add a new element to this sequence, before the current element. 
   * @param element</CODE>
   *   the new element that is being added
   * <dt><b>Postcondition:</b><dd>
   *   A new copy of the element has been added to this sequence. If there was
   *   a current element, then the new element is placed before the current
   *   element. If there was no current element, then the new element is placed
   *   at the start of the sequence. In all cases, the new element becomes the
   *   new current element of this sequence. 
   * @exception OutOfMemoryError
   *   Indicates insufficient memory for a new node.
   **/
   public void addBefore(double element)
   {
	   if(isCurrent())
       {// if there is a current element
           if(cursor==head)
           {// where cursor is the head
               precursor = new DoubleNode(element, cursor);//add new node
               head = precursor;//move head
            }
            else
            {//if cursor is not the head
                precursor.addNodeAfter(element);//add new node
                cursor = precursor.getLink();//move cursor
            }
        }
	   
        else
        {//no current element
            if(head == null)
            {//if head is null
                head = new DoubleNode(element, null);
                cursor = head;//move cursor
                precursor = head;//move precursor
                tail = head;//move tail
            }
            else
            {//otherwise, add the node after the precursor, before cursor
                precursor.addNodeAfter(element);//add after precursor
            }
        }
        manyNodes++;//update invariant
   }
   
   
   /**
   * Place the contents of another sequence at the end of this sequence.
   * @param addend</CODE>
   *   a sequence whose contents will be placed at the end of this sequence
   * <dt><b>Precondition:</b><dd>
   *   The parameter, addend</CODE>, is not null. 
   * <dt><b>Postcondition:</b><dd>
   *   The elements from addend</CODE> have been placed at the end of 
   *   this sequence. The current element of this sequence remains where it 
   *   was, and the addend</CODE> is also unchanged.
   * @exception NullPointerException
   *   Indicates that addend</CODE> is null. 
   * @exception OutOfMemoryError
   *   Indicates insufficient memory to increase the size of this sequence.
   **/
   public void addAll(DoubleLinkedSeq addend)
   {
	   DoubleNode [] copy;
       if(addend == null)
           throw new IllegalArgumentException
               ("addAll:  addend is null");
       if(addend.size()>0)
       {
           copy = DoubleNode.listCopyWithTail(addend.head);
           //listCopyWithTail returns a 2 element array:  index[0] is the head
           // of copy, and index[1] is the tail of the copy
           tail.getLink().setLink(copy[0]);//set what used to be the last node
           // to the start of the new linked list
           copy[1].setLink(null);//set link of last item to null
           tail.setLink(copy[0]);//set tail link to the first element of the copy
           manyNodes += addend.size();//update invariant
       }    
   }   
   
   
   /**
   * Move forward, so that the current element is now the next element in
   * this sequence.
   * @param - none
   * <dt><b>Precondition:</b><dd>
   *   isCurrent()</CODE> returns true. 
   * <dt><b>Postcondition:</b><dd>
   *   If the current element was already the end element of this sequence 
   *   (with nothing after it), then there is no longer any current element. 
   *   Otherwise, the new element is the element immediately after the 
   *   original current element.
   * @exception IllegalStateException
   *   Indicates that there is no current element, so 
   *   advance</CODE> may not be called.
   **/
   public void advance( )
   {
	   if(!isCurrent())//if current there is no current element
           return;//we're at the end of the sequence, nothing to move to
        precursor = cursor;//move precursor to where cursor is
        cursor = cursor.getLink();//move cursor to the next link
   }
   
   
   /**
   * Generate a copy of this sequence.
   * @param - none
   * @return
   *   The return value is a copy of this sequence. Subsequent changes to the
   *   copy will not affect the original, nor vice versa. Note that the return
   *   value must be type cast to a DoubleLinkedSeq</CODE> before it can be used.
   * @exception OutOfMemoryError
   *   Indicates insufficient memory for creating the clone.
   **/ 
   public Object clone( )
   {  
	   DoubleLinkedSeq answer;
       try{
           answer = (DoubleLinkedSeq) super.clone();
       }
       catch(CloneNotSupportedException e)
       {
           throw new RuntimeException
               ("This class does not implement Cloneable.");
       }
       answer.head = DoubleNode.listCopy(head);
       return answer;//return object reference
   }
   

   /**
   * Create a new sequence that contains all the elements from one sequence
   * followed by another.
   * @param s1</CODE>
   *   the first of two sequences
   * @param s2</CODE>
   *   the second of two sequences
   * <dt><b>Precondition:</b><dd>
   *   Neither s1 nor s2 is null.
   * @return
   *   a new sequence that has the elements of s1</CODE> followed by the
   *   elements of s2</CODE> (with no current element)
   * @exception NullPointerException.
   *   Indicates that one of the arguments is null.
   * @exception OutOfMemoryError
   *   Indicates insufficient memory for the new sequence.
   **/   
   public static DoubleLinkedSeq catenation(DoubleLinkedSeq s1, DoubleLinkedSeq s2)
   {
	   if((s1 == null) || (s1 == null))//throw exception if either is null
           throw new IllegalArgumentException
               ("concatenation:  one argument is null");
       DoubleLinkedSeq answer = new DoubleLinkedSeq();
       answer.addAll(s1);//add all from the first sequence
       answer.addAll(s2);//add all from the second sequence
       return answer;//return the union DoubleLinkedSeq object
   }


   /**
   * Accessor method to get the current element of this sequence. 
   * @param - none
   * <dt><b>Precondition:</b><dd>
   *   isCurrent()</CODE> returns true.
   * @return
   *   the current capacity of this sequence
   * @exception IllegalStateException
   *   Indicates that there is no current element, so 
   *   getCurrent</CODE> may not be called.
   **/
   public double getCurrent( )
   {
	   if(!isCurrent())
           throw new IllegalStateException
               ("getCurrent: isCurrent() is null");
       return cursor.getData();//return double value of data at cursor
   }


   /**
   * Accessor method to determine whether this sequence has a specified 
   * current element that can be retrieved with the 
   * getCurrent</CODE> method. 
   * @param - none
   * @return
   *   true (there is a current element) or false (there is no current element at the moment)
   **/
   public boolean isCurrent( )
   {
	   if(cursor == null)
           return false;
       return true;
   }
              
   /**
   * Remove the current element from this sequence.
   * @param - none
   * <dt><b>Precondition:</b><dd>
   *   isCurrent()</CODE> returns true.
   * <dt><b>Postcondition:</b><dd>
   *   The current element has been removed from this sequence, and the 
   *   following element (if there is one) is now the new current element. 
   *   If there was no following element, then there is now no current 
   *   element.
   * @exception IllegalStateException
   *   Indicates that there is no current element, so 
   *   removeCurrent</CODE> may not be called. 
   **/
   public void removeCurrent( )
   {
       if(!isCurrent())
           throw new IllegalStateException
               ("removeCurrent: isCurrent() is null");
       
       else if(tail == head)//only one node case
       {
           head = null;
           tail = null;
           cursor = head;
           precursor = head;
           manyNodes--;//update invariant
           return;
       }
       
       else if(cursor == tail)//if cursor is at the last node
       {
           tail = precursor;//remove last node
           tail.setLink(null);
           cursor = tail;//move cursor
           precursor = head;//move precursor to beginning
           while(precursor.getLink() != cursor)
           {//and search for a link for precursor
               if(precursor.getLink() == null)
                   break;
               precursor = precursor.getLink();
           }
           manyNodes--;//update invariant
           return;
       }
       
       else if(cursor == head)//if cursor is at first node
       {
           head = head.getLink();
           cursor = head;
           precursor = head;
           manyNodes--;
           return;
       }
       
       else
       //regular case
       cursor = cursor.getLink();
       precursor.setLink(cursor);
       manyNodes--;
   }
                 
 
   
   /**
    * Adds a new element at the front of the sequence.
    * @param - element
    * The value that will be added to the front of the sequence.
    * @postcondition
    * 	The element will be added to the front of the sequence.
    **/
   public void addFront(double element)
	{
	 
	   if(head == null)
       {//if head is null
           head = new DoubleNode(element, null);
           cursor = head;//move cursor
           precursor = head;//move precursor
           tail = head;//move tail
           
       }
       else
       {
           head.addNodeAfter(element);
           head = head.getLink();
           precursor = head;
           cursor = precursor.getLink();
       }
	   
	   manyNodes++;
	 
	}
	
   /**
    * Adds a new element at the end of the sequence.
    * @param - element
    * The value that will be added at the end of the sequence.
    * @postcondition
    * 	The element will be added to the end of the sequence.
    **/
   
	public void addLast(double element)
	{
		precursor = head;
		cursor = head;
		
		for (int i = 0; manyNodes > i; i++);
		{
		precursor = cursor;
		cursor = cursor.getLink();
		
		}
		
		precursor = cursor;
		cursor = cursor.getLink();
		
		cursor.addNodeAfter(element);
		
		tail.getLink();
	
	manyNodes++;
	}
	
	   /**
	    *Removes an element at the front of the sequence.
	    * @param - none
	    * @postcondition
	    * 	First element at the front of the sequence will be removed.
	    **/
	public void removeFront()
	{
		head = head.getLink();
	}
	
	   /**
	    * Sets the current index to the end of the sequence
	    * @param - none
	    * @postcondition
	    * 	Current Index will be moved to the end of the sequence
	    **/
	public void end()
	{
		for (int i = 0; manyNodes > i; i++);
		{
		precursor = cursor;
		cursor = cursor.getLink();
		}
	}
	
	   /**
	    * Returns the ith element of the sequence.
	    * @param - element
	    * @postcondition 
	    * 	The value passed will return the indicated element at the ith location of the sequence
	    **/
	public double getElementAtIndex(int element)
	{
		return element;
	}
	
	   /**
	    * Makes the ith element become the current element.
	    * @param - element
	    * The current index will be set to the element location.
	    * @postcondition
	    * 	The value passed will set the current index to the indicated location.
	    **/
	public void setCurrent(int element)
	{
		precursor = head;
		cursor = head;
		
		for (int temp = 0; element > temp; temp++)
		{
			precursor = cursor;
			cursor = cursor.getLink();
		}
	}
      
      /**
       * Determine the number of elements in this sequence.
       * @param - none
       * @return
       *   the number of elements in this sequence
       **/ 
       public int size( )
       {
    	   return manyNodes;
       }
   
   /**
   * Set the current element at the front of this sequence.
   * @param - none
   * <dt><b>Postcondition:</b><dd>
   *   The front element of this sequence is now the current element (but 
   *   if this sequence has no elements at all, then there is no current 
   *   element).
   **/ 
       
   public void start( )
   {
	   if(head == null)//if there are no elements in the sequence
           cursor = null;//no cursor
       cursor = head;//move cursor to first node
       precursor = head;
   }
   
   public String toString()
	{
	   DoubleNode temp = head;
	   System.out.println("" + temp );
	   for (int i = 0; manyNodes > i; i++)
	   {
	   		temp.getLink();
	   }
	  
	   return "";
	}
   
}
           
