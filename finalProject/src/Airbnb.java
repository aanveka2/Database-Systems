import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Airbnb {

	public void localiltyListing(MongoDatabase database) {
		try {

			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");

			Scanner myObj = new Scanner(System.in);
			System.out.println("Please enter the Locality");
			String input = myObj.nextLine();

			Document query = new Document();
			Document projection = new Document();

			projection.append("ID", 1.0);
			projection.append("Name", 1.0);
			projection.append("Street", 1.0);
			projection.append("Description", 1.0);
			projection.append("Accommodates", 1.0);
			projection.append("Bedrooms", 1.0);
			projection.append("Amenities", 1.0);
			projection.append("Price", 1.0);
			projection.append("Number of Reviews", 1.0);
			projection.append("Review Scores Rating", 1.0);

			projection.append("Neighbourhood Cleansed", new BsonRegularExpression(input));

			Document sort = new Document();
			sort.append("Review Scores Rating", -1.0);
			sort.append("Price", 1.0);

			int limit = 10;

			Consumer<Document> processBlock = new Consumer<Document>() {
				@Override
				public void accept(Document document) {
					System.out.println(
							"Name:    " + document.get("Name") + "\n" + "Description:  " + document.get("Description")
									+ "\n" + "Accommodates:  " + document.get("Accommodates") + "\n" + "Street:    "
									+ document.get("Street") + "\n" + "Price:    " + document.get("Price") + "\n"
									+ "Review Scores Rating:    " + document.get("Review Scores Rating"));
					System.out.println(
							"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				}
			};

			collection.find(query).projection(projection).sort(sort).limit(limit).forEach(processBlock);

		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	public void occPriceRangeListing(MongoDatabase database) {

		try {

			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");

			Scanner myObj = new Scanner(System.in);
			System.out.println("Please enter the number of Persons for Accommodation");
			int inAccommodates = myObj.nextInt();

			System.out.println("Please enter the Lower Bound of Price");
			int lowerBoundPrice = myObj.nextInt();

			System.out.println("Please enter the Upper Bound of Price");
			int upperBoundPrice = myObj.nextInt();

			Document query = new Document();
			query.append("Accommodates", new Document().append("$gte", inAccommodates));
			query.append("Price", new Document().append("$gt", lowerBoundPrice).append("$lt", upperBoundPrice));

			  int limit = 10;
			
			Consumer<Document> processBlock = new Consumer<Document>() {
				@Override
				public void accept(Document document) {
					System.out.println(
							"Name:    " + document.get("Name") + "\n" + "Description:  " + document.get("Description")
									+ "\n" + "Accommodates:  " + document.get("Accommodates") + "\n" + "Street:    "
									+ document.get("Street") + "\n" + "Price:    " + document.get("Price") + "\n"
									+ "Review Scores Rating:    " + document.get("Review Scores Rating"));
					System.out.println(
							"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				}
			};

			collection.find(query).limit(limit).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}

	}

	public void finalPriceListing(MongoDatabase database) {
		try {

			
			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");

			Consumer<Document> processBlock = new Consumer<Document>() {
				@Override
				public void accept(Document document) {
					System.out.println("ID: " + document.get("_id") + "\n" + "Name: " + document.get("Name") + "\n"
							+ "Street: " + document.get("Street") + "\n" + "Bedrooms: " + document.get("Bedrooms")
							+ "\n" + "Amenities: " + document.get("Amenities") + "\n" + "Host Response Rate: "
							+ document.get("Host Response Rate") + "\n" + "Neighbourhood Cleansed: "
							+ document.get("Neighbourhood Cleansed")+ "\n" + "Review Scores Rating: "
									+ document.get("Review Scores Rating")+ "\n" + "Final Price: "
											+ document.get("Final Price"));
					System.out.println(
							"--------------------------------------------------------------------------------------------------------------------------------------------------");
				}
			};
			Scanner myObj = new Scanner(System.in);
			System.out.println("Please enter the Id of Listing");
			String inID = myObj.nextLine();
			
			List<? extends Bson> pipeline = Arrays
					.asList(new Document().append("$match", new Document().append("ID", inID)),
							new Document().append("$project", new Document().append("ID", 1.0).append("Name", 1.0)
									.append("Street", 1.0).append("Bedrooms", 1.0).append("Amenities", 1.0)
									.append("Host Response Rate", 1.0).append("Number of Reviews", 1.0)
									.append("Review Scores Rating", 1.0).append("Neighbourhood Cleansed", 1.0)
									.append("Final Price", new Document().append("$multiply",
											Arrays.asList(1.16, new Document().append("$add",
													Arrays.asList("$Price", "$Cleaning_Fee", "$Security_Deposit")))))),
							 new Document()
	                            .append("$limit", 20.0)
							);

			collection.aggregate(pipeline).allowDiskUse(false).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}

	}

	public void requiredOccupantsListing(MongoDatabase database) {
		try  {

			
			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");

			Scanner myObj = new Scanner(System.in);
			System.out.println("Please enter the number of Persons  Required for Accommodation");
			int requiredAccomd = myObj.nextInt();

			Consumer<Document> processBlock = new Consumer<Document>() {
				@Override
				public void accept(Document document) {
					int incomingRequired = (int) document.get("required_rooms");
					// System.out.println("Incming value of rooms available"+ incomingRequired);
					if (incomingRequired >= requiredAccomd) {
						System.out.println("Name:    " + document.get("Name") + "\n" + "Available Occupants:  "
								+ document.get("required_rooms"));
						System.out.println(
								"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

					}
				}
			};

			List<? extends Bson> pipeline = Arrays.asList(new Document().append("$project",
					new Document().append("Name", 1.0).append("Description", 1.0).append("required_rooms",
							new Document().append("$multiply",
									Arrays.asList("$Host_Listings_Count", "$Accommodates")))), new Document()
                    .append("$limit", 10.0));

			collection.aggregate(pipeline).allowDiskUse(false).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}

	}

	public void basicAmenitiesListing(MongoDatabase database) {
		try  {

			
			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");
			
			System.out.println("The Basic Amentites that are defined are TV,Internet, Wireless Internet, Air conditioning, Kitchen, Heating"+"\n"+"Smoke detector,Carbon monoxide detector,First aid kit,Essentials,Shampoo ");
			Consumer<Document> processBlock = new Consumer<Document>() {
				@Override
				public void accept(Document document) {
					System.out.println("ID: " + document.get("_id") + "\n" + "Name: " + document.get("Name") + "\n"
							+ "Host_ID: " + document.get("Host_ID") + "\n" + "Amenitites: " + document.get("Amenitites")
							+ "\n" + "Price: " + document.get("Price") + "\n" + "Review Score: "
							+ document.get("Review Score") + "\n" + "Host Response Score: "
							+ document.get("Host Response Score"));
					System.out.println(
							"--------------------------------------------------------------------------------------------------------------------------------------------------");
				}
			};

			List<? extends Bson> pipeline = Arrays.asList(
					new Document().append("$match",
							new Document().append("Amenities",
									new Document().append("$in",
											Arrays.asList("TV", "Internet", "Wireless Internet", "Air conditioning",
													"Kitchen", "Heating", "Smoke detector", "Carbon monoxide detector",
													"First aid kit", "Essentials", "Shampoo")))),
					new Document().append("$group",
							new Document().append("_id", "$ID").append("Name", new Document().append("$first", "$Name"))
									.append("Host_ID", new Document().append("$first", "$Host ID"))
									.append("Amenitites", new Document().append("$first", "$Amenities"))
									.append("Price", new Document().append("$first", "$Price"))
									.append("Review Score", new Document().append("$first", "$Review Scores Rating"))
									.append("Host Response Score",
											new Document().append("$first", "$Host Response Rate"))),
					new Document().append("$sort",
							new Document().append("Review Score", -1.0).append("Review Score", -1.0)),
					new Document().append("$limit", 10.0));

			collection.aggregate(pipeline).allowDiskUse(false).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}

	}

	public void percentageWiseListing(MongoDatabase database) {
		try  {

			
			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");

			System.out.println("Percentage wise Distribution of Popular Locality of New York city");
			Consumer<Document> processBlock = new Consumer<Document>() {

				@Override
				public void accept(Document document) {
					System.out.println("Locality Name  :" + document.get("_id") + "\n" + "Distribution  in % :"
							+ document.get("Locality_Distribution(%)"));
					System.out.println(
							"--------------------------------------------------------------------------------------------------------------------------------------------------");
				}
			};

			List<? extends Bson> pipeline = Arrays.asList(
					new Document().append("$group",
							new Document().append("_id", "$Neighbourhood_Cleansed").append("count",
									new Document().append("$sum", 1.0))),
					new Document().append("$project",
							new Document().append("Locality_Distribution(%)", new Document().append("$multiply",
									Arrays.asList(100.0,
											new Document().append("$divide", Arrays.asList("$count", 19528.0)))))),
					new Document().append("$sort", new Document().append("Locality_Distribution(%)", -1.0)),
					new Document().append("$limit", 10.0));

			collection.aggregate(pipeline).allowDiskUse(false).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}

	}

	public void roomTypeListing(MongoDatabase database) {
		try  {

			
			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");
			Scanner myObj = new Scanner(System.in);
			System.out.println("Please enter the type of room you are looking for"+"\n"+"1. Entire home/apt"+"\t"+"2. Private room"+"\t"+"   3. Shared room");
			String roomType = myObj.nextLine();
			
			Consumer<Document> processBlock = new Consumer<Document>() {
				@Override
				public void accept(Document document) {
					System.out.println("ID: " + document.get("_id") + "\n" + "Name: " + document.get("Name") + "\n"
							+ "Host_ID: " + document.get("Host_ID") + "\n" + "Amenitites: " + document.get("Amenitites")
							+ "\n" + "Price: " + document.get("Price") + "\n" + "Zipcode: "
							+ document.get("Zipcode") + "\n" + "Street: "
							+ document.get("Street")+ "\n" + "RoomType: "
									+ document.get("RoomType"));
					System.out.println(
							"--------------------------------------------------------------------------------------------------------------------------------------------------");
				
				}
			};

			List<? extends Bson> pipeline = Arrays.asList(
					new Document().append("$match",
							new Document().append("Accommodates", new Document().append("$gte", 2.0))),
					new Document().append("$match", new Document().append("Room Type", roomType)),
					new Document().append("$group",
							new Document().append("_id", "$ID").append("Name", new Document().append("$first", "$Name"))
									.append("Host_ID", new Document().append("$first", "$Host ID"))
									.append("Amenitites", new Document().append("$first", "$Amenities"))
									.append("Price", new Document().append("$first", "$Price"))
									.append("Zipcode", new Document().append("$first", "$Zipcode"))
									.append("Street", new Document().append("$first", "$Street"))
									.append("RoomType", new Document().append("$first", "$Room Type"))),
					new Document().append("$sort", new Document().append("Price", 1.0)),
					new Document().append("$limit", 100.0));

			collection.aggregate(pipeline).allowDiskUse(false).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}

	}

	public void zipListing(MongoDatabase database) {
		try  {


			MongoCollection<Document> collection = database.getCollection("airbnb_NYC");

			Scanner myObj = new Scanner(System.in);
			System.out.println("Please enter the Zipcode");
			int input = myObj.nextInt();
			System.out.println("All the Nearby Locality are listed by taking Zipcode as input");
			Consumer<Document> processBlock = new Consumer<Document>() {
				@Override
				public void accept(Document document) {
					System.out.println("ID: " + document.get("_id") + "\n" + "Name: " + document.get("Name") + "\n"
							+ "Host_ID: " + document.get("Host_ID") + "\n" + "Amenitites: " + document.get("Amenitites")
							+ "\n" + "Price: " + document.get("Price") + "\n" + "Zipcode: " + document.get("Zipcode"));
					System.out.println(
							"--------------------------------------------------------------------------------------------------------------------------------------------------");
				}
			};
			
			List<? extends Bson> pipeline = Arrays.asList(
					new Document().append("$match",
							new Document().append("Zipcode",
									new Document().append("$gte", input - 1).append("$lte", input + 1))),
					new Document().append("$group",
							new Document().append("_id", "$ID").append("Name", new Document().append("$first", "$Name"))
									.append("Host_ID", new Document().append("$first", "$Host ID"))
									.append("Amenitites", new Document().append("$first", "$Amenities"))
									.append("Price", new Document().append("$first", "$Price"))
									.append("Zipcode", new Document().append("$first", "$Zipcode"))),
					new Document().append("$sort", new Document().append("Price", 1.0)),
					new Document().append("$limit", 10.0));

			collection.aggregate(pipeline).allowDiskUse(false).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}
	}

}
