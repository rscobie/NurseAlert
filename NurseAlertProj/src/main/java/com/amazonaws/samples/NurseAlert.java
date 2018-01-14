package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
//import com.amazonaws.samples.GetObject.patientRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.Collection;

//Imports are listed in full to show what's being used
//could just import javafx.*
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NurseAlert extends Application {

	private static String bucketName = "nursealerttest"; 
	private static String key        = "text.txt"; 
	//patientRequest testPat = new patientRequest();
	//this.patientRequestsList.add(testPat);
	
	public class patientRequest {
	    //Doctor Request: 1
	    //Nurse Request: 2
	    //Assistant Request: 3
	    //Volunteer Request: 4
	    int requestType;
	    int requestID;
	    int roomNum;
	    String patientRequest;
	    
	    patientRequest(){
		    int requestType = 0;
		    int requestID = 0;
		    int roomNum = 0;
		    String patientRequest = "";
	    }
	    
	    patientRequest(int _requestID, int _requestType, int _roomNum, String _patientRequest) {
	    		requestType = _requestType;
	    	    requestID = _requestID;
	    	    roomNum = _roomNum;
	    	    patientRequest = _patientRequest;
	    }
	}
	
  //Execute any additional arguments to application before JavaFX start()
  public static void main(String[] args) {
      launch(args);
  }
  
  public ListView allRequests;
  
  //JavaFX Start
  @SuppressWarnings("unchecked")
@Override
  public void start(Stage primaryStage) {
      
	  //GetObject newObject = new GetObject();
	  //newObject.start();
	  
	  
	  List<patientRequest> patientRequestsList;
	  
      //primaryStage is the top-level container
      primaryStage.setTitle("NurseAlert Console UI v0.31");

      //The BorderPane has the same areas laid out as the BorderLayout layout manager
      BorderPane componentLayout = new BorderPane();
      componentLayout.setPadding(new Insets(20,20,20,20));
      
      HBox hbox1 = new HBox();
      HBox hbox2 = new HBox();
      HBox hbox3 = new HBox();
      componentLayout.setTop(hbox1);
      componentLayout.setRight(hbox2);
      componentLayout.setBottom(hbox3);
      componentLayout.setStyle("-fx-font-size:12px");
      
      //The FlowPane is a container that uses a flow layout
      final FlowPane choicePane = new FlowPane();
      choicePane.setHgap(100);
      
      //Request List select buttons
      Button viewDoctorRequests = new Button("Doctor Requests");
      Button viewNurseRequests = new Button("Nurse Requests");
      Button viewAssistantRequests = new Button("Assistant Requests");
      Button viewVolunteerRequests = new Button("Volunteer Requests");
      
      Button removeRequest = new Button("Remove Request");
      
      //Add the label and buttons to flowpane
      hbox1.getChildren().add(viewDoctorRequests);
      hbox1.getChildren().add(viewNurseRequests);
      hbox1.getChildren().add(viewAssistantRequests);
      hbox1.getChildren().add(viewVolunteerRequests);
      
      hbox3.getChildren().add(removeRequest);
      
      //put the flowpane in the top area of the BorderPane
      //componentLayout.setTop(choicePane);
          
      final FlowPane listPane = new FlowPane();
      //listPane.setHgap(100);
      
      hbox2.getChildren().add(listPane);
      
      //ListView doctorRequests = patientRequest.getDoctorList();
      ListView doctorRequests = new ListView(FXCollections.observableArrayList("Initializing Connection to DB"));
      //ListView doctorRequests = patientRequest.getDoctorList();
      ListView nurseRequests = new ListView(FXCollections.observableArrayList());
      //ListView doctorRequests = patientRequest.getDoctorList();
      ListView assistantRequests = new ListView(FXCollections.observableArrayList());
      //ListView doctorRequests = patientRequest.getDoctorList();
      ListView volunteerRequests = new ListView(FXCollections.observableArrayList());
      
      viewDoctorRequests.setOnAction(null);
      
      allRequests = doctorRequests;
      
      //listPane.setBorder(0);
      
      listPane.getChildren().add(allRequests);
      //listPane.setMinWidth(800);
      //componentLayout.setRight(listPane);
      //componentLayout.setCenter(listPane);
      //listPane.setLayoutX(10000);
      //listPane.setLayoutY(100);
      //componentLayout.setLayoutX(100);
      //componentLayout.setLayoutY(100);
      
      viewDoctorRequests.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {

				//Fetch new DB data for list refresh
				ArrayList<patientRequest> employeeRequests = new ArrayList<patientRequest>();
				
				AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, key));
		        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
		        String currLine;
		        
		        try {
			        while((currLine = reader.readLine()) != null) {
			        		System.out.println(currLine);
			        		String[] parsedData= currLine.split(",");

			        		patientRequest newPatReq = new patientRequest(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]), parsedData[3]);
			        		if(newPatReq.requestType == 1) {
			        			
			        			employeeRequests.add(newPatReq);
			        		}
			        }
		        }
		        
		        catch(IOException e) {
		        		System.out.println("failed");
		        }
				
		        //Add specific employee requests to table for display
				List<String> ourCollection = new ArrayList<>();
				for (patientRequest currentPatientRequest : employeeRequests) {
					String request = "Room " + currentPatientRequest.roomNum + " requests the doctor for reason " + currentPatientRequest.patientRequest;
					ourCollection.add(request);
				}
				
				//Add requests and refresh table
				allRequests = new ListView(FXCollections.observableArrayList(ourCollection));
				listPane.getChildren().remove(0);
		        listPane.getChildren().add(allRequests);
			}
      });
      viewNurseRequests.setOnAction(new EventHandler() {
    	  
			@Override
			public void handle(Event arg0) {

				//Fetch new DB data for list refresh
				ArrayList<patientRequest> employeeRequests = new ArrayList<patientRequest>();
				
				AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, key));
		        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
		        String currLine;
		        
		        try {
			        while((currLine = reader.readLine()) != null) {
			        		System.out.println(currLine);
			        		String[] parsedData= currLine.split(",");

			        		patientRequest newPatReq = new patientRequest(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]), parsedData[3]);
			        		if(newPatReq.requestType == 2) {
			        			
			        			employeeRequests.add(newPatReq);
			        		}
			        }
		        }
		        
		        catch(IOException e) {
		        		System.out.println("failed");
		        }
				
		        //Add specific employee requests to table for display
				List<String> ourCollection = new ArrayList<>();
				for (patientRequest currentPatientRequest : employeeRequests) {
					String request = "Room " + currentPatientRequest.roomNum + " requests a nurse for reason " + currentPatientRequest.patientRequest;
					ourCollection.add(request);
				}
				
				//Add requests and refresh table
				allRequests = new ListView(FXCollections.observableArrayList(ourCollection));
				listPane.getChildren().remove(0);
		        listPane.getChildren().add(allRequests);
			}
      });
      viewAssistantRequests.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {

				//Fetch new DB data for list refresh
				ArrayList<patientRequest> employeeRequests = new ArrayList<patientRequest>();
				
				AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, key));
		        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
		        String currLine;
		        
		        try {
			        while((currLine = reader.readLine()) != null) {
			        		System.out.println(currLine);
			        		String[] parsedData= currLine.split(",");

			        		patientRequest newPatReq = new patientRequest(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]), parsedData[3]);
			        		if(newPatReq.requestType == 3) {
			        			
			        			employeeRequests.add(newPatReq);
			        		}
			        }
		        }
		        
		        catch(IOException e) {
		        		System.out.println("failed");
		        }
				
		        //Add specific employee requests to table for display
				List<String> ourCollection = new ArrayList<>();
				for (patientRequest currentPatientRequest : employeeRequests) {
					String request = "Room " + currentPatientRequest.roomNum + " requests an assistant for reason " + currentPatientRequest.patientRequest;
					ourCollection.add(request);
				}
				
				//Add requests and refresh table
				allRequests = new ListView(FXCollections.observableArrayList(ourCollection));
				listPane.getChildren().remove(0);
		        listPane.getChildren().add(allRequests);
			}
      });
      viewVolunteerRequests.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {

				//Fetch new DB data for list refresh
				ArrayList<patientRequest> employeeRequests = new ArrayList<patientRequest>();
				
				AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, key));
		        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
		        String currLine;
		        
		        try {
			        while((currLine = reader.readLine()) != null) {
			        		System.out.println(currLine);
			        		String[] parsedData= currLine.split(",");

			        		patientRequest newPatReq = new patientRequest(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]), parsedData[3]);
			        		if(newPatReq.requestType == 4) {
			        			
			        			employeeRequests.add(newPatReq);
			        		}
			        }
		        }
		        
		        catch(IOException e) {
		        		System.out.println("failed");
		        }
				
		        //Add specific employee requests to table for display
				List<String> ourCollection = new ArrayList<>();
				for (patientRequest currentPatientRequest : employeeRequests) {
					String request = "Room " + currentPatientRequest.roomNum + " requests a volunteer for reason " + currentPatientRequest.patientRequest;
					ourCollection.add(request);
				}
				
				//Add requests and refresh table
				allRequests = new ListView(FXCollections.observableArrayList(ourCollection));
				listPane.getChildren().remove(0);
		        listPane.getChildren().add(allRequests);
			}
      });
      removeRequest.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {

				//Fetch new DB data for list refresh
				ArrayList<patientRequest> employeeRequests = new ArrayList<patientRequest>();
				
				AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, key));
		        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
		        String currLine;
		        
		        try {
			        while((currLine = reader.readLine()) != null) {
			        		System.out.println(currLine);
			        		String[] parsedData= currLine.split(",");

			        		patientRequest newPatReq = new patientRequest(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]), parsedData[3]);
			        		if(newPatReq.requestType == 0) {
			        			
			        			employeeRequests.add(newPatReq);
			        		}
			        }
		        }
		        
		        catch(IOException e) {
		        		System.out.println("failed");
		        }
				
		        //Add specific employee requests to table for display
				List<String> ourCollection = new ArrayList<>();
				for (patientRequest currentPatientRequest : employeeRequests) {
					String request = "Room " + currentPatientRequest.roomNum + " requests the doctor for reason " + currentPatientRequest.patientRequest;
					ourCollection.add(request);
				}
				
				//Add requests and refresh table
				allRequests = new ListView(FXCollections.observableArrayList(ourCollection));
				listPane.getChildren().remove(0);
		        listPane.getChildren().add(allRequests);
			}
    });
      
      //Add the BorderPane to the Scene
      Scene appScene = new Scene(componentLayout,575,475);
      
      //Add the Scene to the Stage
      primaryStage.setScene(appScene);
      primaryStage.show();
  }
}