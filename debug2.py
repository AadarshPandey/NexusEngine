import re
with open("NexusCore/mvn_output.txt", "r") as f:
    mvn_output = f.readlines()
for i, line in enumerate(mvn_output):
    match = re.search(r'\[ERROR\] (.*?\.java):\[(\d+),\d+\] cannot find symbol', line)
    if match and "UmsAdminServiceImpl" in line:
        symbol_line = mvn_output[i+1]
        method_match = re.search(r'symbol:\s+method\s+([A-Za-z0-9_]+)', symbol_line)
        location_line = mvn_output[i+2]
        class_match = re.search(r'location:\s+(?:variable.*?of type|class|interface)\s+([A-Za-z0-9_.]+)', location_line)
        print(f"Line {match.group(2)} - Method: {method_match} - Class: {class_match}")
