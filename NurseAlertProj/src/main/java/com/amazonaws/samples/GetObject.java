package com.amazonaws.samples;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class GetObject {
	private static String bucketName = "nursealerttest"; 
	private static String key        = "text.txt";      
	
	static class patientRequest {
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

	
	public static void main(String[] args) throws IOException {
		AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        System.out.println("Downloading an object");
        
        List<patientRequest> patientRequestsList = new ArrayList<patientRequest>();
        
        System.out.println(patientRequestsList.size());
        

        System.out.println("STARTING PRINT LOOP \n\n\n");
        
        // S3Object s3object = s3.getObject(new GetObjectRequest(bucketName, key));
        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
        String currLine;
        while((currLine = reader.readLine()) != null) {
        		// can copy the content locally as well
        		// using a buffered writer
        		System.out.println(currLine);
        		String[] parsedData= currLine.split(",");
        		System.out.println("ID: " + parsedData[0] + ", Type: " + parsedData[1] + ", Room: " + parsedData[2] + ", Req: " + parsedData[3]);
        		
        		patientRequest newPatReq = new patientRequest(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]), parsedData[3]);
        		patientRequestsList.add(newPatReq);
        }
        
        System.out.println("\n\ndone");

 
	}
}