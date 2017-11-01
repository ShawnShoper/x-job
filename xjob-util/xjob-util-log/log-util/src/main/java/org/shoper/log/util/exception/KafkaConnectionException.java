package org.shoper.log.util.exception;

/**
 * Created by ShawnShoper on 2017/5/22.
 * KafkaConnectionException class
 */
public class KafkaConnectionException extends RuntimeException{
    public KafkaConnectionException(String message) {
        super(message);
    }
}
