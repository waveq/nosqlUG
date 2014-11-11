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
			long checkCount = 0;
			MongoClient mc = new MongoClient();
			DB dbNosql = mc.getDB("mydb");
			DBCollection cTrain = dbNosql.getCollection("train");
			DBCursor cur = cTrain.find();
			long startTime = System.currentTimeMillis();
			DBObject rec;
			DBObject updateRec = new BasicDBObject();
			while(cur.hasNext()){
				try{
					rec = cur.next();
					for(String key : rec.keySet()){
						Object val = rec.get(key);
						if(key.equals("Tags")){
							String[] aTags = ((String)val).split(" ");
							val = aTags;
						}
						updateRec.put(key, val);
					}
					cTrain.update(rec, updateRec);
					checkCount++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			cur.close();
			long time = System.currentTimeMillis() - startTime;
			System.out.println("Wykonanie programu trwało: " + ((double)time)/1000.0/60 + "min");
			System.out.println("Przetworzono: " + checkCount"/"+ cTrain.count() + " rekordów.");
			System.out.println("Wszystkie tagi: "+cTrain.distinct("Tags").size());			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
