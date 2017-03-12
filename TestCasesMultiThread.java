/* Name: Dhiraj Vasant Borade
 * UFID: 4595-8142
 * COP5618 - Homework 2
 * Concurrent Searcher List Test Cases
 */

class InsertItem implements Runnable {
	
	private String insertValue;
	private ConcurrentSearcherList<String> concurrentSearcherLinkedList;
	
	public InsertItem(String string, ConcurrentSearcherList<String> list) {
		insertValue = string;
		concurrentSearcherLinkedList = list;
	}

	public void run() {
		try {
			concurrentSearcherLinkedList.insert(insertValue);
			System.out.println("Inserted item "+ insertValue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class SearchItem implements Runnable {
	
	private String searchValue;
	private ConcurrentSearcherList<String> concurrentSearcherLinkedList;
	
	public SearchItem(String string, ConcurrentSearcherList<String> list) {
		searchValue = string;
		concurrentSearcherLinkedList = list;
	}


	public void run() {
		boolean searchResult = false;
		try {
			searchResult = concurrentSearcherLinkedList.search(searchValue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (searchResult){
			System.out.println("Search Complete - Item " + searchValue + " Found in the List");
		}
		else if(!searchResult){
			System.out.println("Search Complete - Item " + searchValue + " Not Found in the List");
		}
	}
}

class RemoveItem implements Runnable {	
	
	private String removeValue;
	private ConcurrentSearcherList<String> concurrentSearcherLinkedList;
	
	public RemoveItem(String string, ConcurrentSearcherList<String> list) {
		removeValue = string;
		concurrentSearcherLinkedList = list;
	}


	public void run() {
		boolean removeStatus = false;
		try {
			removeStatus = concurrentSearcherLinkedList.search(removeValue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (removeStatus){
			System.out.println("Delete Complete - Item " + removeValue + " deleted from the List");
		}
		else if(!removeStatus){
			System.out.println("Unable to Delete - Item " + removeValue + " Not Found in the List");
		}
	}
}

public class TestCasesMultiThread{
	
	public static void main(String[] args) throws Exception {
		
		ConcurrentSearcherList<String> list = new ConcurrentSearcherList<String>();
		
		Thread insertValueList1 = new Thread(new InsertItem("D", list));
		Thread insertValueList2 = new Thread(new InsertItem("H", list));
		Thread insertValueList3 = new Thread(new InsertItem("I", list));
		Thread insertValueList4 = new Thread(new InsertItem("R", list));
		Thread insertValueList5 = new Thread(new InsertItem("A", list));
		Thread insertValueList6 = new Thread(new InsertItem("J", list));
		Thread insertValueList7 = new Thread(new InsertItem("DHIRAJ", list));
		Thread removeValueList = new Thread(new RemoveItem("T", list));
		Thread searchValueList = new Thread(new SearchItem("D", list));
		
		insertValueList1.start();
		insertValueList2.start();
		insertValueList3.start();
		insertValueList4.start();
		insertValueList5.start();
		insertValueList6.start();
		insertValueList7.start();
		removeValueList.start();
		searchValueList.start();
		insertValueList1.join();
		insertValueList2.join();
		insertValueList3.join();
		insertValueList4.join();
		insertValueList5.join();
		insertValueList6.join();
		insertValueList7.join();
		removeValueList.join();
		searchValueList.join();
		
		boolean statusFlag;
		statusFlag = list.search("D");
		if (statusFlag){
			System.out.println("FOUND - D");
		}
		else if(!statusFlag){
			System.out.println("NOT FOUND - D");
		}
		statusFlag = list.search("H");
		if (statusFlag){
			System.out.println("FOUND - H");
		}
		else if(!statusFlag){
			System.out.println("NOT FOUND - H");
		}
		statusFlag = list.search("A");
		if (statusFlag){
			System.out.println("FOUND - A");
		}
		else if(!statusFlag){
			System.out.println("NOT FOUND - A");
		}
		statusFlag = list.search("J");
		if (statusFlag){
			System.out.println("FOUND - J");
		}
		else if(!statusFlag){
			System.out.println("NOT FOUND - J");
		}
		statusFlag = list.search("DHIRAJ");
		if (statusFlag){
			System.out.println("FOUND - DHIRAJ");
		}
		else if(!statusFlag){
			System.out.println("NOT FOUND - DHIRAJ");
		}
		statusFlag = list.search("BORADE");
		if (statusFlag){
			System.out.println("FOUND - BORADE");
		}
		else if(!statusFlag){
			System.out.println("NOT FOUND - BORADE");
		}	
	}	
}