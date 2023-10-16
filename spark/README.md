## How I created this project structure from scratch

1. Started and entered maven container first
```bash
cd spark && . ./enter-container.sh
```

2. Created a maven project with archetype
```bash
mvn archetype:generate -DgroupId=shimizu517.kafkasparkstreaming.app -DartifactId=kafka-spark-streaming -DarchetypeArtifaceId=maven-archetype-simple -DarchetypeVersion=1.4 -DinteractiveMode=false
```

## Build
```bash
cd kafka-spark-streaming
mvn clean package
```

## References
- 
