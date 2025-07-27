package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    public static void main( String[] args )
    {
        Logger logger = LogManager.getLogger(App.class);
        logger.debug("This is a debug message");
    }
}
