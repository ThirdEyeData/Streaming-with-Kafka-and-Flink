import logging
from datetime import datetime
from pykafka import KafkaClient

import transaction_pb2

# logging
logging.basicConfig(filename=f'consumer_{str(datetime.now())}.log',level=logging.INFO)

# initializing protocol buffer schema class
transaction = transaction_pb2.Transaction()

# connect to multiple kafka brokers
client = KafkaClient(hosts="167.71.235.115:9092,167.71.235.115:9093")

# select kafka topic
topic = client.topics['trans']

# create kafka consumer
cons = topic.get_simple_consumer()

# all the account numbers and aggregated balance is stored in a dict
data_dict = {}

for msg in cons:
    if msg is not None:
        # deserialize the message
        #print(msg.value)
        parsed = transaction.FromString(msg.value)
        ac_no = parsed.account_number
        amnt = parsed.amount
        
        # add account number if not in dict
        if data_dict.get(ac_no):
            # sum the amount of the transaction
            data_dict[ac_no] = round(data_dict[ac_no] + amnt,2)

        else:
            data_dict[ac_no] = round(amnt, 2)
        print(data_dict)
        # logging the total balance and account number in log files
        logging.info(f'ac_no: {ac_no} , balance: {data_dict[ac_no]}')
        logging.info(f'{(ac_no, data_dict[ac_no])}')
