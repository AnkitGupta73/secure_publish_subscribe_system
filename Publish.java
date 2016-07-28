import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
 
public class Publish {
	Properties props;
	KafkaProducer<String, String> producer;
	Publish(){
        props = new Properties();
        props.put("bootstrap.servers", "54.206.35.155:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); 
        //props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", "/home/hadoop/truststore.jks");
        props.put("ssl.keystore.location", "/home/hadoop/keystore.jks");
        props.put("ssl.truststore.password", "123456");
        props.put("ssl.keystore.password", "123456");
        props.put("ssl.key.password", "123456");
        producer = new KafkaProducer<String, String>(props);
	}
    void send(String topic,String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    { 
        msg = encypt(msg);
        ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, msg);
        producer.send(data);
   	}
	private static String encypt(String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		String key = "dAtAbAsE98765432"; // 128 bit key

	     // Create key and cipher
	     Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
	     Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

	     // encrypt the text
	     cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	     byte[] encrypted = cipher.doFinal(msg.getBytes());
//	     System.err.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
	     return Base64.getEncoder().encodeToString(encrypted);
	}
}