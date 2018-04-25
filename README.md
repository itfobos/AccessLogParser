# AccessLogParser

A project is created to learn Spring Boot and Spring Data, check connection of JPA 2.2 with Java 8 time API.
apache/commons-cli is used for command line parsing.


The project is about parsing access log: [log example](https://drive.google.com/file/d/1fnHvWaZb0LJQnMmugAMLaHcsPIADjbB_/view?usp=sharing)

Parser proceses web server access log file, loads the log to MySQL and checks,
 if a given IP makes more than a certain number of requests for the given duration

##### LOG Format
Date, IP, Request, Status, User Agent (pipe(|) delimited, open the example file in text editor)

Date Format: "yyyy-MM-dd HH:mm:ss.SSS"

### How the tool works
The tool takes "startDate", "duration" and "threshold" as command line arguments. "startDate" is of "yyyy-MM-dd.HH:mm:ss" format, 
"duration" can take only "hourly", "daily" as inputs and "threshold" can be an integer.

For example:

`java -jar parser.jar --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100`

The tool will find any IPs that made more than 100 requests starting from 2017-01-01.13:00:00 to 2017-01-01.14:00:00 (one hour) 
and print them to console AND also load them to another MySQL table with comments on why it's blocked.

### How to launch

Start DB container:
```
cd ./sourses
docker-compose up -d
```

MySQL will be started.

For the parser launching:

`java -jar parser.jar --startDate=2017-01-01.00:00:01 --duration=daily --threshold=200 --accesslog=/path/to/file`

