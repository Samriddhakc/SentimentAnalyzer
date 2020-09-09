package com.samriddhakc.mlprojects.SentimentAnalyzerREST;
import java.util.*;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.kafka.common.serialization.StringDeserializer;
import scala.Tuple2;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.Metadata;


public class SentimentStream {
	
	Map<String, Object> kafkaParams = new HashMap<>();

	public SentimentStream() throws InterruptedException{
		
		
		//JavaSparkContext javaSparkContext = JavaSparkContext.fromSparkContext(sc) //new JavaSparkContext(conf);
		SparkSession spark = SparkSession
				.builder()
				.appName("Java Spark SQL basic example")
				.config("spark.master", "local[*]")
				.config("spark.driver.allowMultipleContexts", "true")
				.getOrCreate();
		
		List<Row> dataTest= Arrays.asList(
			    RowFactory.create(1,"I lovess you"),
			    RowFactory.create(2,"I am so happy"),
			    RowFactory.create(3,"I hate good mood")
			);

		StructType schema = new StructType(new StructField[]{
			    new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
			    new StructField("text",  DataTypes.StringType,false, Metadata.empty())
			});
		Dataset<Row> test =spark.createDataFrame(dataTest, schema);
		
		PipelineModel model=PipelineModel.load("/Users/samriddhakc/Desktop/mlmodel");
		Dataset<Row> predictions=model.transform(test);
		for (Row r : predictions.select("id", "text", "probability", "prediction").collectAsList()) {
			  System.out.println("(" + r.get(0) + ", " + r.get(1) + ") --> prob=" + r.get(2)
			    + ", prediction=" + r.get(3));
			}
		
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount").set("spark.driver.allowMultipleContexts", "true");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		kafkaParams.put("bootstrap.servers","localhost:9092,localhost:9093,localhost:9094");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);
	
		Collection<String> topics = Arrays.asList("reddit");
		
		JavaInputDStream<ConsumerRecord<String, String>> stream =
		  KafkaUtils.createDirectStream(
				  jssc,
		    LocationStrategies.PreferConsistent(),
		    ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
		  );
		
		//stream.mapToPair(record -> new Tuple2<>(record.key(), record.value()));
		stream.map(record->(record.value())).print();
		//stream.print();
		jssc.start();
		jssc.awaitTermination();
	
	}
	
	
	
	/*public String preprocess_sentiments(String tweet){ 
	    //wordLemm=WordNetLemmatizer()
	    user_pattern="@[^\s]+";
	    alpha_pattern="[^a-zA-Z0-9]"
	    sequence_pattern=r"(.)\1\1+"
	    sequence_replace_pattern= r"\1\1"
	    tweet=tweet.lower()
	    tweet=re.sub(url_pattern,' URL',tweet)
	    for emoji in emojis.keys(): 
	        tweet=tweet.replace (emoji,"EMOJ"+emojis[emoji])
	    tweet=re.sub(user_pattern,' USER',tweet)
	    #tweet=re.sub(alpha_pattern," ",tweet)
	    tweet=re.sub(sequence_pattern,sequence_replace_pattern,tweet)
	    tweet_words=""
	    for word in tweet.split(' '): 
	        if len(word)>1 and word not in stopwordlist: 
	            word=wordLemm.lemmatize(word)
	            tweet_words+=(word+' ')
	    return tweet_words
	}*/
}
	

