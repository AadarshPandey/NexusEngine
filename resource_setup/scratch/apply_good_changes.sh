#!/bin/bash
sed -i 's/import lombok.Data;/import lombok.Getter;\nimport lombok.Setter;/' ./NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OutboxEvent.java
sed -i 's/@Data/@Getter\n@Setter/' ./NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/OutboxEvent.java
sed -i 's/username: guest/username: ${RABBITMQ_USERNAME:guest}/' ./NexusCore/nexus-application/src/main/resources/application-prod.yml
sed -i 's/public class CommonResult<T> {/@lombok.Getter\n@lombok.Setter\npublic class CommonResult<T> {/' ./NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/api/CommonResult.java
sed -i '/public long getCode() {/,$d' ./NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/api/CommonResult.java
echo "}" >> ./NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/api/CommonResult.java
sed -i 's/public class CommonPage<T> {/@lombok.Getter\n@lombok.Setter\npublic class CommonPage<T> {/' ./NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/api/CommonPage.java
sed -i '/public Integer getPageNum() {/,$d' ./NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/api/CommonPage.java
echo "}" >> ./NexusCore/nexus-common/src/main/java/com/nexusengine/core/common/api/CommonPage.java
