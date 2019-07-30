package ProjectFiles;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;

// Sample Test Application
public class TestApplication {

	public static void main(String args[])
	{
		System.out.println("A Simple Key-Value DataStore Application\n 1. View Data \n 2. Insert Data \n 3. Edit Data \n 4. Delete Data \n 5. Exit");
		Scanner sc=new Scanner(System.in);
		String choice_str=sc.next();
		int choice=0;
		while(!isInteger(choice_str)){
			 System.out.println("## Enter Vaild Number ##");
			 choice_str=sc.next();
		}
		choice=Integer.parseInt(choice_str);

		DataStore_Library dataStore = new DataStore_Library();
		
		if(!dataStore.initilize()){
			 System.out.println("Error Occured in DataStore");
			 System.exit(0);
		}
		
		String data,key;
		int duration;
		while(choice!=5){
			switch(choice){
				case 1:
					HashMap<String, Object> map=new HashMap<String, Object>();
					map=dataStore.fetch_data();
					if(map!=null)
						print_dataMap(map);
					else
						System.out.println("=> DataStore Empty");
					break;
					
				case 2:
					System.out.println("Enter the Data");
					data=sc.next();
					System.out.println("Enter Time to Live duration in sec as (2sec = 2000) ");
					String duration_str=sc.next();
					while(!isInteger(duration_str)){
						 System.out.println("## Enter Vaild Number ##");
						 duration_str=sc.next();
					}
					duration=Integer.parseInt(duration_str);
					if(dataStore.insert_data(data, duration))
						System.out.println("=> Data Inserted Successfully");
					else{
						System.out.println("=> Error in inserting the data");
					}
					break;
					
				case 3:
					System.out.println("Enter the Key ");
					key=sc.next();
					System.out.println("Enter the Data ");
					data=sc.next();
					if(dataStore.update_data(key,data))
						System.out.println("=> Data Updated Successfully");
					else{
						System.out.println("=> Invalid Key");
					}
					break;
					
				case 4:
					System.out.println("Enter the Key");
					key=sc.next();
					if(dataStore.delete_data(key))
						System.out.println("=> Data Deleted Successfully");
					else{
						System.out.println("=> Invalid Key");
					}
					break;
					
				default:
					System.out.println("=> Invalid Choice");
			}
			System.out.println("\n\n 1. View Data \n 2. Insert Data \n 3. Edit Data \n 4. Delete Data \n 5. Exit");
			choice_str=sc.next();
			while(!isInteger(choice_str)){
				 System.out.println("## Enter Vaild Number ##");
				 choice_str=sc.next();
			}
			choice=Integer.parseInt(choice_str);
		}
		System.out.println("");
		System.exit(0);
	
	}
	
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	private static void print_dataMap(HashMap map){
		
		 Iterator<Map.Entry<String, Object>> itr = map.entrySet().iterator(); 
		 System.out.println("");
	        while(itr.hasNext()) 
	        { 
	             Entry<String, Object> entry = itr.next(); 
	             System.out.println(" Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
	        } 
		
	}
}
