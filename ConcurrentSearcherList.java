/* Name: Dhiraj Vasant Borade
 * UFID: 4595-8142
 * COP5618 - Homework 2
 * Concurrent Searcher List Implementation
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentSearcherList<T> {

/*
 * Three kinds of threads share access to a singly-linked list:
 * searchers, inserters and deleters. Searchers merely examine the list;
 * hence they can execute concurrently with each other. Inserters add
 * new items to the front of the list; insertions must be mutually exclusive
 * to preclude two inserters from inserting new items at about
 * the same time. However, one insert can proceed in parallel with
 * any number of searches. Finally, deleters remove items from anywhere
 * in the list. At most one deleter process can access the list at
 * a time, and deletion must also be mutually exclusive with searches
 * and insertions.
 * 
 * Make sure that there are no data races between concurrent inserters and searchers!
 */

	private static class Node<T> {
		final T item;
		Node<T> next;
		Node(T item, Node<T> next) {
			this.item = item;
			this.next = next;
		}
	}
	
	/* Variables, Locks and Conditions are defined */
	private Node<T> first; 
	private int numberOfInserts = 0;
    private int numberOfRemoves = 0;
    private int numberOfSearches = 0;
    
    private final ReentrantLock lock;
    private final Condition insertCondition;
    private final Condition removeCondition;
    private final Condition searchCondition;
    
    public ConcurrentSearcherList() {
    	
    	first = null;
        lock = new ReentrantLock();
        insertCondition=lock.newCondition();
        removeCondition=lock.newCondition();
        searchCondition=lock.newCondition();    
    }
	
/**
 * Inserts the given item into the list.  
 * 
 * Precondition:  item != null
 * 
 * @param item
 * @throws InterruptedException
 */
    
    public void insert(T item) throws InterruptedException {
    	
    	assert item != null: "Error in ConcurrentSearcherList insert:  Attempt to insert null";
		start_insert();
		try {
			first = new Node<T>(item, first);
		}
		finally {
			end_insert();
		}
	}
	
/**
 * Determines whether or not the given item is in the list
 * 
 * Precondition:  item != null
 * 
 * @param item
 * @return  true if item is in the list, false otherwise.
 * @throws InterruptedException
 */
    
    public boolean search(T item) throws InterruptedException {
    	
    	assert item != null: "Error in ConcurrentSearcherList insert:  Attempt to search for null";
		start_search();
		try {
			for(Node<T> curr = first;  curr != null ; curr = curr.next) {
				if (item.equals(curr.item)) return true;
			}
			return false;
		}
		finally {
			end_search();
		}
	}
	
/**
 * Removes the given item from the list if it exists.  Otherwise the list is not modified.
 * The return value indicates whether or not the item was removed.
 * 
 * Precondition:  item != null.
 * 
 * @param item
 * @return  whether or not item was removed from the list.
 * @throws InterruptedException
 */
    
    public boolean remove(T item) throws InterruptedException {
    	
		assert item != null: "Error in ConcurrentSearcherList insert:  Attempt to remove null";
		start_remove();
		try {
			if(first == null) return false;
			if (item.equals(first.item)) {
				first = first.next;
				return true;
			}
			for(Node<T> curr = first;  curr.next != null ; curr = curr.next) {
				if (item.equals(curr.next.item)) {
					curr.next = curr.next.next;
					return true;
				}
			}
			return false;			
		}
		finally {
			end_remove();
		}
	}
	
    /* Implementation for securing lock during inserting an Item in the list */
	private void start_insert() throws InterruptedException {
		
		lock.lock();
		try {
			while(numberOfInserts != 0 && numberOfRemoves != 0) {
            insertCondition.await();
			}
			numberOfInserts++;	
		}
		finally {
			lock.unlock();	
		}   
	}

	private void end_insert() {
		
		lock.lock();
        try {
        	numberOfInserts--;
            insertCondition.signalAll();
           	removeCondition.signalAll();   	
        }
        finally {
        	lock.unlock();	
        }
	}
	
	/* Implementation for securing lock during searching an Item in the list */
	private void start_search() throws InterruptedException {
		
		lock.lock();
        try {
        	while(numberOfRemoves != 0) {
        		searchCondition.await();	
        	}
        	numberOfSearches++;	
        }
        finally {
        	lock.unlock();	
        }       
	}
	
	private void end_search(){
		
		lock.lock();
		try {
			numberOfSearches--;
			if(numberOfSearches == 0) {
				removeCondition.signalAll();	
			}	
		}
		finally {
			lock.unlock();	
		}        
	}
	
	/* Implementation for securing lock during removing an Item from the list */
	private void start_remove() throws InterruptedException {
		
        lock.lock();
        try {
            while(numberOfInserts != 0 && numberOfSearches != 0 && numberOfRemoves != 0) {
                removeCondition.await();
            }
            numberOfRemoves++;
        }
        finally {
            lock.unlock();    
        }
	}
	
	private void end_remove() {
		
		lock.lock();
		try {
			numberOfRemoves--;
            insertCondition.signalAll();
            removeCondition.signalAll();
            searchCondition.signalAll();    
		}
		finally {
			lock.unlock();	
		}	
	}
}