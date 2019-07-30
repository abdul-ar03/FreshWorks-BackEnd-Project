package ProjectFiles;
import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.codehaus.jackson.map.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
@SuppressWarnings({ "unchecked", "rawtypes","unused"})
class DataStore_Library{
	private int max_key_length=32;
	private String fileName="DataStore_file.JSON";

	// Initialization function.
    public boolean initilize(){
		if(get_DataStore_File())
			return true;
		else
			return false;
    }

    // Fetch the data from dataStore.
	public HashMap fetch_data() {
		File file = new File(fileName);
		if(file.length()!=0)
		{	
			try {
				Object obj = new JSONParser().parse(new FileReader(fileName));
				if(obj!=null) {
				JSONObject jsonObject = (JSONObject) obj;
				final HashMap<String, Object> map=new HashMap<String, Object>();
				jsonObject.keySet().forEach(keyStr ->{
			        Object keyvalue = jsonObject.get(keyStr);
			        map.put((String) keyStr, keyvalue);
			    });
				return map;
				}
			} 
			catch (IOException | ParseException e) {
				e.printStackTrace();
			} 
		}
		return null;
	}

	
	// JSON File Creation function
	private boolean get_DataStore_File()
	{
		File file = new File(fileName);
		if(!file.exists())
		{	
	        try {
				file.createNewFile();
				return true;
			} 
	        catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	
	// Updating the JSON Object.
	public boolean update_data(String key,Object obj){
		
		//Check if file is empty
        int fileSize=get_fileSize();
        int gb=fileSize/(1024*1024*1024);
		if(fileSize<=1)
			return false;
		else if(gb>=1)
		{
			System.out.println("Size exceeded the limit");
			return false;
		}
			
		ObjectMapper mapper = new ObjectMapper();
		JSONObject rootObject = null;
	    try {
	    	rootObject = mapper.readValue(new File(fileName), JSONObject.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	    // Return false when key not available in the dataStore
	    if(rootObject.get(key)==null)
	    	return false;
	    
	    rootObject.put(key,obj);
	    try (FileWriter file2 = new FileWriter(fileName)) 
	    {
	          file2.write(rootObject.toString());
	          file2.close();
	          return true;
	    } catch (IOException e) {
	    	 e.printStackTrace();
	    }
	    return false;
	}

	
	// Delete the JSON Object. 
	public boolean delete_data(String key){
		//Check if file is empty
        int fileSize=get_fileSize();
        int gb=fileSize/(1024*1024*1024);
		if(fileSize<=1)
			return false;
		else if(gb>=1)
		{
			System.out.println("Size exceeded the limit");
			return false;
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		JSONObject rootObject = null;
	    try {
	    	rootObject = mapper.readValue(new File(fileName), JSONObject.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	    // Return false when not found.
	    if(rootObject.get(key)==null)
	    	return false;
	    
	    rootObject.remove(key);
	    try (FileWriter file2 = new FileWriter(fileName)) 
	    {
	    	file2.write(rootObject.toString());
	        return true;
	     } catch (IOException e) {
	    	e.printStackTrace();
	     }
	     return false;
	}

	
	// Insert new JSON Object.
	public boolean insert_data(Object obj, long duration){
      //Check if file is empty
        int fileSize=get_fileSize();
        int gb=fileSize/(1024*1024*1024);
		String new_key=generate_Key();
		
		if(fileSize<=1)
		{										 // Inserting data into new file
			 try {  
		            FileWriter fileWriter = new FileWriter(fileName);
		            JSONObject json = new JSONObject();
		            json.put(new_key,obj);
		            fileWriter.write(json.toJSONString());  
		            fileWriter.flush();  
		            fileWriter.close();
		            
		            if(duration > 0){
		                new Timer().schedule(new TimeoutTask(new_key), duration);
		            }
		            return true;
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        } 
		}
		else if(gb>=1)
		{
			System.out.println("Size exceeded the limit");
			return false;
		}
		else
		{										// Appending data into existing file
			ObjectMapper mapper = new ObjectMapper();
		    JSONObject root = null;
			try {
				root = mapper.readValue(new File(fileName), JSONObject.class);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			while(root.get(new_key)!=null)
				new_key=generate_Key();
	
		    root.put(new_key,obj);
		    									// calling time to live function
		    if(duration > 0){
                new Timer().schedule(new TimeoutTask(new_key), duration);
            }
		    									// writing data into file
		    try (FileWriter file = new FileWriter(fileName)) 
		    {
		    	file.write(root.toString());
		    	file.close();
		    }catch (IOException e) {
		    	 e.printStackTrace();
		    }
		    return true;
		}
		return false;
	}
	
	
	// Generate unique key
	private String generate_Key()
	{
		String chars = "0123456789abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  		String key="";
        for (int i = 0; i < max_key_length; i++) { 

            int index = (int)(chars.length()* Math.random()); 
            key+=chars.charAt(index);
        }
        return key; 
	}
	
	
	// Get size of file
	private int get_fileSize(){
        File file = new File(fileName);
		return (int) file.length();
	}
	
}

class TimeoutTask extends TimerTask{
    private String key;
 
    /**
     * Constructor
     * @param key The key to use & store
     */
    public TimeoutTask(String key){
        this.key = key;
    }
 
    @Override
    public void run() {
        if(this.key != null & this.key.length() > 0){
        	DataStore_Library dataStore=new DataStore_Library();
        	System.out.println("=> Key Deleted - Time to Live Function Call");
        	dataStore.delete_data(this.key);
        }
    }
}