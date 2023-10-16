#!/bin/bash
# cd spark/ && . enter-container.sh
docker build -t shimizu517.kafka-spark-streaming.spark .
docker run -it -v $(pwd)/project:/app shimizu517.kafka-spark-streaming.spark bash
