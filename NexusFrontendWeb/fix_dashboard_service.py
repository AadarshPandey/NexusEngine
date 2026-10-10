import re

with open("../NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/DashboardServiceImpl.java", "r", encoding="utf-8") as f:
    content = f.read()

# Replace imports
content = content.replace("import java.time.LocalDateTime;", "import java.time.LocalDateTime;\nimport java.time.OffsetDateTime;\nimport java.time.ZoneOffset;")

# Replace variable declarations to OffsetDateTime
content = content.replace("LocalDateTime todayStart = today.atStartOfDay();", "OffsetDateTime todayStart = today.atStartOfDay().atOffset(ZoneOffset.UTC);")
content = content.replace("LocalDateTime yesterdayStart = yesterday.atStartOfDay();", "OffsetDateTime yesterdayStart = yesterday.atStartOfDay().atOffset(ZoneOffset.UTC);")
content = content.replace("LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();", "OffsetDateTime todayEnd = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);")

content = content.replace("LocalDateTime monthStart = firstDayOfMonth.atStartOfDay();", "OffsetDateTime monthStart = firstDayOfMonth.atStartOfDay().atOffset(ZoneOffset.UTC);")
content = content.replace("LocalDateTime weekStart = firstDayOfWeek.atStartOfDay();", "OffsetDateTime weekStart = firstDayOfWeek.atStartOfDay().atOffset(ZoneOffset.UTC);")

content = content.replace("LocalDateTime dStart = d.atStartOfDay();", "OffsetDateTime dStart = d.atStartOfDay().atOffset(ZoneOffset.UTC);")
content = content.replace("LocalDateTime dEnd = d.plusDays(1).atStartOfDay();", "OffsetDateTime dEnd = d.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);")

with open("../NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/DashboardServiceImpl.java", "w", encoding="utf-8") as f:
    f.write(content)

print("Fixed DashboardServiceImpl.java")
