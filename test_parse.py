import re
import os
import json

with open("NexusCore/mvn_output.txt", "r") as f:
    mvn_output = f.readlines()

for i, line in enumerate(mvn_output):
    if "UmsAdminServiceImpl.java:[58,35]" in line:
        print("FOUND LINE:", line.strip())
        print("SYMBOL LINE:", mvn_output[i+1].strip())
        print("LOCATION LINE:", mvn_output[i+2].strip())
        method_match = re.search(r'symbol:\s+method\s+([A-Za-z0-9_]+)', mvn_output[i+1])
        print("METHOD:", method_match.group(1) if method_match else "NONE")
        class_match = re.search(r'location:\s+(?:variable.*?of type|class|interface)\s+([A-Za-z0-9_.]+)', mvn_output[i+2])
        print("CLASS:", class_match.group(1) if class_match else "NONE")

