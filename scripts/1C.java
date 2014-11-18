package nosql;

import java.net.UnknownHostException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class NoSql {
	public static void main(String[] args) {
		try {
			int tagCounter = 0;
			long checkCount = 0;
			MongoClient mc = new MongoClient();
			DB dbNosql = mc.getDB("mydb");
			DBCollection cTrain = dbNosql.getCollection("train");
			System.out.println("Ilość rekordóww: "+cTrain.count());
			DBCursor cursor = cTrain.find();
			long startTime = System.currentTimeMillis();
			DBObject record;
			DBObject updateRec = new BasicDBObject();
			while(cursor.hasNext()){
				try{
					record = cursor.next();
					for(String key : record.keySet()){
						Object value = record.get(key);
						if(key.equals("Tags")){
							String[] aTags = ((String)value).split(" ");
							for(String tag : aTags) {
								tagCounter++;
							}
							value = aTags;
						}

						updateRec.put(key, value);
					}
					cTrain.update(record, updateRec);
					checkCount++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			cursor.close();
			long time = System.currentTimeMillis() - startTime;
			System.out.println("Czas działania: " + ((double)time)/1000.0 + "s");
			System.out.println("Przetworzono: " + checkCount + " rekordów.");
			System.out.println("Wszystkie różne tagi: "+cTrain.distinct("Tags").size());	
			System.out.println("Wszystkie tagi: "+tagCounter);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}