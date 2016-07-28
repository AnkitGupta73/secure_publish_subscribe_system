# secure_publish_subscribe_system
A secure communication system in a distributed computing environment which allows publishers to publish files and data on various topics and allows subscribers to retrieve all the files/data related to a topic.
<br>Apache kafka is used for implementing the publish subscribe system. It stores messages as logs and support maximum message size of 10^6 bytes. The kafka broker is running on amazon EC2 cluster.
<br>The file/data is encrypted using AES-128 bit encryption before sending and is decrypted at subscriber.
<br>A certifying authority is created which signs certificates to authenticate  the cluster nodes.
<br>A truststore and keystore is created at each node in the cluster.
<br>The truststore contains the certificate of the certifying authority and signfies that the certificates signed by this authority are to be trusted.
<br>Keystore contains the signed certificate of the node itself and is used by kafka to send certificates over the network.
<br>Support for sending files over the kafka network is carried out by converting the file into a String using Apache commons library.
<br>File name and format are reserved.
<br>Currently support for sending of 1MB files.
<br>Message format : M{message body}
<br>File format : F{4 bytes for storing size of filename and format}{filename and format}{file}
