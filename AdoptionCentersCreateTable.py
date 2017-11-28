from _future_ import print_function
import boto3

dynamodb = boto3.resource('dynamodb', region_name = 'us-west-2', endpoint_url"


table = dynamodb.create_table(
   TableName = 'AdoptionCenters',
   KeySchema = [
      {
         'AttributeName':'AdoptionCenterName',
         'KeyType': 'HASH'
      },
      {
         'AttributeName': 'PhoneNumber',
         'KeyType': 'RANGE' 
      },
   ],
   AttributeDefinitions = [
      {
         'AttributeName':'AdoptionCenterName',
         'AttributeType': 'S'
      },
      {
         'AttributeName':'PhoneNumber',
         'AttributeType': 'N'
      },
   ],
   ProvisionedThroughput={
      'ReadCapacityUnits' : 10,
      'WriteCapacityUnits' : 10
   }
)
