"""
Handles NurseAlert Alexa inputs from Java API, and communicates with s3
based on this tutorial:
https://medium.com/@mr_rigden/how-to-make-an-alexa-skill-with-python-cb8a6a6c4d85
"""

from BucketWriterS3 import write_s3_bucket

#builders
def build_PlainSpeech(body):
    
    speech = {}
    speech['type'] = 'PlainText'
    speech['text'] = body
    output_speech = {"outputSpeech":speech}
    return output_speech


def build_response(message, session_attributes={}):
    response = {}
    response['version'] = '1.0'
    response['sessionAttributes'] = session_attributes
    response['response'] = message
    return response
    


def lambda_handler(event, context):
   print("event: "+str(event))#debug
   print("context: "+str(context.__dict__))#debug
   if event['request']['type'] == "LaunchRequest":
        return on_launch(event, context)
   elif event['request']['type'] == "IntentRequest":
        return intent_router(event, context)
    


def intent_router(event, context):
    intent = event['request']['intent']['name']

    # Custom Intents
    if intent == "GetNeed":
        return get_need(event, context)
    # Required Intents

    if intent == "AMAZON.CancelIntent":
        return cancel_intent()

    if intent == "AMAZON.HelpIntent":
        return help_intent()

    if intent == "AMAZON.StopIntent":
        return stop_intent()
        
def on_launch(event, context):
    return build_response(build_PlainSpeech("nurse is ready"))
    
def get_need(event, context):
    request_id = "42" #deprecated, not needed any more
    employee_type = None
    patient_request = event.get('request').get('intent').get('slots').get('NeedType').get('value')
    room_number = "101"#event.get('request').get('requestId') #temporary, need to send request for address
    
    intent_name = event.get('request').get('intent').get('name')
    
    employee_types = {"Doctor":"1", "Nurse":"2","Assistant":"3","Volunteer":"4" }
    
    #send request for room address
    
    #just in case
    if patient_request == None:
        patient_request = "assistance"
    
    #determine correct employee
    if patient_request in ["water","drink","food","grub","refreshment","blanket","blankets","bed","tv","teevee","television","telly","volunteer"]:
        employee_type = "Volunteer"
    elif patient_request in ["bathroom","loo","bath","bathe","bathing","shower","showering","clean","assistant"]:
        employee_type = "Assistant"
    elif patient_request in ["results","prescription","scan","xray","doctor"]:
        employee_type = "Doctor"
    elif patient_request in ["medicine", "ivy", "pain","hurt","hurting","fell","fall", "help","nurse","assistance"]:
        employee_type = "Nurse"
    else:
        employee_type = "Nurse"
    
    bucket_list = [request_id, employee_types.get(employee_type), room_number, "\""+patient_request+"\""] 
    print("\n\n!!!!!!!: "+bucket_list.__str__()) #debug
    temp = ",".join(bucket_list)
    temp+="\n"
    write_s3_bucket(temp)
    
    return build_response(build_PlainSpeech("your request regarding " + patient_request+ " has been sent. A " + employee_type + " is on their way."))
