package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		try {
			Input input = JsonInputReader.getInputFromJson(args[0]); //Read Json
			Ewoks ewoks = Ewoks.get();//Create Ewoks
			for (int i = 0; i < input.getEwoks(); ++i){ewoks.addEwok();}
			Thread hanSoloMicroservice = new Thread(new HanSoloMicroservice()); //Create Threads
			Thread landoMicroservice = new Thread(new LandoMicroservice(input.getLando()));
			Thread c3POMicroservice = new Thread(new C3POMicroservice());
			Thread r2D2Microservice = new Thread(new R2D2Microservice(input.getR2D2()));
			Thread leiaMicroservice = new Thread(new LeiaMicroservice(input.getAttacks()));
			hanSoloMicroservice.start();
			landoMicroservice.start();
			c3POMicroservice.start();
			r2D2Microservice.start();
			leiaMicroservice.start();
			hanSoloMicroservice.join(); // make sure all the threads has finished before creating the output.
			landoMicroservice.join();
			c3POMicroservice.join();
			leiaMicroservice.join();
			r2D2Microservice.join();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();//Write Json
			try (FileWriter writer = new FileWriter(args[1])) {
				gson.toJson(Diary.getDiary(), writer);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
