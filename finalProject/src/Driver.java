import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import com.mongodb.client.MongoDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Driver {

	public static void main(String args[]) throws IOException {

		boolean exit = false;
		try (MongoClient client = new MongoClient("localhost", 27017)) {

			MongoDatabase database = client.getDatabase("databasecourse");
			
			Airbnb ab = new Airbnb();

			while (!exit) {

				System.out.println();
				String input;
				BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

				System.out.println("Welcome to Airbnb Listing System");

				System.out.println("Select the following List of Options");
				System.out.println("1.Display the Listing on the Basis of Locality");
				System.out.println("2.Display the Listing by taking the Number of Occupants and Price range as input");
				System.out.println("3.Display the Final Price of Lisitng by taking the Host ID as input");
				System.out.println("4.Display all those Listing where the Number of occupants is taken as Input");
				System.out.println("5.Display all those Listing where the Basic amenitites are meet ");
				System.out.println("6.Display Percentage wise distribution of Locality in New York city");
				System.out.println("7.Display all those Listing near to vicinity by providing Zipcode as input");
				System.out.println(
						"8.Display all those Listing on the basis of Room type and Number of Occupants as input");
				input = userInput.readLine();
				boolean exit1 = false;
				while(!exit1) {

				if (input.equals("1")) {

					ab.localiltyListing(database);
					exit1 = true;

				} else if (input.equals("2")) {

					ab.occPriceRangeListing(database);
					exit1 = true;

				}

				else if (input.equals("3")) {

					ab.finalPriceListing(database);
					exit1 = true;

				} else if (input.equals("4")) {

					ab.requiredOccupantsListing(database);
					exit1 = true;

				} else if (input.equals("5")) {

					ab.basicAmenitiesListing(database);
					exit1 = true;

				} else if (input.equals("7")) {

					ab.zipListing(database);
					exit1 = true;

				} else if (input.equals("6")) {
					
					ab.percentageWiseListing(database);
					
					exit1 = true;

				} else if (input.equals("8")) {

					ab.roomTypeListing(database);
					exit1 = true;

				} else {

					System.out.println("Please select one among the given options");
				}
				}
			}

		}

	}
}
