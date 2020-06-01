import random
import time
from datetime import datetime
from uuid import uuid4
from pykafka import KafkaClient

import transaction_pb2


# initializing protocol buffer schema class
Transaction = transaction_pb2.Transaction()

# generates random transaction data for 20 account numbers
def gen_data():
    Transaction.Clear()
    AC_NOs = ['4090000872969', '4090000358934', '4090009580282', '4090007525867', '4090003318142', '4090007380439', '4090002998810', '4090000608654', '4090001987091', '4090001797429', '4090000383991', '4090001752691', '4090009216541', '4090009991273', '4090001261321', '4090008077650', '4090007233629', '4090005947552', '4090004283593', '4090005908962']
    Transaction.transaction_id = uuid4().hex
    Transaction.account_number = random.choice(AC_NOs)
    Transaction.transaction_reference = uuid4().hex
    Transaction.transaction_datetime = str(datetime.now())
    Transaction.amount = round(random.uniform(10.00,1000.00),2)
    return Transaction


# connect to multiple kafka brokers
client = KafkaClient(hosts="167.71.235.115:9092")

# select kafka topic
topic = client.topics['trans']


# create a producer
with topic.get_sync_producer() as prod:
    for count in range(1000):
        # generate random transaction data
        data = gen_data()
        # serialize transaction data using protocol buffer
        # publish on the selected kafka topic
        # produce() by default selects a random partition
        prod.produce(data.SerializeToString())
        print('Count- ', count)
        print('Data - ', data)
        time.sleep(0.3)
