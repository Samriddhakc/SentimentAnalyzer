#Extract info from reddit using the PRAW Wrapper.
import praw 
import time 
from kafka import KafkaProducer
def main(): 
    reddit=praw.Reddit(client_id="ouEdSOnGae56pg",client_secret="GKYAH2Wz1OKLH-ddmJBs87x_UMg",user_agent="webapp:com.SentimentAnalyzer.extractReddit:v1.0.0")
    producer = KafkaProducer(bootstrap_servers=['localhost:9092','localhost:9094','localhost:9095'])
    indexer=0
    while True: 
        for submission in reddit.subreddit("coronavirus").hot(limit=100):
            producer.send("reddit",key=bytes(str(indexer),'utf-8'),value=bytes(str(submission.title),'utf-8'))
            indexer+=1 
        time.sleep(1)
main()