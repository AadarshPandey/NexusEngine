with open('NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/StagedEvent.java', 'r') as f:
    content = f.read()

content = content.replace('@Column(nullable = false)\n    @Column(name = "event_type")', '@Column(name = "event_type", nullable = false)')

with open('NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/StagedEvent.java', 'w') as f:
    f.write(content)
