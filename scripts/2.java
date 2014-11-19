package nosql;

import com.mongodb.AggregationOutput;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class Zad2 {
	public static void main(String[] args) throws Exception {
		MongoClient mongoClient = getConnection();
		DB db = mongoClient.getDB("mydb");

		DBCollection coll = db.getCollection("getglue");

		directorsWithMostMovies(coll);
		mostCommonMovieNames(coll);
		mostCommentedMovies(coll);
		mostLikingUsers(coll);
	}
	
	public static void directorsWithMostMovies(DBCollection coll) {
		DBObject Xmatch = (DBObject) JSON.parse("{ $match: {'director': { $regex: '^H.*'}} }");
		DBObject Xmatch2 = (DBObject) JSON.parse("{ $match: {'modelName': 'movies'} }");
		DBObject Xgroup = (DBObject) JSON.parse("{ $group: {_id: {'Typ programu':'$modelName', 'Reżyser': '$director'}, count: {$sum: 1}}");
		DBObject Xsort = (DBObject) JSON.parse(" { $sort: {count: -1} }");
		DBObject Xlimit = (DBObject) JSON.parse("{ $limit : 10}");
		
		AggregationOutput output = coll.aggregate(Xmatch, Xmatch2, Xgroup, Xsort, Xlimit);
		System.out.println(output.results());
	}

	public static void mostCommonMovieNames(DBCollection coll) {
		DBObject Xmatch = (DBObject) JSON.parse("{ $match: {'title': { $regex: '^M.*'}} }");
		DBObject Xgroup = (DBObject) JSON.parse("{ $group: {_id: {'Tytuł' : '$title'}, count: {$sum: 1}} }");
		DBObject Xsort = (DBObject) JSON.parse("{ $sort: {count: -1} }");
		DBObject Xlimit = (DBObject) JSON.parse("{ $limit : 10}");
		
		AggregationOutput output = coll.aggregate(Xmatch, Xgroup, Xsort, Xlimit);
		System.out.println(output.results());
	} 
	
	public static void mostCommentedMovies(DBCollection coll) {
		DBObject Xmatch = (DBObject) JSON.parse("{ $match: { action: 'Comment' }}");
		DBObject Xgroup = (DBObject) JSON.parse("{ $group: { _id: { Tytuł : '$title' }, 'count': {'$sum': 1} } }");
		DBObject Xsort = (DBObject) JSON.parse("{ $sort: { 'count': -1 } }");
		DBObject Xlimit = (DBObject) JSON.parse("{ $limit: 10 }");
		
		AggregationOutput output = coll.aggregate(Xmatch, Xgroup, Xsort, Xlimit);
		System.out.println(output.results());
	}
	
	public static void mostLikingUsers(DBCollection coll) {
		DBObject Xmatch = (DBObject) JSON.parse("{ $match:{ action: 'Liked' }}");
		DBObject Xgroup = (DBObject) JSON.parse("{ $group: { _id: { Użytkownik : '$userId' }, 'count': {'$sum': 1} } }");
		DBObject Xsort = (DBObject) JSON.parse("{ $sort: { 'count': -1 } }");
		DBObject Xlimit = (DBObject) JSON.parse("{ $limit: 10 }");
		
		AggregationOutput output = coll.aggregate(Xmatch, Xgroup, Xsort, Xlimit);
		System.out.println(output.results());
	}
	
	public static MongoClient getConnection() throws Exception {
		MongoClient mongoClient = new MongoClient();
		return mongoClient;
	}

}
