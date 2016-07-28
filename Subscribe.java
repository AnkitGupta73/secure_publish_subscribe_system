import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Subscribe {
static int a=0;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String topic = "demo";
		
		Properties props = new Properties();
	    props.put("bootstrap.servers", "54.206.35.155:9092");
	    props.put("group.id", "test");
	    props.put("enable.auto.commit", "true");
	    props.put("auto.commit.interval.ms", "1000");
	    props.put("session.timeout.ms", "30000");
	    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
/*	    props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", "/home/hadoop/truststore.jks");
        props.put("ssl.keystore.location", "/home/hadoop/keystore1.jks");
        props.put("ssl.truststore.password", "123456");
        props.put("ssl.keystore.password", "123456");
        props.put("ssl.key.password", "123456");
*/        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records){
                String msg = decrypt(record.value());
                if(msg.length()==0)continue;
            	if(msg.charAt(0)=='M')System.out.println("message received :\n "+msg.substring(1));
            	else{
            		byte[] b2 = msg.substring(1,5).getBytes();
            		int size= (b2[0]<<24)&0xff000000 | (b2[1]<<16)&0x00ff0000|(b2[2]<< 8)&0x0000ff00| (b2[3]<< 0)&0x000000ff;
            		String filename = msg.substring(5,size+5);
            		byte[] fb = Base64.decodeBase64(msg.substring(size+5));
            		System.out.println("file received : " +filename);
            		FileOutputStream fos = new FileOutputStream("/home/hadoop/Desktop/"+filename);
            		fos.write(fb);
            		fos.close();
            	}
            }
        }
	}
	private static String decrypt(String msg){
		String key = "dAtAbAsE98765432"; // 128 bit key

	     // Create key and cipher
		try{
	     Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
	     Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	     
	     cipher.init(Cipher.DECRYPT_MODE, aesKey);
	     String decrypted = new String(cipher.doFinal(java.util.Base64.getDecoder().decode(msg)));
	     //System.err.println("Decrypted: " + decrypted);
	     return decrypted;
		}
		catch(Exception ex){ex.printStackTrace();}
		return null;
	}

}
