package com.example.logging.appender;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.util.Properties;

@Plugin(
        name = "Kafka",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE
)
public class KafkaAppender extends AbstractAppender {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    @PluginFactory
    public static KafkaAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("bootstrapServers") String bootstrapServer,
            @PluginElement("topic") String topic
            ) {
        return new  KafkaAppender(name, filter, layout, bootstrapServer, topic);
    }

    protected KafkaAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                            String bootstrapServers, String topic) {
        super(name, filter, layout);

        this.topic = topic;

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    @Override
    public void append(LogEvent event) {
        producer.send(new ProducerRecord<>(topic, event.getMessage().getFormattedMessage()));
    }

    @Override
    public void stop() {
        producer.close();
    }
}
