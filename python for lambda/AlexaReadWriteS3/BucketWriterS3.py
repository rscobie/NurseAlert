import boto3
import botocore

def write_s3_bucket(input_string):
    
    #download current requests
    s3 = boto3.client('s3')
    bucket_data = s3.get_object(Bucket="nursealerttest", Key="text.txt")
    data_str = bucket_data['Body'].read().decode('utf-8')
    
    #add new patientRequest
    data_str = data_str + input_string

    #write new file
    text_file = open('/tmp/text.txt', 'w')
    text_file.write(data_str)
    text_file.close()
    
    #delete old key, not necessary I guess
    boto3.client('s3').delete_object(Bucket="nursealerttest", Key="text.txt")

    #upload new file
    s3_client = boto3.client('s3')
    s3_client.upload_file('/tmp/text.txt', 'nursealerttest', 'text.txt')

    return